package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import services._
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._

/**
  * Created by szq on 2017/4/13.
  */
class ApiController extends Controller with ApiService {
  def getApis = Action.async {implicit request =>
    for {
      apis <- getAllApis
    } yield Ok(apis.map {
      case (id, method, resource, summary) => Overview(id, method, resource, summary)
    }.asJson.noSpaces)
  }

  def getApi(id:Long) = Action.async {implicit request =>
    for {
      api <- getApiById(id)
    } yield {
      api match {
        case a :: Nil => Ok(a.asJson.noSpaces)
        case _ => NotFound
      }
    }
  }

  def createApi = Action.async(parse.form(apiForm)) {implicit request =>
    for {
      res <- addApi(request.body)
    } yield {
      Ok(res.asJson.noSpaces)
    }
  }

  def modifyApi(id:Long) = Action.async(parse.form(apiForm)) {implicit request =>
    val api = request.body
    for {
      res <- updateApiById(id, api)
    } yield Ok("")
  }

  def removeApi(id:Long) = Action.async {implicit request =>
    for {
      res <- removeApiById(id)
    } yield Ok("")
  }

  def apiForm = Form(
    mapping (
      "id" -> optional(longNumber),
      "method" -> nonEmptyText.verifying(s => Method.isMethod(s)),
      "resource" -> nonEmptyText,
      "summary" -> optional(text),
      "description" -> optional(text),
      "parameters" -> optional(text),
      "responses" -> optional(text)
    )(Api.apply)(Api.unapply)
  )

}
