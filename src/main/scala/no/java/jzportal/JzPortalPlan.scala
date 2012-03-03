package no.java.jzportal

import java.io._
import java.net._
import javax.servlet.FilterConfig
import org.joda.time.format._
import no.arktekk.cms.{Logger => CmsLogger, _}
import no.arktekk.cms.CmsUtil._
import no.arktekk.cms.atompub._
import no.java.jzportal.twitter._
import org.joda.time.Minutes._
import scala.util.control._
import scala.xml._
import unfiltered.filter._
import unfiltered.response._

import org.slf4j._
import org.constretto._
import org.constretto.Converter._
import org.apache.commons.io.IOUtils
import unfiltered.request._

// TODO: Wrap an unfiltered kit around this plan that prevent all methods except GET (and possibly HEAD)
class JzPortalPlan extends Plan {
  val pageSize = Positive.fromInt(10)

  var logger: Logger = null
  var cmsClient: CmsClient = null
  var twitterClient: TwitterSearch = null

  import html._

  def intent = {
    newsIntent.orElse(pagesIntent.orElse(fallbackIntent))
  }

  def dumpEntry(indent: Int)(entry: CmsEntry): String = {
    ("".padTo(indent, ' ') + "Title=" + entry.title + ", slug=" + entry.slug + ", id=" + entry.id + ", categories=" + entry.categories) + "\n" +
    cmsClient.fetchChildrenOf(entry.slug).map(_.map(dumpEntry(indent + 1))).map(_.mkString("\n")).getOrElse("No children")
  }

  def fallbackIntent = Intent {
    case Path(Seg(Nil)) =>
      Redirect("/news.html")

    case Path(Seg("dump" :: Nil)) =>
      val lines = cmsClient.fetchTopPages().map(dumpEntry(0))
      Ok ~> PlainTextContent ~> unfiltered.response.ResponseString(lines.mkString("\n") + "\n")

    case Path(Seg("flush" :: Nil)) =>
      cmsClient.flushCaches()
      Redirect("/news.html")

    case Path(Seg("favicon.ico" :: Nil)) =>
      Redirect("http://java.no/favicon.ico")

    case req & Path(p) =>
      Option(classOf[JzPortalPlan].getClassLoader.getResourceAsStream("webapp" + p)) match {
        case Some(inputStream) =>
          logger.info("Found resource: /webapp" + p)
          val bytes = IOUtils.toByteArray(inputStream)
          Ok ~> PathBasedContentTypeResponder(p) ~> ResponseBytes(bytes)
        case None =>
          logger.info("Not found: /webapp" + p)
          val topPages = cmsClient.fetchTopPages()
          val tweets = twitterClient.currentResults
          NotFound ~> Html5(notFound(topPages, tweets, p))
      }
  }

  case class HeadersFromEntry(entry: CmsEntry) extends Responder[Any] {
//    def toString(dateTime: DateTime) = {
//    }

    def respond(res: HttpResponse[Any]) {
      res.header("Cache-Control", "public, must-revalidate")

//      entry.updatedOrPublished.foreach(dateTime => res.header("Last-Modified", toString(dateTime)))
    }
  }

  def newsIntent = Intent {
    case Path(Seg("news.html" :: Nil)) & Params(params) =>
      val start = params.get("start").flatMap(_.headOption)
      Ok ~> Html5(renderNewsList(start))

    case Path(Seg("news" :: slug :: Nil)) & Path(p) =>
      val s = slug.replaceFirst("\\.html$", "")
      renderNewsItem(s) match {
        case Some((entry, html)) =>
          Ok ~> HeadersFromEntry(entry) ~> Html5(html)
        case None =>
          val topPages = cmsClient.fetchTopPages()
          val tweets = twitterClient.currentResults
          NotFound ~> Html5(notFound(topPages, tweets, p))
      }
  }

  def pagesIntent = Intent {
    case Page(page) & Path(path) =>
      Ok ~> Html5(renderPage(page))
  }

  def renderNewsList(start: Option[String]) = {
    val offset = start.flatMap(parseInt).getOrElse(0)

    val response = cmsClient.fetchEntriesForCategory("news", offset, pageSize)
    val topPages = cmsClient.fetchTopPages()
    val tweets = twitterClient.currentResults

    news(topPages, tweets, response)
  }

  def renderNewsItem(slug: String): Option[(CmsEntry, NodeSeq)] = for {
    post <- cmsClient.fetchPostBySlug(CmsSlug.fromString(slug))
    val topPages = cmsClient.fetchTopPages()
    val tweets = twitterClient.currentResults
  } yield (post, news(topPages, tweets, post))

  def renderPage(p: CmsEntry) = {
    val siblings = for {
      parent <- cmsClient.fetchParentOfPageBySlug(p.slug)
      (prev, item, next) <- cmsClient.fetchSiblingsOf(p.slug)
    } yield (parent, prev, item, next)

    val topPages = cmsClient.fetchTopPages()
    val children = cmsClient.fetchChildrenOf(p.slug)
    val tweets = twitterClient.currentResults
    page(topPages, tweets, p, children, siblings)
  }

  object Page {
    def unapply[T](req: HttpRequest[T]): Option[CmsEntry] = {
      val list = req.uri.split('?')(0).split("/").toList
      list match {
        case "" :: slug :: Nil =>
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

    this.cmsClient = cmsClient

    // Twitter search integration
    this.twitterClient = {
      val x: String = constretto("twitter.search")(stringConverter)
      println("twitter search = " + x)
      val searchUri = new URI(x)
      val logger = LoggerFactory.getLogger("twitter.client")
      val twitterClient = new TwitterClientActor(logger, minutes(1), searchUri)
      twitterClient ! TwitterClient.Update
      twitterClient.start()
      logger.info("Starting twitter search: " + searchUri)
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
