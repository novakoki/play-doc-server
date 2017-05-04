package parser

import scala.io.Source
import java.io.File
import scala.concurrent._
import models.Api
import scala.collection.immutable.Seq
import io.circe.syntax._


/**
  * Created by szq on 2017/5/4.
  */
trait AppParser extends ControllerParser with RouteParser {
  implicit val context = ExecutionContext.Implicits.global

  private def getRoutes(path: String = "conf/routes") = Future {
    Source.fromFile(path).getLines.map(parseRoute).flatten.to[Seq]
  }

  private def getControllers(path: String = "app/controllers") = Future {
    new File(path).listFiles.filter(_.isFile).map(parseController).to[Seq]
  }

  def getApis:Future[Seq[Api]] = {
    for {
      routes <- getRoutes()
      controllers <- getControllers()
    } yield {
      for {
        Route(method, path, actionCall) <- routes
        cs <- controllers
        controller <- cs
        action <- controller.actions
        api = Api(method = method, resource = path.map(_.name).mkString("/"),
          parameters = Some(action.asJson.noSpaces)
        ) if actionCall.controller.value == controller.name.value &&
        actionCall.method.value == action.name.value
      } yield api
    }
  }
}
