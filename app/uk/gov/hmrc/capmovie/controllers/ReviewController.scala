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

import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.capmovie.connectors.ReviewConnector
import uk.gov.hmrc.capmovie.controllers.predicates.CheckUser
import uk.gov.hmrc.capmovie.models.{RatingReg, Review, ReviewReg}
import uk.gov.hmrc.capmovie.views.html.{Confirmation, RatingPage, ReviewPage, ReviewSummary}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ReviewController @Inject()(mcc: MessagesControllerComponents,
                                 check: CheckUser,
                                 ratingPage: RatingPage,
                                 reviewPage: ReviewPage,
                                 summaryPage: ReviewSummary,
                                 successPage: Confirmation,
                                 connector: ReviewConnector) extends FrontendController(mcc) {

  def getRating(movieId: String): Action[AnyContent] = Action async { implicit request =>
    check.check { _ =>
      val form = RatingReg.form.fill(RatingReg(""))
      Future.successful(Ok(ratingPage(form, movieId, "user")))
    }
  }

  def submitRating(movieId: String): Action[AnyContent] = Action async { implicit request =>
    check.check { id =>
      RatingReg.form.bindFromRequest().fold({
        formWithErrors => Future.successful(BadRequest(ratingPage(formWithErrors, "", "user")))
      }, formData => if (id.contains("USER")) Future.successful(Redirect(routes.ReviewController.getReview(movieId)).withSession(request.session + ("rating" -> formData.rating))) else Future.successful(BadRequest))
    }
  }

  def getReview(movieId: String): Action[AnyContent] = Action async { implicit request =>
    check.check { _ =>
      val form = ReviewReg.form.fill(ReviewReg(""))
      Future.successful(Ok(reviewPage(form, movieId, "user")))
    }
  }

  def submitReview(movieId: String): Action[AnyContent] = Action async { implicit request =>
    check.check { id =>
      ReviewReg.form.bindFromRequest().fold({
        formWithError => Future.successful(BadRequest(reviewPage(formWithError, "", "user")))
      }, formData => if (id.contains("USER")) Future.successful(Redirect(routes.ReviewController.getSummary(movieId)).withSession(request.session + ("review" -> formData.review))) else Future.successful(BadRequest))
    }
  }

  def getSummary(movieId: String): Action[AnyContent] = Action async { implicit request =>
    check.check { userId =>
      val rating = request.session.get("rating").getOrElse("SOMETHING WENT WRONG!")
      val review = request.session.get("review").getOrElse("SOMETHING WENT WRONG!")
      Future.successful(Ok(summaryPage(Review(review, rating.toDouble), userId, movieId, "user")))
    }
  }

  def submit(movieId: String): Action[AnyContent] = Action async { implicit request =>
    check.check { userId =>
      connector.create(userId, movieId, Review(request.session.get("review").get, request.session.get("rating").get.toDouble)).map {
        case true => Ok(successPage("user"))
        case false => BadRequest
      }

    }
  }


}
