package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.{Api, Method, Overview}
import services.ApiService
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import _root_.parser.AppParser

/**
  * Created by szq on 2017/4/13.
  */
class ApiController extends Controller with ApiService with AppParser {
  def getApis = Action.async { implicit request =>
    for {
      apis <- getAllApis
    } yield Ok(apis.map {
      case (id, method, resource, summary, status, repoId) => Overview(id, method, resource, summary, status, repoId)
    }.asJson.noSpaces)
  }

  def getApi(id:Long) = Action.async { implicit request =>
    for {
      api <- getApiById(id)
    } yield {
      api match {
        case a :: Nil => Ok(a.asJson.noSpaces)
        case _ => NotFound
      }
    }
  }

  // 1234

  def createApi = Action.async(parse.form(apiForm)) { implicit request =>
    for {
      res <- addApi(request.body)
    } yield {
      Ok(res.asJson.noSpaces)
    }
  }

  def modifyApi = Action.async(parse.form(apiForm)) { implicit request =>
    val api = request.body
    for {
      res <- updateApiById(api)
    } yield Ok("")
  }

  def removeApi(id:Long) = Action.async { implicit request =>
    for {
      res <- removeApiById(id)
    } yield Ok("")
  }

  val apiForm = Form(
    mapping (
      "id" -> optional(longNumber),
      "method" -> nonEmptyText.verifying(Method.isMethod(_)),
      "resource" -> nonEmptyText,
      "summary" -> optional(text),
      "description" -> optional(text),
      "parameters" -> optional(text),
      "responses" -> optional(text),
      "status" -> optional(number),
      "repoId" -> optional(number)
    )(Api.apply)(Api.unapply)
  )

}
