package no.java.jzportal.banner

import java.net.URL
import org.joda.time.Minutes
import org.slf4j.Logger
import actors.{TIMEOUT, Actor}
import no.arktekk.cms.{CmsSlug, CmsClient}

case class BannerImage(url: URL, title: String, alt: String)

class BannerActor(val cmsClient: CmsClient, val logger: Logger, val timeout: Minutes) extends Actor {
  def act() {
    loop {
      reactWithin(timeout.getMinutes * 60 * 1000) {
        case TIMEOUT =>
          update()
        case BannerActor.Update =>
          update()
        case _ =>
          logger.warn("Unknown message")
      }
    }
  }

  def update() {
  }
}

object BannerActor {
  object Update  
}
