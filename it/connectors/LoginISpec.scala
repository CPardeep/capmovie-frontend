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
import play.api.http.Status.{BAD_REQUEST, OK, UNAUTHORIZED}
import play.api.libs.json.Json
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.capmovie.connectors.LoginConnector
import uk.gov.hmrc.capmovie.models.User

class LoginISpec extends AnyWordSpec with Matchers with GuiceOneServerPerSuite with WireMockHelper with BeforeAndAfterEach{

  lazy val connector: LoginConnector = app.injector.instanceOf[LoginConnector]

  override val wireMockPort: Int = 9009

  override def beforeEach(): Unit = startWireMock()

  override def afterEach(): Unit = stopWireMock()

  val user: User = User(
    id = "TESTID",
    password = "TESTPASS"
  )

  "login" should {
    "return Ok" in {
      stubPost(s"/user-login", 200, Json.toJson(user).toString())
      val result = connector.login(user)
      await(result) shouldBe OK
    }

    "return Unauthorised" in {
      stubPost(s"/user-login", 401, Json.toJson("{}").toString())
      val result = connector.login(user)
      await(result) shouldBe UNAUTHORIZED
    }
  }
  "return Badrequest" in {
    stubPost(s"/user-login", 400, Json.toJson("{}").toString())
    val result = connector.login(user)
    await(result) shouldBe BAD_REQUEST

  }

}
