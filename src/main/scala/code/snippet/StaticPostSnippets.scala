package code.snippet

//import net.liftweb.sitemap.Loc.DispatchLocSnippets
//import net.liftweb.http.S
import xml.NodeSeq
import no.arktekk.cms.{CmsSlug, CmsClient}

class StaticPostSnippets(cmsClient: CmsClient) /*extends DispatchLocSnippets*/ {
  /*
  def dispatch: PartialFunction[String, NodeSeq => NodeSeq] = {
    case "page" => _ =>
      S.param("slug").map {
        slug =>
          cmsClient.fetchPostBySlug(CmsSlug.fromString(slug)).map {
            entry =>
              <xml:group>
                <h2>{entry.title}</h2>
                <div>{entry.content}</div>
              </xml:group>
          }
      }.openOr(None).getOrElse(NodeSeq.Empty)
  }
  */
}
