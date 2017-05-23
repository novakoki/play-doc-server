package controllers

import play.api.mvc._
import services.{ApiService, TestService}
import play.api.data._
import play.api.data.Forms._
import models.{Api, Test}
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

  def removeTest(id:Long) = Action.async { implicit request =>
    for {
      res <- deleteTestById(id)
    } yield Ok("")
  }

  def createTest(apiId:Long) = Action.async(parse.form(testForm)) { implicit request =>
    val test = request.body
    for {
      id <- addTest(test)
      resOpt <- sendTest(test)
      r <- updateResponses(id, resOpt)
    } yield Ok("")
  }

  def modifyTest(id:Long) = Action.async(parse.form(testForm)) { implicit request =>
    val test = request.body
    for {
      p <- updateParametersById(id, test.parameters)
      resOpt <- sendTest(test)
      r <- updateResponses(Some(id), resOpt)
    } yield Ok("")
  }

  def updateResponses(id:Option[Long], resOpt:Option[WSResponse]) = {
    resOpt match {
      case Some(res) => updateResponsesById(id, res.body).map(Some(_))
      case None => Future.successful(None)
    }
  }

  def sendTest(test:Test) = {
    for {
      apis <- getApiById(test.apiId)
      resOpt <- sendTestOpt(apis.headOption, Some(test))
    } yield resOpt
  }

  def sendTestOpt(apiOpt:Option[Api], testOpt:Option[Test]) = {
    apiOpt match {
      case Some(api) =>
        testOpt match {
          case Some(test) => val req = ws.url(
            s"http://${api.resource}").withBody(test.parameters.getOrElse(""))
            req.execute(api.method).map(Some(_))
          case _ => Future(None)
        }
      case _ => Future(None)
    }
  }

//  def sendTest(apiId:Long, id:Long):Action[AnyContent] = Action.async {implicit request =>
//    for {
//      apis <- getApiById(apiId)
//      tests <- getTestById(id)
//      res <- sendTest(apis.headOption, tests.headOption)
//    } yield res match {
//      case Some(r) => Ok(r.body)
//      case None => NotFound
//    }
//  }

  val testForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "apiId" -> longNumber,
      "parameters" -> optional(text),
      "expect_responses" -> optional(text),
      "actual_responses" -> optional(text),
      "status" -> optional(number)
    )(Test.apply)(Test.unapply)
  )
}
