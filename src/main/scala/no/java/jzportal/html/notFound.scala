package no.java.jzportal.html

import no.arktekk.cms.CmsEntry
import no.java.jzportal.twitter.JzTweet

object notFound {
  def apply(topPages: List[CmsEntry], tweets: List[JzTweet], path: String) = default(topPages, tweets, html(path))
  
  def html(path: String) =
    <div>
      Boo, we couldn't find your page "{path}", sorry mate!
    </div>
}
