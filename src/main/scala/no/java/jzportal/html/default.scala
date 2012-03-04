package no.java.jzportal.html

import xml.NodeSeq
import no.arktekk.cms.CmsEntry

class default(val topPages: List[CmsEntry], val aboutJavaZone: NodeSeq, val aboutJavaBin: NodeSeq) {
  def apply(content: NodeSeq) =
    <html xmlns="http://www.w3.org/1999/xhtml" lang="en">
    <head>
      <title>JavaZone 2012</title>
      <meta name = "viewport" content = "initial-scale = 0.6, user-scalable = no" /> 
      <link rel="shortcut icon" type="image/x-icon" href="/img/favicon.ico" />
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
      <img src="/img/longtext.png" id="longtext" />
    </a>

    <a class="max" href="/">
      <img src="/img/longtext-vertical.png" class="hide" id="longtext-vertical" />
    </a>

    <a class="med" href="/">
      <img src="/img/text.png" id="text-horizontal" />
    </a>

    <a href="/">
      <img src="/img/splashlogo.png" id="splashlogo" />
    </a>

    <div id="c1"><div id="c2"><div id="c4">

    {menu(topPages)}

    <div id="main">
      
      {content}

      <div class="side side2">
        {aboutJavaZone}
        <div class="spacer"></div>
        {aboutJavaBin}
      </div>
      <div class="clear"></div>
    </div>

    <div id="footer">
      <p>
      <a href="http://twitter.com/#!/javazone"><img src="/img/social/twitter.png" /></a>
      <a href="http://www.facebook.com/javabin"><img src="/img/social/facebook.png" /></a>
      <a href="http://www.youtube.com/user/JavaZoneNo"><img src="/img/social/youtube.png" /></a>
      <a href="http://vimeo.com/javazone/videos"><img src="/img/social/vimeo.png" /></a>
      </p>
      <p class="text">Designed by <a href="#">Accenture Creative Services</a></p>
    </div>

    </div></div></div>
    <div class="clear"></div>
    <script src="/js/libs/jquery-1.6.2.min.js"></script>
    <script src="/js/libs/jquery.color.js"></script>
    <script src="/js/libs/respond.min.js"></script>
    <script src="/js/hyphenator.js"></script>
    <script src="/js/script.js"></script>
    </body>
    </html>

  def menu(topPages: List[CmsEntry]): NodeSeq = {
    implicit def f(o: Option[CmsEntry]): NodeSeq = o match {
      case None => <span>&nbsp;</span>
      case Some(entry) => <a href={entry.slug.toString() + ".html"}>{entry.title}</a>
    }

    <div id="menu">
      <div id="menuelement-0"><a href="/news.html">News</a></div>
      <div id="menuelement-1" class="high">{topPages.lift(0).flatten}</div>
      <div id="menuelement-2" class="high">{topPages.lift(1).flatten}</div>
      <div id="menuelement-3">{topPages.lift(2).flatten}</div>
      <div id="menuelement-4" class="high">{topPages.lift(3).flatten}</div>
      <div id="menuelement-5">{topPages.lift(4).flatten}</div>
      <div id="menuelement-6">{topPages.lift(5).flatten}</div>
      <div id="menuelement-7" class="high last">{topPages.lift(6).flatten}</div>
    </div>
  }

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
}
