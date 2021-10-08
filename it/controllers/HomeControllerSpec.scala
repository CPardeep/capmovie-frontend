/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import org.jsoup.Jsoup
import org.mockito.Mockito.when
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status.OK
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, status}
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.capmovie.connectors.MovieConnector
import uk.gov.hmrc.capmovie.controllers.HomeController
import uk.gov.hmrc.capmovie.controllers.predicates.CheckUser
import uk.gov.hmrc.capmovie.models.Movie
import uk.gov.hmrc.capmovie.views.html.{HomePage, StartPage}

import scala.concurrent.Future

class HomeControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite{

  val home: HomePage = app.injector.instanceOf[HomePage]
  val start: StartPage = app.injector.instanceOf[StartPage]
  val check: CheckUser = app.injector.instanceOf[CheckUser]
  val connector: MovieConnector = mock[MovieConnector]
  val controller = new HomeController(Helpers.stubMessagesControllerComponents(), home, start, check, connector)

  val movie: Movie = Movie(
    id = "TESTMOV",
    plot = "Test plot",
    genres = List(
      "testGenre1",
      "testGenre2"),
    rated = "testRating",
    cast = List(
      "testPerson",
      "TestPerson"),
    poster = "testURL",
    title = "testTitle",
    reviews = List()
    )

  "homePage" should {
    "load movieList with standard navbar" in {
      when(connector.readAll()) thenReturn Future.successful(List(movie))
      val result = controller.homePage(FakeRequest("GET", "/"))
      status(result) shouldBe OK
      Jsoup.parse(contentAsString(result)).getElementById("navigation").childrenSize() shouldBe 2
    }
    "load movieList with admin navbar" in {
      when(connector.readAll()) thenReturn Future.successful(List(movie))
      val result = controller.homePage(FakeRequest("GET", "/").withSession("adminId" -> "ADMIN101"))
      status(result) shouldBe OK
      Jsoup.parse(contentAsString(result)).getElementById("navigation-admin").childrenSize() shouldBe 3
    }
    "load movieList with user navbar" in {
      when(connector.readAll()) thenReturn Future.successful(List(movie))
      val result = controller.homePage(FakeRequest("GET", "/").withSession("adminId" -> "USER101"))
      status(result) shouldBe OK
      Jsoup.parse(contentAsString(result)).getElementById("navigation-user").childrenSize() shouldBe 1
    }
  }

  "startPage" should {
    "load the start page" in {
      val result = controller.startPage(FakeRequest("GET", "/"))
      status(result) shouldBe OK
    }
  }
}
