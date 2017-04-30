package controllers

import play.api.mvc._
import services._
import play.api.data._
import play.api.data.Forms._
import models._
import javax.inject.Inject
import play.api.libs.ws._
import io.circe.syntax._
import io.circe.generic.auto._
import scala.concurrent.Future

/**
  * Created by szq on 2017/4/15.
  */
class TestController @Inject() (ws:WSClient) extends Controller
  with TestService
  with ApiService {

  def getTests(apiId:Long) = Action.async { implicit resquest =>
    for {
      tests <- getTestsByApiId(apiId)
    } yield Ok(tests.asJson.noSpaces)
  }

  def removeTest(id:Long) = Action.async {implicit request =>
    for {
      res <- deleteTestById(id)
    } yield Ok("")
  }

  def createTest(apiId:Long) = Action.async(parse.form(testForm)) { implicit request =>
    val test = request.body
    for {
      id <- addTest(test)
      response <- sendTest(test)
      r <- updateResponsesById(id, response.body)
    } yield Ok("")
  }

  def modifyTest(id:Long) = Action.async(parse.form(testForm)) {implicit request =>
    val test = request.body
    for {
      p <- updateParametersById(id, test.parameters)
      response <- sendTest(test)
      r <- updateResponsesById(Some(id), response.body) if response.status == 200
    } yield Ok("")
  }

  def sendTest(test:Test) = {
    val apis = getApiById(test.apiId)
    apis.flatMap {
      case api :: Nil => {
        val req = ws.url(s"http://${api.resource}").withBody(test.parameters.getOrElse(""))
        req.execute(api.method)
      }
    }
  }

  def sendTest(apiId:Long, parameters:Option[String] = None) = Action.async {
    val apis = getApiById(apiId)
    apis.flatMap {
      case api :: Nil => {
        val req = ws.url(s"http://${api.resource}").withBody(parameters.getOrElse(""))
        req.execute(api.method).map(p => Ok(p.body))
      }
      case _ => Future.successful(NotFound)
    }
  }

  val testForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "apiId" -> longNumber,
      "parameters" -> optional(text),
      "responses" -> optional(text),
      "status" -> optional(number)
    )(Test.apply)(Test.unapply)
  )
}
