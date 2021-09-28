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

package uk.gov.hmrc.capmovie.models

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class ReviewSpec extends AnyWordSpec with Matchers {

  val reviewMax = Review(
    movieId = Some("MOV0001"),
    userId = "TESTUSER",
    review = "Good",
    rating = 5.0,
    isApproved = true)

  val reviewJsonMax = Json.obj(
    "movieId" -> reviewMax.movieId,
    "userId" -> reviewMax.userId,
    "review" -> reviewMax.review,
    "rating" -> reviewMax.rating,
    "isApproved" -> reviewMax.isApproved
  )

  val reviewMin = reviewMax.copy(movieId = None)
  val reviewJsonMin = reviewJsonMax - "movieId"

  "Review" can {
    "OFormat" when {
      "given Max values" should {
        "convert object to json" in {
          Json.toJson(reviewMax) shouldBe reviewJsonMax
        }
        "convert json to object" in {
          Json.fromJson[Review](reviewJsonMax).get shouldBe reviewMax
        }
      }
      "given Min values" should {
        "convert object to json" in {
          Json.toJson(reviewMin) shouldBe reviewJsonMin
        }
        "convert json to object" in {
          Json.fromJson[Review](reviewJsonMin).get shouldBe reviewMin
        }
      }
    }
  }
}
