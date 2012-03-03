package no.java.jzportal.html

import xml.NodeSeq
import no.java.jzportal.html.Snippets._
import no.arktekk.cms.{OpenSearchResponse, CmsEntry}
import no.java.jzportal.twitter.JzTweet

object news {
  def apply(topPages: List[CmsEntry], tweets: List[JzTweet], response: OpenSearchResponse) = default(topPages, tweets,
    <div class="news">
      {
        val pages = response.page.map(entryToHtml)
        pages.foldLeft(NodeSeq.Empty)((news, nodes) => nodes ++ news)
      }
      <div class="morelink">
        {readMoreLink(response, response.index)}
      </div>
    </div>
  )

  def apply(topPages: List[CmsEntry], tweets: List[JzTweet], newsEntry: CmsEntry) = default(topPages, tweets,
    <div class="news">
      {entryToHtml(newsEntry)}
    </div>
  )
}
