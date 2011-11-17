package no.java.jzportal.html

import no.arktekk.cms.CmsEntry

object notFound {
  def apply(topPages: => List[CmsEntry], path: String) = default(topPages, html(path))
  
  def html(path: String) =
    <div>
      Boo, we couldn't find your page "{path}", sorry mate!
    </div>
}
