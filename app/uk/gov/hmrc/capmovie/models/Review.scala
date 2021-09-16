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

import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.libs.json.{Json, OFormat}

case class Review(review: String, rating: Double, isApproved: Boolean = false)

object Review {
  implicit val format: OFormat[Review] = Json.format[Review]
}

case class ReviewReg(review: String)

object ReviewReg {
  val form: Form[ReviewReg] = Form(
    mapping(
      "review" -> text.verifying("cannot leave empty", _.nonEmpty)
    )(ReviewReg.apply)(ReviewReg.unapply)
  )
}

case class RatingReg(rating: String)

object RatingReg{
  val form: Form[RatingReg] = Form(
    mapping(
      "rating" -> text.verifying("cannot leave empty", _.nonEmpty)
    )(RatingReg.apply)(RatingReg.unapply)
  )
}
