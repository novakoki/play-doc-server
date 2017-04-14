package controllers

import play.api.mvc._
import models._
import services._
import io.circe.syntax._
import io.circe.generic.auto._

/**
  * Created by szq on 2017/4/13.
  */
class ApiController extends Controller with ApiService {
  def index = Action.async { request =>
    for {
      apis <- getAllApis
    } yield Ok(apis.asJson.noSpaces)
  }
}
