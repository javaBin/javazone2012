package no.java.jzportal.html

import no.arktekk.cms._
import scala.xml._
import no.java.jzportal.twitter.JzTweet

object page {
  import Snippets._

  def apply(topPages: List[CmsEntry], tweets: List[JzTweet], page: CmsEntry, children: Option[List[CmsEntry]], siblings: Option[(CmsEntry, List[CmsEntry], CmsEntry, List[CmsEntry])]) = default(topPages, tweets,
    <div class="page">
      {siblings.map(siblings_(_)).getOrElse(NodeSeq.Empty)}
      {children.filter(!_.isEmpty).map(children_(_)).getOrElse(NodeSeq.Empty)}
      <h2>{page.title}</h2>
      <div>{page.content}</div>
    </div>
  )

  def siblings_(siblings: (CmsEntry, List[CmsEntry], CmsEntry, List[CmsEntry])) = siblings match { case (parent, prev, item, next ) =>
    <div id="submenu">
      <span>{parent.title}:</span>
      <ul>
        {prev.map(entry => pageToLi(entry))}
        {pageToLi(item)}
        {next.map(entry => pageToLi(entry))}
      </ul>
    </div>
  }

  def children_(children: List[CmsEntry]) = 
    <div id="submenu">
      <span>Content:</span>
      <ul>
        {children.map(entry => pageToLi(entry))}
      </ul>
    </div>
}
