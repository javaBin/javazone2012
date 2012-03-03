package no.java.jzportal.html

import xml.NodeSeq
import no.arktekk.cms.CmsEntry
import no.java.jzportal.twitter.JzTweet

object default {
  def apply(topPages: List[CmsEntry], tweets: List[JzTweet], content: NodeSeq) =
    <html xmlns="http://www.w3.org/1999/xhtml" lang="en">
    <head>
      <title>JavaZone 2021</title>

      <meta name = "viewport" content = "initial-scale = 0.6, user-scalable = no" /> 
      <link rel="stylesheet" href="/css/style.css" />
      <link rel="stylesheet" href="/css/jz.css" />
      <link rel="stylesheet" href="/css/min.css" />
      <link rel="stylesheet" href="/css/med.css" />
      <link rel="stylesheet" href="/css/max.css" />
      <script type="text/javascript" src="http://use.typekit.com/vtw8zbt.js"></script>

      <script type="text/javascript">{" try{Typekit.load();}catch(e){} "}</script>

      <link href="http://jz11.java.no/atom.xml" title="JavaZone nyheter" type="application/atom+xml" rel="alternate"/>
      <base/>
      <!--[if IE]><script type="text/javascript" src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
    </head>
    <body lang="en">


    <div id="line" class="min">
      <a href="#">12-13 Sept 2012</a>
      <a class="last">Oslo Spektrum, Norway</a>
    </div>

    <a href="/" class="med" id="splash"></a>
    <a href="/" class="max" id="sidesplash"></a>
    <a href="/" class="min" id="minsplash"></a>
    <div class="max" id="side"></div>


    <a class="max" href="/">
      <img src="img/longtext.png" id="longtext" />
    </a>

    <a class="max" href="/">
      <img src="img/longtext-vertical.png" class="hide" id="longtext-vertical" />
    </a>

    <a class="med" href="/">
      <img src="img/text.png" id="text-horizontal" />
    </a>

    <a href="/">
      <img src="img/splashlogo.png" id="splashlogo" />
    </a>

    <div id="c1"><div id="c2"><div id="c4">

    <div id="menu">
      <div id="menuelement-0"><a href="/">Home</a></div>
      <div id="menuelement-1" class="high"><a href="/">Tickets</a></div>
      <div id="menuelement-2" class="high"><a href="/">Program</a></div>
      <div id="menuelement-3"><a href="/about.html">Info</a></div>
      <div id="menuelement-4" class="high middle"><a href="/">Partners</a></div>
      <div id="menuelement-5"><a href="/">Video</a></div>
      <div id="menuelement-6"><a href="/">Contact</a></div>
      <div id="menuelement-7" class="high last"><a href="/">Twitter</a></div>
    </div>

    <div id="main">

      <div id="submenu">
        <a href="#">Practicalities</a>
        <a href="#">Foobar</a>
        <a href="#">Something</a>
        <a href="#">Another</a>
        <a href="#">Coolbreeze</a>
      </div>
      
      <div class="body hyphenate">
        {content}
      </div>
      <div class="side tweets">
        {sidebar(tweets)}
      </div>

      <div class="side side2">
        <h2><a href="#">About JavaZone</a></h2>
       
        <p>JavaZone is an important meeting place for software developers in Scandinavia, and the biggest community driven conference of its kind. JavaZone has been described as a high quality, independent conference – a leading forum for knowledge exchange for IT-professionals.</p>

        <p>Over 30 large and mid-sized companies – our partners – contribute with speakers and exhibit their brand and services/products during the conference. Without partners, volunteers, the speakers and the participants, the conference would not be possible.</p>
        <div class="spacer"></div>
        <h2><a href="#">About javaBin</a></h2>

        <p>Over the two conference days, we deliver more than 200,000 hours of expertise. In addition, many informal discussions take place at stands and between attendees. Altogether, JavaZone stands out as a tremendous arena for knowledge transfer.</p>

        <p>A conference involving so many developers is also a key arena for recruitment. Through their exhibitions and other activities, our partners use the opportunity to promote their business and recruit new staff. JavaZone is more than just a conference. It’s a place to meet, network and socialize!</p>

      </div>
      <div class="clear"></div>
    </div>

    <div id="footer">
      <a href="/">Home</a>
      <a href="/">Tickets</a>
      <a href="/">Program</a>
      <a href="/about.html">About</a>
      <a href="/">Info</a>
      <a href="/">Video</a>
      <a href="/">Contact</a>
      <a href="/">Twitter</a>
    </div>

    </div></div></div>
    <div class="clear"></div>
    <script src="/js/libs/jquery-1.6.2.min.js"></script>
    <script src="/js/libs/jquery.color.js"></script>
    <script src="/js/libs/jquery.backgroundparallax.js"></script>
    <script src="/js/hyphenator.js"></script>
    <script src="/js/script.js"></script>
    </body>
    </html>

  def topPages(topPages: List[CmsEntry]): NodeSeq =
    <ul>
      <li>
        <a href="/news.html">News</a>
      </li>
      {NodeSeq.fromSeq(topPages.map(Snippets.pageToLi))}
    </ul>

  def banner =
    <div id="banner">
      <div id="banner-content">
        <ol class="images">
          <li class="prev"><img src="/images/banner/news/1.jpg" alt=""/></li>
          <li class="curr"><img src="/images/banner/news/2.jpg" alt=""/></li>
          <li class="next"><img src="/images/banner/news/3.jpg" alt=""/></li> 
        </ol>
      </div>
      <div id="prev" class="btn-nav"><img src="/images/banner/nav-left.png" alt=""/></div>
      <div id="next" class="btn-nav"><img src="/images/banner/nav-right.png" alt=""/></div>
      <img class="message" src="/images/banner/news/message.png" alt="JavaZone 2011"/>
    </div>

  def sidebar(tweets: List[JzTweet]) =
    <h2>
      <a href="http://twitter.com/javazone">#javazone</a>
    </h2> 
    <div id="twitter" class="twitted">
      {tweets.map(Snippets.tweetToDiv)}
    </div>
}
