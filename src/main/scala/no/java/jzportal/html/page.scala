package no.java.jzportal.html

import no.arktekk.cms._
import scala.xml._

object page {
  import Snippets._

  def apply(default: default, page: CmsEntry, children: Option[List[CmsEntry]], siblings: Option[(CmsEntry, List[CmsEntry], CmsEntry, List[CmsEntry])]) = default(
    <div class="body bigbody hyphenate">
      {siblings.map(siblings_(_)).getOrElse(NodeSeq.Empty)}
      {children.filter(!_.isEmpty).map(children_(_)).getOrElse(NodeSeq.Empty)}
      {page.content}
    </div>
  )

  def siblings_(siblings: (CmsEntry, List[CmsEntry], CmsEntry, List[CmsEntry])) = siblings match { case (parent, prev, item, next ) =>
    <div id="submenu" class="donthyphenate">
      <ul>
        {prev.map(entry => pageToLi(entry))}
        {pageToLi(item)}
        {next.map(entry => pageToLi(entry))}
      </ul>
    </div>
  }

  def children_(children: List[CmsEntry]) = 
    <div id="submenu" class="donthyphenate">
      <ul>
        {children.map(entry => pageToLi(entry))}
      </ul>
    </div>
}
