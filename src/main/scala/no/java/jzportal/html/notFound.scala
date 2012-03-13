package no.java.jzportal.html

object notFound {
  def apply(default: default, path: String) = default(html(path))
  
  def html(path: String) =
    <div class="body hyphenate">
      Boo, we couldn't find your page "{path}", sorry mate!
    </div>
}
