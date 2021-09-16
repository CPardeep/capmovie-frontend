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
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status.{BAD_REQUEST, OK, SEE_OTHER}
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, status}
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.capmovie.connectors.ReviewConnector
import uk.gov.hmrc.capmovie.controllers.ReviewController
import uk.gov.hmrc.capmovie.controllers.predicates.CheckUser
import uk.gov.hmrc.capmovie.views.html.{Confirmation, RatingPage, ReviewPage, ReviewSummary}

import scala.concurrent.Future

class ReviewControllerISpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {
  val check: CheckUser = app.injector.instanceOf[CheckUser]
  val ratingPage: RatingPage = app.injector.instanceOf[RatingPage]
  val reviewPage: ReviewPage = app.injector.instanceOf[ReviewPage]
  val summaryPage: ReviewSummary = app.injector.instanceOf[ReviewSummary]
  val successPage: Confirmation = app.injector.instanceOf[Confirmation]
  val connector: ReviewConnector = mock[ReviewConnector]
  val controller = new ReviewController(Helpers.stubMessagesControllerComponents(), check, ratingPage, reviewPage, summaryPage, successPage, connector)

  "GET /movie/:id/create/rating" should {
    "load the rating page" in {
      val result = controller.getRating("TESTMOVIE").apply(FakeRequest("GET", "/").withSession("adminId" -> "USERTEST"))
      status(result) shouldBe OK
      Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-radios__item").size() shouldBe 5
    }
  }

  "POST /movie/:id/create/rating" should {
    "Badrequest" when {
      "if the fields are left empty" in {
        val result = controller.submitRating("TESTMOVIE").apply(FakeRequest("GET", "/")
          .withSession("adminId" -> "USERTEST")
          .withFormUrlEncodedBody("rating" -> ""))
        status(result) shouldBe BAD_REQUEST
      }
      "if the wrong user is logged in" in {
        val result = controller.submitRating("TESTMOVIE").apply(FakeRequest("GET", "/")
          .withSession("adminId" -> "ADMINTEST")
          .withFormUrlEncodedBody("rating" -> "3"))
        status(result) shouldBe BAD_REQUEST
      }
    }
    "Redirect" in {
      val result = controller.submitRating("TESTMOVIE").apply(FakeRequest("GET", "/")
        .withSession("adminId" -> "USERTEST")
        .withFormUrlEncodedBody("rating" -> "3"))
      status(result) shouldBe SEE_OTHER
    }
  }

  "GET /movie/:id/create/review" should {
    "load the review page" in {
      val result = controller.getReview("TESTMOVIE").apply(FakeRequest("GET", "/").withSession("adminId" -> "USERTEST"))
      status(result) shouldBe OK
      Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-textarea").size() shouldBe 1
    }
  }

  "POST /movie/:id/create/review" should {
    "Badrequest" when {
      "if the fields are left empty" in {
        val result = controller.submitReview("TESTMOVIE").apply(FakeRequest("GET", "/")
          .withSession("adminId" -> "USERTEST")
          .withFormUrlEncodedBody("review"-> ""))
        status(result) shouldBe BAD_REQUEST
      }
      "if the wrong user is logged in" in {
        val result = controller.submitReview("TESTMOVIE").apply(FakeRequest("GET", "/")
          .withSession("adminId" -> "ADMINTEST")
          .withFormUrlEncodedBody("review" -> "TestReviewInput"))
        status(result) shouldBe BAD_REQUEST
      }
    }
    "Redirect" in {
      val result = controller.submitReview("TESTMOVIE").apply(FakeRequest("GET", "/")
        .withSession("adminId" -> "USERTEST")
        .withFormUrlEncodedBody("review" -> "TestReviewInput"))
      status(result) shouldBe SEE_OTHER
    }
  }

  "GET /movie/:id/create/review/summary" should {
    "load the summary page" in {
      val result = controller.getSummary("TESTMOVIE").apply(FakeRequest("GET", "/").withSession("adminId" -> "USERTEST", "rating" -> "1", "review" -> "testReview"))
      status(result) shouldBe OK
      Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-summary-list__value").size() shouldBe 2
    }
  }

  "GET /movie/:id/create/review/sudmit" should {
    "load the confirmation page if review is added to db" in {
      when(connector.create(any(), any(), any())) thenReturn Future.successful(true)
      val result = controller.submit("TESTMOVIE").apply(FakeRequest("GET", "/").withSession("adminId" -> "USERTEST", "rating" -> "1", "review" -> "testReview"))
      status(result) shouldBe OK
    }
    "Badrequest" when {
      "if the db returns false" in {
        when(connector.create(any(), any(), any())) thenReturn Future.successful(false)
        val result = controller.submit("TESTMOVIE").apply(FakeRequest("GET", "/").withSession("adminId" -> "USERTEST", "rating" -> "1", "review" -> "testReview"))
        status(result) shouldBe BAD_REQUEST
      }
    }
  }

}
