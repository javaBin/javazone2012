package no.java.jzportal.html

import no.java.jzportal.twitter._
import scala.xml._

object HtmlGenerator {
  import Snippets._

  def tweets(twitterSearch: TwitterSearch): NodeSeq =
    <ul id="twitter_update_list">
      {twitterSearch.currentResults.map(tweetToDiv(_))}
    </ul>

  def moreTweets(twitterSearch: TwitterSearch): NodeSeq =
    twitterSearch.searchUrlHtml.map(url => <a href={url.toExternalForm}>More...</a>).getOrElse(NodeSeq.Empty)
}
