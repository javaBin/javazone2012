package no.java.jzportal.html

import no.arktekk.cms._
import no.java.jzportal.twitter.JzTweet
import org.joda.time.format._
import scala.xml._

object Snippets {

  val postDateTimeFormatter: DateTimeFormatter = new DateTimeFormatterBuilder().
    appendDayOfMonth(2).
    appendLiteral(' ').
    appendMonthOfYearText().
    appendLiteral(' ').
    appendYear(4, 4).
    toFormatter

  def pageToLi(page: CmsEntry) = <li>{pageToA(page)}</li>

  def pageToA(page: CmsEntry) = <a href={"/" + page.slug + ".html"}>{page.title}</a>

  def readMoreLink(response: OpenSearchResponse, offset: Int): NodeSeq = {
    val prev = response.prevStart.map(i => <a href={"/news.html?start=" + i}>Prev</a>)
    val next = response.nextStart.map(i => <a href={"/news.html?start=" + i}>Next</a>)

    prev.getOrElse(Text("Prev")) ++ Text(" - ") ++ next.getOrElse(Text("Next"))
  }

  def entryToHtml(entry: CmsEntry) =
    <div class="news">
      <h3><a href={ "/news/" + entry.slug + ".html" }>{entry.title}</a></h3>
      {entry.content}
      <p>{entry.updatedOrPublished.map(date => <span class="timestamp">{postDateTimeFormatter.print(date)}</span>).getOrElse(NodeSeq.Empty)}</p>
    </div>

  def tweetToDiv(tweet: JzTweet): NodeSeq =
    <div class="tweet">
      <p>{tweet.text} <a href={tweet.handleUrl.toExternalForm}>{tweet.handle}</a></p>
      <!--<span class="meta">{tweet.timeAgo} &#183; <a class="tweet_link" href={tweet.htmlLink.toString}>View Tweet</a></span>-->
    </div>
}