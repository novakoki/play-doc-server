package parser

import scala.meta._
import org.scalatest._

/**
  * Created by szq on 2017/5/2.
  */
class ControllerParserSpec extends FunSuite with Matchers with ControllerParser {
  test("controller") {
    println(parseController("app/controllers/ApiController.scala"))
  }
}
