package no.java.jzportal

import unfiltered.jetty._

object TestRunner extends App {
  Http(8080).
    plan(new JzPortalPlan).
    run()
}
