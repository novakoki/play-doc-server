package controllers

import play.api.mvc._
import services._
import play.api.data._
import play.api.data.Forms._
import models._

import javax.inject.Inject

import play.api.libs.ws._

/**
  * Created by szq on 2017/4/15.
  */
class TestController @Inject() (ws:WSClient) extends Controller
  with TestService
  with ApiService {
  def getTests(apiId:Long) = Action.async { implicit resquest =>
    for {
      tests <- getTestsByApiId(apiId)
    } yield Ok("")
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
      r <- updateResponsesById(Some(id), response.body) if response.status == 1
    } yield Ok("")
  }

  def sendTest(test:Test) = {
    for {
      apis <- getApiById(test.apiId)
      api = apis.head if apis.nonEmpty
      req = ws.url(api.resource)
      res <- api.method match {
        case "GET" => req.get
        // case "POST" => req.post
        // case "PUT" => req.put
        case "DELETE" => req.delete
      }
    } yield res
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
