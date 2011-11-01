import sbt._
import Keys._

object JavaZonePortal extends Build {

  val logbackVersion = "0.9.18"
  val abderaVersion = "1.1.2"
  val liftVersion = "2.4-M4"

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "no.java.javazone-portal",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.9.1"
  )

  lazy val project = Project(
    id = "javazone-portal",
    base = file("."),
    settings = buildSettings ++ Seq(
      description := "JavaZone 2012 Portal",
      libraryDependencies := Seq(
        "net.liftweb" %% "lift-common" % liftVersion % "compile->default" withSources(),
        "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default" withSources(),
        "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default" withSources(),
        "net.liftweb" %% "lift-util" % liftVersion % "compile->default" withSources(),

        "no.javabin" %% "atom2twitterpublisher" % "1.1-SNAPSHOT",
        "no.arktekk.atom-client" %% "atom-client-lift" % "1.0-SNAPSHOT",

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
