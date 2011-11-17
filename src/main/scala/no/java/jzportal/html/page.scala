package no.java.jzportal.html

import no.arktekk.cms._

object page {
  def apply(topPages: => List[CmsEntry], page: => CmsEntry) = default(topPages,
    <div id="main">
      TODO: Children
      TODO: Siblings
        <h1>{page.title}</h1>
        <div>{page.content}</div>
    </div>
  )
}
