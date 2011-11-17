package no.java.jzportal

import unfiltered.response._

case class PathBasedContentTypeResponder(path: String) extends Responder[Any] {
  def respond(res: HttpResponse[Any]) {
    val contentType = path.replaceAll(".*\\.([a-z]*)$", "$1") match {
      case "js" => "text/javascript"
      case "html" => "text/html"
      case "css" => "text/css"
      case "jpg" => "image/jpeg"
      case "jpeg" => "image/jpeg"
      case "gif" => "image/gif"
      case "png" => "image/png"
      case _ => "application/octet-stream"
    }
    res.header("Content-Type", contentType)
  }
}
