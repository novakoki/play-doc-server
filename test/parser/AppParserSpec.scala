package parser

import org.scalatest._
import services.ApiService

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by szq on 2017/5/4.
  */
class AppParserSpec  extends FunSuite with Matchers with AppParser with ApiService {
  test("app") {
    Await.result(batchUpdateApis(1, Await.result(parseApis(1, "."), 5 seconds)), 5 seconds)
  }
}
