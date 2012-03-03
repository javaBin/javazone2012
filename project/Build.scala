import sbt._
import sbt.Keys._
import sbtrelease._
import sbtrelease.Release._
import com.github.siasia.PluginKeys._
import com.github.siasia.WebPlugin

object JavaZonePortal extends Build {

  val logbackVersion = "0.9.18"
  val unfilteredVersion = "0.5.1"

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "no.java.javazone-portal",
    scalaVersion := "2.9.1"
  )

  lazy val webSettings = WebPlugin.webSettings ++ Seq(
    scanDirectories in Compile := Nil
  )

// Add back when we're deploying to oss.sonatype.com
//  lazy val credentialsSetting = credentials ++
//    (Seq("REPOSITORY_USER", "REPOSITORY_PASSWORD").map(k => Option(System.getenv(k))) match {
//      case Seq(Some(user), Some(pass)) =>
//        Seq(Credentials("javaBin", "smia.java.no", user, pass))
//      case _ =>
//        sys.error("foo") // Seq.empty[Credentials]
//    })
//    publishTo <<= (version) {
//      version: String =>
//        if (version.trim.endsWith("SNAPSHOT"))
//          Some("javaBin" at "http://smia.java.no/maven/dist/snapshots")
//        else
//          Some("javaBin" at "http://smia.java.no/maven/dist/releases")
//    },

  lazy val project = Project(
    id = "javazone-portal",
    base = file("."),
    settings = buildSettings ++
      releaseSettings ++
//      credentialsSetting ++
      webSettings ++
      Seq(
      description := "JavaZone 2012 Portal",
      resolvers += "sonatype snapshot" at "https://oss.sonatype.org/content/repositories/snapshots",
      ReleaseKeys.releaseProcess <<= thisProjectRef apply { ref =>
        import sbtrelease.ReleaseStateTransformations._
        Seq[ReleasePart](
          initialGitChecks,
          checkSnapshotDependencies,
          inquireVersions,
          runTest,
          setReleaseVersion,
          commitReleaseVersion,
          tagRelease,
        // Enable when we're deploying to Sonatype
  //        releaseTask(publish in Global in ref),
          setNextVersion,
          commitNextVersion
        )
      },
      libraryDependencies := Seq(
        "net.databinder" %% "unfiltered-filter" % unfilteredVersion,
        "net.databinder" %% "unfiltered-jetty" % unfilteredVersion % "test",

        "no.java" %% "atom2twitterpublisher" % "1.2-SNAPSHOT",
        "no.arktekk.atom-client" %% "atom-client-core" % "1.1-SNAPSHOT",

        "org.constretto" %% "constretto-scala" % "1.0",

        "log4j" % "log4j" % "1.2.16",
        "org.slf4j" % "slf4j-log4j12" % "1.6.1",

        "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default" withSources(),
        "junit" % "junit" % "4.5" % "test->default",
        "org.specs2" %% "specs2" % "1.6.1" % "test->default" withSources(),
        "com.h2database" % "h2" % "1.2.138",

        // For xsbt-web-plugin
        "org.mortbay.jetty" % "jetty" % "6.1.22" % "container"
      )
    )
  )
}
