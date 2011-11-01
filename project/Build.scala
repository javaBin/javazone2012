import sbt._
import sbtrelease.Release
import sbtrelease.Release._
import sbtrelease.ReleaseKeys
import sbtrelease.ReleasePart
//import sbtrelease._
import Keys._

object JavaZonePortal extends Build {

  val logbackVersion = "0.9.18"
  val abderaVersion = "1.1.2"
  val liftVersion = "2.4-M4"

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "no.java.javazone-portal",
    scalaVersion := "2.9.1"
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
      Seq(
      description := "JavaZone 2012 Portal",
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
        "net.liftweb" %% "lift-common" % liftVersion % "compile->default" withSources(),
        "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default" withSources(),
        "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default" withSources(),
        "net.liftweb" %% "lift-util" % liftVersion % "compile->default" withSources(),

        "no.javabin" %% "atom2twitterpublisher" % "1.1",
        "no.arktekk.atom-client" %% "atom-client-lift" % "1.0",

        "log4j" % "log4j" % "1.2.16",
        "org.slf4j" % "slf4j-log4j12" % "1.6.1",

        "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default" withSources(),
        "junit" % "junit" % "4.5" % "test->default",
        "org.specs2" %% "specs2" % "1.6.1" % "test->default" withSources(),
        "com.h2database" % "h2" % "1.2.138"
      )
    )
  )
}
