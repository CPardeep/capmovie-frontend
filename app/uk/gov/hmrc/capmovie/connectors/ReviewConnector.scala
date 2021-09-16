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

package uk.gov.hmrc.capmovie.connectors

import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}
import uk.gov.hmrc.capmovie.models.Review
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ReviewConnector @Inject()(ws: WSClient, cc: ControllerComponents) extends AbstractController(cc) {

  def create(userId: String, movieId: String, review: Review): Future[Boolean] = {
    ws.url(s"http://localhost:9009/movie/$movieId/user/$userId/review").patch(Json.toJson(review)).map {
      _.status match {
        case CREATED => true
        case _ => false
      }
    }
  }

}
