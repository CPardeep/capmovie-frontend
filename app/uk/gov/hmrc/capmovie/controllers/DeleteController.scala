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

package uk.gov.hmrc.capmovie.controllers

import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.capmovie.connectors.MovieConnector
import uk.gov.hmrc.capmovie.controllers.predicates.CheckUser
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.capmovie.views.html.{DeleteAreYouSurePage, DeleteConfirmPage}

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DeleteController @Inject()(mcc: MessagesControllerComponents,
                                deletePage: DeleteAreYouSurePage,
                                 deleteConfirm: DeleteConfirmPage,
                                connector: MovieConnector,
                                 check: CheckUser
                                )
  extends FrontendController(mcc){

  def deleteAreYouSure(id: String) = Action async { implicit request =>
    check.check {
      case a if a.contains("ADMIN") => connector.readOne(id).map(movie => Ok(deletePage(movie.get, "admin")))
      case _ => Future(Redirect(routes.HomeController.homePage()))
    }
  }
  def deleteConfirmation(id: String) = Action async { implicit request =>
    check.check {
      case a if a.contains("ADMIN") => connector.delete(id).map {
        case true => Ok(deleteConfirm("admin"))
        case false => Redirect(routes.HomeController.homePage())
      }
      case _ => Future(Redirect(routes.HomeController.homePage()))
    }
  }
}
