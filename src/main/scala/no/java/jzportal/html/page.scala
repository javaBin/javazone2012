package no.java.jzportal.html

import no.arktekk.cms._
import scala.xml._
import scalaz.NonEmptyList

object page {
  def apply(default: default, path: NonEmptyList[CmsSlug], page: CmsEntry, children: Option[Seq[CmsEntry]], siblings: Option[(List[CmsEntry], CmsEntry, List[CmsEntry])]) = {
    val siblingsCp = default.contextPath + "/" + path.list.dropRight(1).mkString("/") + "/"
    val childrenCp = default.contextPath + "/" + path.list.mkString("/") + "/"

    default(
      <div class="body bigbody hyphenate">
        {siblings.map{case (prev, _, next) => siblings_(siblingsCp, page, (prev, next))}.getOrElse(NodeSeq.Empty)}
        {children.map{children_(childrenCp, path.list, _)}.getOrElse(NodeSeq.Empty)}
        {page.content}
      </div>
    )
  }

  def siblings_(contextPath: String, page: CmsEntry, siblings: (List[CmsEntry], List[CmsEntry])) = siblings match { case (prev, next) =>
    <div id="submenu" class="donthyphenate">
      <!-- siblings -->
      <ul>
        {prev.map(entry => <li>{<a href={contextPath + entry.slug}>{entry.title}</a>}</li>)}
        {<li>{<a href={contextPath + page.slug}>{page.title}</a>}</li>}
        {next.map(entry => <li>{<a href={contextPath + entry.slug}>{entry.title}</a>}</li>)}
      </ul>
    </div>
  }

  def children_(contextPath: String, path: Seq[CmsSlug], children: Seq[CmsEntry]) = {
    <div id="submenu" class="donthyphenate">
      <!-- children -->
      <ul>
        {children.map(entry => <li>{<a href={contextPath + entry.slug}>{entry.title}</a>}</li>)}
      </ul>
    </div>
  }
}
