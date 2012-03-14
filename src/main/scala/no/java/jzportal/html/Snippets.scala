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

  def splitSiblings(page: CmsSlug, siblings: Seq[CmsEntry]): (Seq[CmsEntry], Seq[CmsEntry]) = {
    (List.empty, List.empty)
  }

  def readMoreLink(response: OpenSearchResponse, offset: Int): NodeSeq = {
    val prev = response.prevStart.map(i => <a href={"/news?start=" + i}>Prev</a>)
    val next = response.nextStart.map(i => <a href={"/news?start=" + i}>Next</a>)

    prev.getOrElse(Text("Prev")) ++ Text(" - ") ++ next.getOrElse(Text("Next"))
  }

  def entryToHtml(entry: CmsEntry) =
    <div class="news">
      <h3>
        <a href={"/news/" + entry.slug}>
          {entry.title}
          <span>{entry.updatedOrPublished.map(date => <span class="timestamp">{postDateTimeFormatter.print(date)}</span>).getOrElse(NodeSeq.Empty)}</span>
        </a>
      </h3>
      {entry.content}
    </div>

  def tweetToDiv(tweet: JzTweet): NodeSeq =
    <div class="tweet">
      <p>{tweet.text} <a href={tweet.handleUrl.toExternalForm}>{tweet.handle}</a></p>
    </div>
}