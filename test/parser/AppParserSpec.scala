package parser

import org.scalatest._
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by szq on 2017/5/4.
  */
class AppParserSpec  extends FunSuite with Matchers with AppParser {
  test("app") {
    Await.result(getApis, 5 seconds).foreach(println)
  }
}
