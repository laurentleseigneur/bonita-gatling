package com.bonitasoft

import java.util.concurrent.TimeUnit

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.core.session
import io.gatling.http.Predef.{jsonPath, _}
import io.gatling.jdbc.Predef._

class portal extends Simulation {

  val httpProtocol = http
    .baseURL("http://localhost:8080")
    .inferHtmlResources()
    .acceptHeader("image/webp,image/apng,image/*,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36")

  val headers_0 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_1 = Map("Accept" -> "text/css,*/*;q=0.1")

  val headers_2 = Map("Accept" -> "*/*")

  val headers_4 = Map(
    "Accept" -> "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01",
    "X-Requested-With" -> "XMLHttpRequest")

  val headers_6 = Map(
    "Accept" -> "*/*",
    "Content-Type" -> "application/json",
    "X-Bonita-API-Token" -> "cd60e0c6-adf0-43cf-a51b-039b7b7f1195")

  val headers_10 = Map(
    "Accept" -> "*/*",
    "Origin" -> "http://localhost:8080")

  val headers_22 = Map(
    "Accept" -> "application/json, text/plain, */*",
    "X-Bonita-API-Token" -> "cd60e0c6-adf0-43cf-a51b-039b7b7f1195")

  val headers_login = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Origin" -> "http://localhost:8080",
    "Upgrade-Insecure-Requests" -> "1")

  val headers_bonita = Map(
    "Accept" -> "*/*",
    "Content-Type" -> "application/json"
  )


  val uri1 = "http://localhost:8080/bonita"

  val scn = scenario("portal")
    // portal
    // login
    .exec(http("login")
    .post("/bonita/loginservice")
    .headers(headers_login)
    .formParam("username", "walter.bates")
    .formParam("password", "bpm")
    .formParam("redirect", "false")
    .formParam("tenant", "1")
    .formParam("_l", "en"))

    .pause(150 millis, 200 millis)
    .exec(http("session")
      .get("/bonita/API/system/session/unusedId")
      .check(status.is(200))
      .check(jsonPath("$.user_id").saveAs("userId"))
      .check(jsonPath("$.user_name").saveAs("userName"))
      .check(jsonPath("$.version").saveAs("version"))
    )


    .pause(150 millis, 200 millis)
    .exec(http("feature")
      .get("/bonita/API/system/feature?p=0&c=100")
      .check(status.is(200)))

    .pause(150 millis, 200 millis)
    .exec(http("profile")
      .get("/bonita/API/portal/profile?p=0&c=100&f=user_id%3d${userId}&f=hasNavigation%3dtrue")
      .check(status.is(200)))

    .pause(150 millis, 200 millis)
    .exec(http("process-search")
      .get("/bonita/API/bpm/process?p=0&c=10")
      .check(status.is(200)))


    .pause(150 millis, 200 millis)
    .exec(http("task-search")
      .get("/bonita/API/bpm/task?p=0&c=10&o=state")
      .check(status.is(200)))

    .pause(150 millis, 200 millis)
    .exec(
      http("logout")
        .get("/bonita/logoutservice?redirect=false")
        .check(status.is(200))
    )

  //  setUp(scn.inject(atOnceUsers(50))).protocols(httpProtocol)
  setUp(scn.inject(rampUsers(1000) over (5 minutes)).protocols(httpProtocol))
}