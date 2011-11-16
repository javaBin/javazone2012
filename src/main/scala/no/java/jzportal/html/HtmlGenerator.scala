package no.java.jzportal.html

import no.arktekk.cms._
import javazone2011._
import scala.xml._

object HtmlGenerator {
  import HtmlTemplates._

  def topPages(cmsClient: CmsClient): NodeSeq =
    <ul>
      <li>
        <a href="/news.html">News</a>
      </li>
      {NodeSeq.fromSeq(cmsClient.fetchTopPages().map(pageToLi))}
    </ul>

  def tweets(twitterSearch: TwitterSearch): NodeSeq =
    <ul id="twitter_update_list">
      {twitterSearch.currentResults.map(tweetToLi(_))}
    </ul>

  def moreTweets(twitterSearch: TwitterSearch): NodeSeq =
    twitterSearch.searchUrlHtml.map(url => <a href={url.toExternalForm}>More...</a>).getOrElse(NodeSeq.Empty)
}
