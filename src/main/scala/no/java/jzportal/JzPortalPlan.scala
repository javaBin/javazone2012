package no.java.jzportal

import java.io._
import java.net._
import javax.servlet.FilterConfig
import javazone2011._
import no.arktekk.push._
import no.arktekk.cms.{Logger => CmsLogger, _}
import no.arktekk.cms.CmsUtil._
import org.joda.time.Minutes._
import scala.util._
import scala.util.control._
import scala.xml._
import unfiltered.filter._
import unfiltered.request._
import unfiltered.response._

import org.slf4j._
import org.constretto._
import org.constretto.ScalaValueConverter._

class JzPortalPlan extends Plan {
  val pageSize = Positive.fromInt(2)

  var logger: Logger = null
  var cmsClient: CmsClient = null
  var twitterClient: TwitterSearch = null

  import html.HtmlGenerator._
  import html.HtmlTemplates._

  def intent = {
    case Path(Seg(Nil)) => Redirect("/news.html")
    case Path(Seg("news.html" :: Nil)) & Params(params) =>
      Ok ~> Html(fetchNews(params.get("start").flatMap(_.headOption)))
    case Path(Seg(x :: Nil)) => Ok ~> ResponseString("x=" + x + "\n")
  }

  def fetchNews(start: Option[String]): NodeSeq = {
    val offset = start.flatMap(parseInt).getOrElse(0)

    val response = cmsClient.fetchEntriesForCategory("News", offset, pageSize)

    news(topPages(cmsClient), response.page.map(entryToHtml), readMoreLink(response, offset))
  }

  override def init(config: FilterConfig) {
    super.init(config)

    logger = org.slf4j.LoggerFactory.getLogger("JzPortal")

    CmsUtil.skipEhcacheUpdateCheck

    // PubsubhubbubSubscriber
    val subscriptionUrl = new URL("http://localhost:8080/pubsubhubbub")
    val pubsubhubbub: PubsubhubbubSubscriber = new DefaultPubsubhubbubSubscriber(new Random(), subscriptionUrl)

    def hubCallback(hub: URL, topic: URL) {
      pubsubhubbub.addTopicToHub(hub, topic)
    }

    // CMS Integration
    val cmsLogger = new CmsLogger {
      private val logger = org.slf4j.LoggerFactory.getLogger("CMS")

      def info(message: String) { logger.info(message) }

      def warn(message: String) { logger.warn(message) }
    }

    val cmsClient = CmsClient(cmsLogger, "CMS", new File(System.getProperty("user.home"), ".cms"), hubCallback)

    this.cmsClient = cmsClient

    // Twitter search integration
    this.twitterClient = {
      val constretto = Constretto(Seq(Constretto.properties("classpath:default.properties")))
  //    println("Props.get(twitter.search)=" + Props.get("twitter.search"))
      val x: String = constretto("twitter.search")(stringConverter)
      println(x)
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
}
