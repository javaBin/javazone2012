package no.java.jzportal.html

import xml.NodeSeq
import no.java.jzportal.html.Snippets._
import no.arktekk.cms.{OpenSearchResponse, CmsEntry}

object news {
  def apply(topPages: => List[CmsEntry], response: OpenSearchResponse) = default(topPages,
    <div id="main">
      <h1>News</h1>
      {
        val pages = response.page.map(entryToHtml)
        pages.foldLeft(NodeSeq.Empty)((news, nodes) => nodes ++ news)
      }
      {readMoreLink(response, response.index)}
    </div>
  )

  def apply(topPages: => List[CmsEntry], newsEntry: => CmsEntry) = default(topPages,
    <div id="main">
      <h1>News</h1>
      {entryToHtml(newsEntry)}
    </div>
  )
}
