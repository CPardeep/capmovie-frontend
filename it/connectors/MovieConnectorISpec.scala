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

package connectors

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json.Json
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.capmovie.connectors.MovieConnector
import uk.gov.hmrc.capmovie.models.{Movie, MovieWithAvgRating}

class MovieConnectorISpec extends AnyWordSpec with Matchers with GuiceOneServerPerSuite
  with WireMockHelper with BeforeAndAfterEach {

  lazy val connector: MovieConnector = app.injector.instanceOf[MovieConnector]

  override val wireMockPort: Int = 9009

  override def beforeEach(): Unit = startWireMock()

  override def afterEach(): Unit = stopWireMock()

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
  val movieWithAvgRating: MovieWithAvgRating = MovieWithAvgRating(
    movie = movie,
    avgRating = 0.0
  )

  val movieList = List(movie, movie.copy(id = "TESTMOV2"))

  "readAll" should {
    "return movieList" in {
      stubGet(s"/movie-list", 200, Json.toJson(movieList).toString())
      val result = connector.readAll()
      await(result) shouldBe movieList
    }
    "return emptyList" in {
      stubGet(s"/movie-list", 400, Json.toJson("{}").toString())
      val result = connector.readAll()
      await(result) shouldBe List()
    }

    "fail to connect to database" in {
      stubGet(s"/movie-list", 200, Json.toJson("{}").toString())
      val result = connector.readAll()
      await(result) shouldBe List()
    }
  }
  "readOne" should {
    "return a movie" in {
      stubGet(s"/movie/${movie.id}", 200, Json.toJson(movieWithAvgRating).toString())
      val result = connector.readOne(movie.id)
      await(result) shouldBe Some(movieWithAvgRating)
    }
    "return None" in {
      stubGet(s"/movie/${movie.id}", 200, Json.toJson("{}").toString())
      val result = connector.readOne(movie.id)
      await(result) shouldBe None
    }
    "throw an exception" in {
      stubGet(s"/movie/${movie.id}", 404, Json.toJson("{}").toString())
      val result = await(connector.readOne(movie.id))
      result shouldBe None
    }
  }
  "delete" should {
    "return true" in {
      stubDelete(
        url = s"/delete/${movie.id}",
        status = 204,
        responseBody = "")
      val result = await(connector.delete(movie.id))
      result shouldBe true
    }
    "return false" in {
      stubDelete(
        url = s"/delete/${movie.id}",
        status = 404,
        responseBody = "")
      val result = await(connector.delete(movie.id))
      result shouldBe false
    }
  }
}
