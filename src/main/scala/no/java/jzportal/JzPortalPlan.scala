package no.java.jzportal

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.util._
import javazone2011._
import org.joda.time.Minutes._
import unfiltered.filter._
import unfiltered.request._
import unfiltered.response._
import scala.util._
import no.arktekk.push._
import no.arktekk.cms.{Logger => CmsLogger, _}
import java.io._
import java.net._

class JzPortalPlan extends Plan {
  def intent = {
    case Path(Seg(p :: Nil)) => ResponseString(p)
  }
}

object JzPortalPlan {
  def create() = {
    CmsUtil.skipEhcacheUpdateCheck

    // PubsubhubbubSubscriber
    val subscriptionUrl = new URL("http://localhost:8080/pubsubhubbub")
    val pubsubhubbub: PubsubhubbubSubscriber = new DefaultPubsubhubbubSubscriber(new Random(), subscriptionUrl)

    def hubCallback(hub: URL, topic: URL) {
      pubsubhubbub.addTopicToHub(hub, topic)
    }

    // CMS Integration
    val cmsLogger = new CmsLogger {
      private val logger = net.liftweb.common.Logger("CMS")

      def info(message: String) { logger.info(message) }

      def warn(message: String) { logger.warn(message) }
    }

    val cmsClient = CmsClient(cmsLogger, "CMS", new File(System.getProperty("user.home"), ".cms"), hubCallback)
    LiftRules.unloadHooks.append({ () => cmsClient.close() })

    // Twitter search integration
    println("Props.get(twitter.search)=" + Props.get("twitter.search"))
    val searchUri = new URI(Props.get("twitter.search").open_!)
    val logger = Logger("twitter.client")
    val twitterClient = new TwitterClientActor(logger, minutes(1), searchUri)
    twitterClient ! TwitterClient.Update
    twitterClient.start()
    logger.info("Starting twitter search: " + searchUri)

    new JzPortalPlan()
  }
}
