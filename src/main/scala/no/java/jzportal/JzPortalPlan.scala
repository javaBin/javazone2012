package no.java.jzportal

import java.io._
import java.net._
import javax.servlet.FilterConfig
import javax.servlet.http._
import no.arktekk.cms.CmsUtil._
import no.arktekk.cms.atompub._
import no.arktekk.cms.{Logger => CmsLogger, _}
import no.java.jzportal.twitter._
import org.apache.commons.io.IOUtils
import org.constretto.Converter._
import org.constretto._
import org.joda.time.Minutes._
import org.joda.time._
import org.joda.time.format._
import org.slf4j._
import scala.util.control._
import scala.xml._
import unfiltered.filter._
import unfiltered.request._
import unfiltered.response._

// TODO: Wrap an unfiltered kit around this plan that prevent all methods except GET (and possibly HEAD)
class JzPortalPlan extends Plan {
  val pageSize = Positive.fromInt(10)

  var logger: Logger = null
  var atomPubClient: AtomPubClient = null
  var cmsClient: CmsClient = null
  var twitterClient: TwitterSearch = null
  var aboutJavaZone: URL = null
  var aboutJavaBin: URL = null
  var twitterSearchHtmlUrl: URL = null

  import html._

  /**
   * Conforms to http://tools.ietf.org/html/draft-ietf-httpbis-p2-semantics-18#section-8.
   * Example: Sun, 06 Nov 1994 08:49:37 GMT
   */
  val httpDateFormat = DateTimeFormat.forPattern("E, dd MMM yyyy HH:mm:ss 'GMT'").withZone(DateTimeZone.UTC)

  case class CacheControl(maxAge: Long, mustRevalidate: Boolean) extends Responder[Any] {
    def respond(res: HttpResponse[Any]) {
      res.header("Cache-Control",
        (if(maxAge > 0) "max-age=" + maxAge + ", " else "no-cache, ") +
        (if(mustRevalidate) "must-revalidate" else "")
      )
    }
  }

  object CacheOneDay extends CacheControl(86400L, false)
  object CacheOneDayMustRevalidate extends CacheControl(86400L, true)
  object NoCache extends CacheControl(0L, true)

  case class LastModified private(timestamp: Long) extends Responder[Any] {

    def this(entry: CmsEntry) = this(entry.updatedOrPublished.map(_.getMillis).getOrElse(0L))

    def this(connection: URLConnection) = this(connection.getLastModified)

    def respond(res: HttpResponse[Any]) {
      if(timestamp > 0) {
        res.header("Last-Modified", httpDateFormat.print(timestamp))
      }
    }
  }

  def intent = {
    filterIntent(newsIntent.orElse(pagesIntent.orElse(fallbackIntent)))
  }

  def filterIntent(intent: Plan.Intent) = new Plan.Intent {
    val f = Intent {
      case x@GET(_) =>
        intent.apply(x)
      case x@HEAD(_) =>
        intent.apply(x)
      case _ => MethodNotAllowed ~>
        Allow("GET", "HEAD") ~>
        PlainTextContent ~>
        ResponseString("Only GET and HEAD are allowed.")
    }

    def isDefinedAt(x: HttpRequest[HttpServletRequest]) = f.isDefinedAt(x) && intent.isDefinedAt(x)

    def apply(x: HttpRequest[HttpServletRequest]) = f(x)
  }

  def fallbackIntent = Intent {
    case Path(Seg(Nil)) =>
      Redirect("/news.html")

    case Path(Seg("dump" :: Nil)) =>
      def dumpEntry(indent: Int)(entry: CmsEntry): String = {
        ("".padTo(indent, ' ') + "Title=" + entry.title + ", slug=" + entry.slug + ", id=" + entry.id + ", categories=" + entry.categories) + "\n" +
        cmsClient.fetchChildrenOf(entry.slug).map(_.map(dumpEntry(indent + 1))).map(_.mkString("\n")).getOrElse("No children")
      }
      val lines = cmsClient.fetchTopPages().map(dumpEntry(0))
      Ok ~> NoCache ~> PlainTextContent ~> unfiltered.response.ResponseString(lines.mkString("\n") + "\n")

    case Path(Seg("flush" :: Nil)) =>
      cmsClient.flushCaches()
      Redirect("/news.html")

    case Path(Seg("favicon.ico" :: Nil)) =>
      Redirect("http://java.no/favicon.ico")

    case req & Path(p) =>
      Option(classOf[JzPortalPlan].getClassLoader.getResource("webapp" + p)) match {
        case Some(url) =>
          logger.info("Found resource: /webapp" + p)
          val connection = url.openConnection()
          val input = connection.getInputStream
          Ok ~> CacheOneDay ~> new LastModified(connection) ~> PathBasedContentTypeResponder(p) ~> new ResponseStreamer {
            def stream(os: OutputStream) {
              try {
                IOUtils.copy(input, os)
              } finally {
                IOUtils.closeQuietly(input)
              }
            }
          }
        case None =>
          logger.info("Not found: /webapp" + p)
          NotFound ~> NoCache ~> Html5(notFound(default(), p))
      }
  }

  def conditionMatches(entry: CmsEntry, req: HttpRequest[Any]): Boolean = entry.updatedOrPublished match {
    case Some(dateTime) => req match {
      case IfModifiedSince(clientDate) =>
        (clientDate.getTime >= dateTime.withMillisOfSecond(0).getMillis)
      case _ =>
        false
    }
    case _ =>
      false
  }

  def newsIntent = Intent {
    case Path(Seg("news.html" :: Nil)) & Params(params) =>
      val start = params.get("start").flatMap(_.headOption)
      Ok ~> CacheOneDayMustRevalidate ~> Html5(renderNewsList(start))

    case req@Path(Seg("news" :: slug :: Nil)) & Path(p) if slug.endsWith(".html") =>
      val s = slug.replaceFirst("\\.html$", "")
      cmsClient.fetchPostBySlug(CmsSlug.fromString(s)) match {
        case None =>
          NotFound ~> NoCache ~> Html5(notFound(default(), p))
        case Some(entry) =>
          CacheOneDayMustRevalidate ~>
            new LastModified(entry) ~>
            (if (conditionMatches(entry, req)) {
              NotModified
            }
            else {
              if (req.method == "HEAD")
                // Unfiltered sets "Content-Length: 0" :(
                Ok
              else
                Ok ~> Html5(news(default(), entry))
            })
      }
  }

  def pagesIntent = Intent {
    case Page(page) & Path(path) if path.endsWith(".html") =>
      Ok ~> CacheOneDayMustRevalidate ~> new LastModified(page) ~> Html5(renderPage(page))
  }

  def default(): default = {
    def fetchEntry(url: URL): Option[NodeSeq] =
      cmsClient.fetchEntry(url).map(_.content)

    val topPages = cmsClient.fetchTopPages()
    val aboutJavaZone = fetchEntry(this.aboutJavaZone).getOrElse(Nil)
    val aboutJavaBin = fetchEntry(this.aboutJavaBin).getOrElse(Nil)
    new default(topPages, aboutJavaZone, aboutJavaBin)
  }

  def renderNewsList(start: Option[String]) = {
    val offset = start.flatMap(parseInt).getOrElse(0)

    val response = cmsClient.fetchEntriesForCategory("news", offset, pageSize)
    val tweets = twitterClient.currentResults

    news(default(), twitterSearchHtmlUrl, tweets, response)
  }

  def renderPage(p: CmsEntry) = {
    val siblings = for {
      parent <- cmsClient.fetchParentOfPageBySlug(p.slug)
      (prev, item, next) <- cmsClient.fetchSiblingsOf(p.slug)
    } yield (parent, prev, item, next)

    val children = cmsClient.fetchChildrenOf(p.slug)
    page(default(), p, children, siblings)
  }

  object Page {
    def unapply[T](req: HttpRequest[T]): Option[CmsEntry] = {
      val list = req.uri.split('?')(0).split("/").toList
      list match {
        case "" :: slug :: Nil if slug.endsWith(".html") =>
          val s = slug.replaceFirst("\\.html$", "")
          cmsClient.fetchPageBySlug(CmsSlug.fromString(s))
        case _ =>
          None
      }
    }
  }

  override def init(config: FilterConfig) {
    super.init(config)

    logger = org.slf4j.LoggerFactory.getLogger("JzPortal")

    CmsUtil.skipEhcacheUpdateCheck

    val constretto = loadConstretto()

    def hubCallback(hub: URL, topic: URL) {}

    // CMS Integration
    val cmsLogger = new CmsLogger {
      private val logger = org.slf4j.LoggerFactory.getLogger("CMS")

      def info(message: String) { logger.info(message) }

      def warn(message: String) { logger.warn(message) }
    }

    // TODO: Make the cache directory configurable
    val cmsCacheDir = new File("target/cms-cache")
    val atomPubClientConfiguration = new AtomPubClientConfiguration(cmsLogger, "CMS", cmsCacheDir, None, Some(minutes(10)), Some(CachingAbderaClient.confluenceFriendlyRequestOptions))
    val atomPubClient = AtomPubClient(atomPubClientConfiguration)
    val cmsConfiguration = CmsClient.ExplicitConfiguration(
      constretto("cms.postsFeed")(urlConverter),
      constretto("cms.pagesFeed")(urlConverter))
    val cmsClient = new DefaultCmsClient(cmsLogger, atomPubClient, cmsConfiguration, hubCallback)

    this.aboutJavaZone = constretto("cms.snippets.about_javazone")(urlConverter)
    this.aboutJavaBin = constretto("cms.snippets.about_javabin")(urlConverter)
    val twitterSearchAtomUrl = constretto("twitter.search.atom")(urlConverter)
    this.twitterSearchHtmlUrl = constretto("twitter.search.html")(urlConverter)
    this.atomPubClient = atomPubClient
    this.cmsClient = cmsClient

    // Twitter search integration
    this.twitterClient = {
      println("twitter search = " + twitterSearchAtomUrl)
      val logger = LoggerFactory.getLogger("twitter.client")
      val twitterClient = new TwitterClientActor(logger, minutes(1), twitterSearchAtomUrl.toURI)
      twitterClient ! TwitterClient.Update
      twitterClient.start()
      logger.info("Starting twitter search: " + twitterSearchAtomUrl)
      twitterClient
    }
  }

  override def destroy() {
    logger.info("Closing CMS client")
    Exception.allCatch either cmsClient.close()
    logger.info("Closed CMS client")
  }

  def loadConstretto() = {
    val stores = Seq(Constretto.properties("classpath:default.properties"))

    val withResources = stores.foldLeft(new ConstrettoBuilder)(_.addConfigurationStore(_))
    val withTags = constrettoTags.foldLeft(withResources)(_.addCurrentTag(_))
    Constretto.configuration(withTags.getConfiguration)
  }

  def constrettoTags = {
    val hostname = try {
      Some(InetAddress.getLocalHost.getHostName)
    } catch {
      case _ => None
    }

    List(Some("dev"), hostname).flatten
  }
}
