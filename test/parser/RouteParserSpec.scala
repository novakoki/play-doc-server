package parser

/**
  * Created by shaoziqi on 2016/12/29.
  */
import org.scalatest._
import scala.util._
import scala.meta._
import models._

class RouteParserSpec extends FunSuite with Matchers with RouteParser {

  test("comment") {
    parseRoute("# Index Page") match {
      case Success(result) => result should be (None)
    }
  }

  test("static route") {
    parseRoute("GET /index controllers.IndexController.index") match {
      case Success(Route(method, path, action)) => {
        Method.isMethod(method) should be (true)
        path should contain (Path(2, "index", None))
        action match {
          case ActionCall(controller, method, params) => {
            controller match {
              case Term.Name(c) => c should be ("IndexController")
            }
            method match {
              case Term.Name(m) => m should be ("index")
            }
            params should be (None)
          }
        }
      }
    }
  }
}