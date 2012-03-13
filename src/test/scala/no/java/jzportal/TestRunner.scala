package no.java.jzportal

import unfiltered.jetty._

object TestRunner extends App {
  Http(8080).
    context("/javazone2012") { builder =>
      builder.filter(new JzPortalPlan)
    }.run()
}
