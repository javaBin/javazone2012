package no.java.jzportal.html

import org.joda.time.format.{DateTimeFormatterBuilder, DateTimeFormatter}
import no.arktekk.cms.{OpenSearchResponse, CmsEntry}
import scala.xml._
import javazone2011.JzTweet

object HtmlTemplates {

  val postDateTimeFormatter: DateTimeFormatter = new DateTimeFormatterBuilder().
    appendDayOfMonth(2).
    appendLiteral(' ').
    appendMonthOfYearText().
    appendLiteral(' ').
    appendYear(4, 4).
    toFormatter

  // Snippets

  def pageToLi(page: CmsEntry) = <li>{pageToA(page)}</li>

  def pageToA(page: CmsEntry) = <a href={"/" + page.slug + ".html"}>{page.title}</a>

  def readMoreLink(response: OpenSearchResponse, offset: Int): NodeSeq = {
    val prev = response.prevStart.map(i => <a href={"/news.html?start=" + i}>Prev</a>)
    val next = response.nextStart.map(i => <a href={"/news.html?start=" + i}>Next</a>)

    prev.getOrElse(Text("Prev")) ++ Text(" - ") ++ next.getOrElse(Text("Next"))
  }

  def entryToHtml(entry: CmsEntry) =
    <div class="newsframe">
      {entry.updatedOrPublished.map(date => <span class="timestamp">{postDateTimeFormatter.print(date)}</span>).getOrElse(NodeSeq.Empty)}
      <div class="expand_newsframe">
          <a href={ "/news/" + entry.slug + ".html" }>
            <img src="/images/expand.png" alt="co" title="Open"/>
          </a>
      </div>
      <h2>{entry.title}</h2>
      <div>{entry.content}</div>
    </div>

  def tweetToLi(tweet: JzTweet): NodeSeq =
    <li>
      <span class="handle"><a href={tweet.handleUrl.toExternalForm}>{tweet.handle}</a></span>:
      <span class="text">{tweet.text}</span><br/>
      <span class="meta">{tweet.timeAgo} &#183; <a class="tweet_link" href={tweet.htmlLink.toString}>View Tweet</a></span>
    </li>

  // Page and Page Section Templates

  def banner =
    <div id="banner">
      <div id="banner-content">
        <ol class="images">
          <li class="prev">
            <img src="/images/banner/news/1.jpg" alt=""/>
          </li>
          <li class="curr">
            <img src="/images/banner/news/2.jpg" alt=""/>
          </li>
          <li class="next">
            <img src="/images/banner/news/3.jpg" alt=""/>
          </li>
        </ol>
      </div>
      <div id="prev" class="btn-nav"><img src="/images/banner/nav-left.png" alt=""/></div>
      <div id="next" class="btn-nav"><img src="/images/banner/nav-right.png" alt=""/></div>
      <img class="message" src="/images/banner/news/message.png" alt="JavaZone 2011"/>
    </div>

  def sidebar =
    <div id="sidebar">
      <h2>
        <a href="http://twitter.com/javazone">#javazone</a>
        <span class="follow">Follow&nbsp;us&nbsp;on&nbsp;<a href="http://www.facebook.com/JavaZoneConference">Facebook</a>&nbsp;and&nbsp;<a href="http://twitter.com/javazone">Twitter</a></span>
      </h2>
      <div id="twitter" class="twitted">
        <!--
          <lift:jz.tweets/>
          <lift:jz.moreTweets/>
        -->
        TODO: Tweets
      </div>
    </div>

  def default(topPages: => NodeSeq, content: => NodeSeq) =
  <xml:group>
    <html xmlns="http://www.w3.org/1999/xhtml">
    <head>
      <title>JavaZone 2011</title>
      <link rel="stylesheet" href="/css/jz11.css" media="all"/>
      <link rel="stylesheet" href="/css/jz11-desktop.css" media="all"/>
      <link rel="stylesheet" href="/css/jz11-mobile.css" media="only screen and (max-width: 980px), handheld"/>
      <link rel="stylesheet" href="/css/jquery.lightbox-0.5.css" media="all"/>
      <meta name="viewport" content="width=960"/>
      <link href="http://jz11.java.no/atom.xml" title="JavaZone nyheter" type="application/atom+xml" rel="alternate"/>
      <base/>
      <!--[if IE]><script type="text/javascript" src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
    </head>
    <body>
    <div id="header" class="center">
      <div id="header-content">
        <a class="logo" title="Javazone - Forsiden" href="/"><img src="/images/jz11_logo.png" alt="Javazone 2011" title="Javazone - Forsiden" class="logo"/></a>
        <div id="mainmenu">{topPages}</div>
      </div>
    </div>
    {banner}
    <div id="wrapper" class="clearfix">
      <div id="content">
        {content}
        {sidebar}
      </div>
    </div>
    <div id="footer-container">
      <div id="footer">
        <ul>
          <li>JavaZone.no - shipped in cooperation with <a href="http://www.bekk.no/"><img src="/images/footer/bekk.png" alt="BEKK"/></a></li>
          <li>Banner photos by <a href="http://www.fotogal.no/">Andre Eide</a></li>
          <li>All images and text are Creative Commons BY-SA 2.0 Licensed</li>
        </ul>
      </div>
    </div>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.0/jquery.min.js"></script>
    <script type="text/javascript" src="http://jz10.java.no/lib/jquery/jquery.twitter.js"></script>
    <script type="text/javascript" src="/js/jquery.lightbox-0.5.min.js"></script>
    <script type="text/javascript" src="/js/jquery.bgiframe.js"></script>
    <script type="text/javascript" src="/js/jquery.dimensions.js"></script>
    <script type="text/javascript" src="/js/jquery.tooltip.pack.js"></script>
    <script type="text/javascript" src="/js/jz11.js"></script>
    </body>
    </html>
  </xml:group>

  def news(topPages: => NodeSeq, newsList: => List[NodeSeq], readMore: => NodeSeq) = default(topPages,
    <xml:group>
      <h1>News</h1>
      {newsList.foldLeft(NodeSeq.Empty)((news, nodes) => nodes ++ news)}
      {readMore}
      </xml:group>
  )
}