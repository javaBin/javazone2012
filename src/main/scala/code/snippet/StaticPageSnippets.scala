package code.snippet

//import net.liftweb.common._
//import net.liftweb.http.S
//import net.liftweb.sitemap.Loc._
import no.arktekk.cms.{CmsClient, CmsSlug}
import scala.xml._

class StaticPageSnippets(cmsClient: CmsClient) /*extends DispatchLocSnippets with Logger*/ {
  /*
  def dispatch: PartialFunction[String, NodeSeq => NodeSeq] = {
    case "page" => _ =>
      (for {
        slug <- S.param("slug").map(CmsSlug.fromString)
        entry <- Box(cmsClient.fetchPageBySlug(slug))
      } yield {
        <xml:group>
          <h1>{entry.title}</h1>
          <div>{entry.content}</div>
        </xml:group>
      }).openOr(NodeSeq.Empty)
    case "siblings" => _ =>
      (for {
        slug <- S.param("slug").map(CmsSlug.fromString)
        parent <- cmsClient.fetchParentOfPageBySlug(slug)
        (prev, item, next) <- cmsClient.fetchSiblingsOf(slug)
      } yield {
        <xml:group>
          <div id="submenu">
            <span>{parent.title}:</span>
            <ul>
              {prev.map(entry => JavaZonePagesSnippet.pageToLi(entry))}
              {JavaZonePagesSnippet.pageToLi(item)}
              {next.map(entry => JavaZonePagesSnippet.pageToLi(entry))}
            </ul>
          </div>
        </xml:group>
      }).openOr(NodeSeq.Empty)
    case "children" => _ =>
      (for {
        slug <- S.param("slug").map(CmsSlug.fromString)
        list <- cmsClient.fetchChildrenOf(slug) if !list.isEmpty
      } yield {
        <xml:group>
          <div id="submenu">
            <span>Content:</span>
            <ul>
              {list.map(entry => JavaZonePagesSnippet.pageToLi(entry))}
            </ul>
          </div>
        </xml:group>
      }).openOr(NodeSeq.Empty)
  }
  */
}
