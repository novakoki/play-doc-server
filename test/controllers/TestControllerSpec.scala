package controllers

import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent._
import models._

/**
  * Created by szq on 2017/4/26.
  */
class TestControllerSpec extends PlaySpec {
  "TestController" should {
    WsTestClient.withClient { client =>
      val controller = new TestController(client)
      "get no tests with no such API id" in {
        val result = controller.getTests(100000).apply(FakeRequest())
        val bodyText = contentAsString(result)
        bodyText mustBe "[]"
      }
    }
  }
}
