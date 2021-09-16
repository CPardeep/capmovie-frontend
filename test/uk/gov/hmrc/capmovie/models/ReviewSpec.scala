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
import play.api.libs.json.{JsSuccess, JsValue, Json}

class ReviewSpec extends AnyWordSpec with Matchers {

  val review: Review = Review(
    review = "TESTREVIEW",
    rating = 5.0,
    isApproved = false
  )

  val reviewJson: JsValue = Json.parse(
    s"""{
       |"review" : "TESTREVIEW",
       |"rating" : 5.0,
       |"isApproved" : false
       |}
       |""".stripMargin
  )

  "Review" can {
    "OFormat" should {
      "convert object into json" in {
        Json.toJson(review).toString() shouldBe reviewJson.toString()
      }
      "convert Json into object" in {
        Json.fromJson[Review](reviewJson).get.toString shouldBe JsSuccess(review).get.toString
      }
    }
  }

}
