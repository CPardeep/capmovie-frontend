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

import play.api.libs.json._

case class Review(movieId: Option[String],
                  userId: String,
                  review: String,
                  rating: Double,
                  isApproved: Boolean)

object Review {

  implicit val format: OFormat[Review] = Json.format[Review]

//  val newReviewReads: Reads[Review] = new Reads[Review] {
//
//    def reads(json: JsValue): JsResult[Review] = {
//      val result = for {
//        userId <- (json \ "userId").asOpt[String]
//        review <- (json \ "review").asOpt[String]
//        rating <- (json \ "rating").asOpt[Double]
//        isApproved <- (json \ "isApproved").asOpt[Boolean]
//      } yield Review(None, userId, review, rating, isApproved)
//      result match {
//        case Some(x) => JsSuccess(x)
//        case _ => JsError("ERROR")
//      }
//    }
//  }
}