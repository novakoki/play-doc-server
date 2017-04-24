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
class TestController @Inject() (ws:WSClient) extends Controller with TestService {
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


  // need to confirm test status
  def createTest(apiId:Long) = Action.async(parse.form(testForm)) { implicit request =>
    val test = request.body
    for {
      res <- addTest(test)
      status <- sendTest(test)
    } yield Ok("")
  }

  def modifyTest(id:Long) = Action.async(parse.form(testForm)) {implicit request =>
    val test = request.body
    for {
      res <- updateTestById(id, test)
      status <- sendTest(test)
    } yield Ok("")
  }

  def sendTest(test:Test) = {
    val req = ws.url("")
    req.get
  }

  val testForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "apiId" -> longNumber,
      "parameters" -> optional(text),
      "responses" -> optional(text),
      "status" -> number
    )(Test.apply)(Test.unapply)
  )
}
