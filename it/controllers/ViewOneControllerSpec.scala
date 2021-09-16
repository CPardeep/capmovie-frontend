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

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status.OK
import play.api.test.Helpers.{defaultAwaitTimeout, status}
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.capmovie.connectors.MovieConnector
import uk.gov.hmrc.capmovie.controllers.ViewOneController
import uk.gov.hmrc.capmovie.controllers.predicates.CheckUser
import uk.gov.hmrc.capmovie.models.Movie
import uk.gov.hmrc.capmovie.views.html.MoviePage

import scala.concurrent.Future

class ViewOneControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  val view: MoviePage = app.injector.instanceOf[MoviePage]
  val connector: MovieConnector = mock[MovieConnector]
  val check: CheckUser = app.injector.instanceOf[CheckUser]
  val controller = new ViewOneController(Helpers.stubMessagesControllerComponents(), view, check, connector)
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
    avgRating = 0.0)

  "viewOnePage" should {
    "load a movie" in {
      when(connector.readOne(any()))
        .thenReturn(Future.successful(Some(movie)))
      val result = controller.viewOnePage(movie.id).apply(FakeRequest("GET", "/"))
      status(result) shouldBe OK
    }
  }

}
