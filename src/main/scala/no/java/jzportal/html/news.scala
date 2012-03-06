package no.java.jzportal.html

import xml.NodeSeq
import java.net.URL
import no.arktekk.cms.{OpenSearchResponse, CmsEntry}
import no.java.jzportal.html.Snippets._
import no.java.jzportal.twitter.JzTweet

object news {
  def apply(default: default, twitterSearchHtmlUrl: URL, tweets: List[JzTweet], response: OpenSearchResponse) = default(
    <div class="body hyphenate">
      <div class="news">
        {
          val pages = response.page.map(entryToHtml)
          pages.foldLeft(NodeSeq.Empty)((news, nodes) => nodes ++ news)
        }
        <div class="morelink">
          {readMoreLink(response, response.index)}
        </div>
      </div>
    </div>
    <div class="side tweets">
      {sidebar(twitterSearchHtmlUrl, tweets)}
    </div>    
  )

  def apply(default: default, newsEntry: CmsEntry) = default(
    <div class="news">
      {entryToHtml(newsEntry)}
    </div>
  )

  def sidebar(twitterSearchHtmlUrl: URL, tweets: List[JzTweet]) =
    <h2>
      <a href={twitterSearchHtmlUrl.toString}>#javazone</a>
    </h2> 
    <div id="twitter" class="twitted">
      {tweets.map(Snippets.tweetToDiv)}
    </div>

}
