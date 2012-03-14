package no.java.jzportal.html

import xml.NodeSeq
import no.arktekk.cms.CmsEntry

class default(val contextPath: String, val topPages: List[CmsEntry], val aboutJavaZone: NodeSeq, val aboutJavaBin: NodeSeq) {
  def apply(content: NodeSeq) =
    <html xmlns="http://www.w3.org/1999/xhtml" lang="en" class="no-js">
    <head>
      <title>JavaZone 2012</title>
      <meta name = "viewport" content = "initial-scale = 0.6"/>
      <link rel="shortcut icon" type="image/x-icon" href={contextPath + "/img/favicon.ico"}/>
      <link rel="stylesheet" href={contextPath + "/css/style.css"}/>
      <link rel="stylesheet" href={contextPath + "/css/jz.css"}/>
      <script type="text/javascript" src="http://use.typekit.com/cti1mmi.js"></script>
      <script type="text/javascript">{" try{Typekit.load();}catch(e){} "}</script>
      <!-- <link href="http://jz11.java.no/atom.xml" title="JavaZone nyheter" type="application/atom+xml" rel="alternate"/> -->
      <!--[if IE]><script type="text/javascript" src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
    </head>
    <body lang="en">

    <div id="line" class="min">
      <a href="#">12-13 Sept 2012</a>
      <a class="last">Oslo Spektrum, Norway</a>
    </div>

    <a href={contextPath + "/news"} class="med" id="splash"></a>
    <a href={contextPath + "/news"} class="max" id="sidesplash"></a>
    <a href={contextPath + "/news"} class="min" id="minsplash"></a>
    <div class="max" id="side"></div>


    <a class="max" href={contextPath}>
      <img src={contextPath + "/img/longtext.png"} id="longtext" />
    </a>

    <a class="med" href={contextPath}>
      <img src={contextPath + "/img/text.png"} id="text-horizontal" />
    </a>

    <a href={contextPath}>
      <img src={contextPath + "/img/splashlogo.png"} id="splashlogo" />
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
        <a href="http://twitter.com/#!/javazone"><img src={contextPath + "/img/social/twitter.png"}/></a>
        <a href="http://www.facebook.com/javabin"><img src={contextPath + "/img/social/facebook.png"}/></a>
        <a href="http://www.youtube.com/user/JavaZoneNo"><img src={contextPath + "/img/social/youtube.png"}/></a>
        <a href="http://vimeo.com/javazone/videos"><img src={contextPath + "/img/social/vimeo.png"}/></a>
      </p>
      <p class="text">JavaZone.no - shipped in cooperation with <a href="http://accenture.no" target="_blank">Accenture Creative Services</a></p>
      <p class="text">All images and text are Creative Commons BY-SA 2.0 Licensed</p>
    </div>

    </div></div></div>
    <div class="clear"></div>
    <script src={contextPath + "/js/libs/jquery-1.6.2.min.js"}></script>
    <script src={contextPath + "/js/libs/jquery.color.js"}></script>
    <script src={contextPath + "/js/libs/respond.min.js"}></script>
    <script src={contextPath + "/js/hyphenator.js"}></script>
    <script src={contextPath + "/js/script.js"}></script>
    <script type="text/javascript">{"""
      var _gaq = _gaq || [];
      _gaq.push(['_setAccount', 'UA-3676724-5']);
      _gaq.push(['_trackPageview']);

      (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
      })();
  """}</script>
    </body>
    </html>

  def menu(topPages: List[CmsEntry]): NodeSeq = {
    implicit def f(o: Option[CmsEntry]): NodeSeq = o match {
      case None => <span>&nbsp;</span>
      case Some(entry) => <a href={contextPath + "/" + entry.slug.toString()}>{entry.title}</a>
    }

    <div id="menu">
      <div id="menuelement-0"><a href={contextPath + "/news"}>News</a></div>
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
          <li class="prev"><img src={contextPath + "/images/banner/news/1.jpg"} alt=""/></li>
          <li class="curr"><img src={contextPath + "/images/banner/news/2.jpg"} alt=""/></li>
          <li class="next"><img src={contextPath + "/images/banner/news/3.jpg"} alt=""/></li>
        </ol>
      </div>
      <div id="prev" class="btn-nav"><img src={contextPath + "/images/banner/nav-left.png"} alt=""/></div>
      <div id="next" class="btn-nav"><img src={contextPath + "/images/banner/nav-right.png"} alt=""/></div>
      <img class="message" src={contextPath + "/images/banner/news/message.png"} alt="JavaZone 2011"/>
    </div>
}
