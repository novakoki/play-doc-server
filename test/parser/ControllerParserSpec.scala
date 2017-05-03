package parser

import io.circe.syntax._
import org.scalatest._

/**
  * Created by szq on 2017/5/2.
  */
class ControllerParserSpec extends FunSuite with Matchers with ControllerParser {
  test("controller") {
    val ast = parseController("app/controllers/ApiController.scala")
    println(ast.head.actions.asJson)
  }
}
