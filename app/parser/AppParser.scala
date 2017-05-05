package parser

import scala.io.Source
import java.io.File
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import models.Api
import io.circe.syntax._


/**
  * Created by szq on 2017/5/4.
  */
trait AppParser extends ControllerParser with RouteParser {
  private def getRoutes(path: String = "conf/routes") = Future {
    Source.fromFile(path).getLines.map(parseRoute).flatten.toList
  }

  private def getControllers(path: String = "app/controllers") = Future {
    new File(path).listFiles.filter(_.isFile).map(parseController).toList
  }

  def parseApis:Future[List[Api]] = {
    for {
      routes <- getRoutes()
      controllers <- getControllers()
    } yield {
      for {
        Route(method, path, actionCall) <- routes
        controller <- controllers.flatten
        action <- controller.actions
        api = Api(method = method, resource = path.map(_.name).mkString("/"),
          parameters = Some(action.asJson.noSpaces)
        ) if actionCall.controller.value == controller.name.value &&
        actionCall.method.value == action.name.value
      } yield api
    }
  }
}
