package services

import models._
import cats.implicits._

/**
  * Created by szq on 2017/4/14.
  */
trait ApiService extends QuillSupport {
  import QDB._

  def getAllApis = {
    val selectAllApis = quote {
      query[Api].map(p => (p.id, p.method, p.resource, p.summary, p.status))
    }
    run(selectAllApis)
  }

  def getApiById(id:Long) = {
    def selectApi(id:Option[Long]) = quote {
      query[Api].filter(_.id == lift(id))
    }
    run(selectApi(Some(id)))
  }

  def getApiByMethodAndResource(method:String, resource:String) = {
    def selectApi(method:String, resource:String) = quote {
      query[Api].filter(p => p.method == lift(method) && p.resource == lift(resource))
    }
    run(selectApi(method, resource))
  }

  def addApi(api:Api) = {
    def insertApi(api:Api) = quote {
      query[Api].insert(lift(api)).returning(_.id)
    }
    run(insertApi(api))
  }

  def updateApiById(id:Long, api:Api) = {
    def updateApi(id:Option[Long], api:Api) = quote {
      query[Api].filter(_.id == lift(id)).update(lift(api))
    }
    run(updateApi(Some(id), api))
  }

  def updateParametersById(id:Option[Long], parameters:Option[String]) = {
    def updateApi(id:Option[Long], parameters:Option[String]) = quote {
      query[Api].filter(_.id == lift(id)).update(_.parameters -> lift(parameters))
    }
    run(updateApi(id, parameters))
  }

  def batchUpdateApis(apis:List[Api]) = {
//    def updateApis(apis:List[Api]) = quote {
//      liftQuery(apis).foreach { api =>
//        val q = query[Api].filter(p => p.method == api.method && p.resource == api.resource)
//        if (q.nonEmpty)
//          q.update(_.parameters -> api.parameters)
//        else query[Api].insert(api)
//      }
//    }
//    run(updateApis(apis.toList))
    apis traverse { api =>
      getApiByMethodAndResource(api.method, api.resource) flatMap {
        case head::tail => updateParametersById(head.id, head.parameters)
        case Nil => addApi(api)
      }
    }
    //
//    for {
//      api <- apis
//    } yield {
//      for {
//        res <- getApiByMethodAndResource(api.method, api.resource)
//      } yield {
//        res match {
//          case head::Nil => updateParametersById(head.id, head.parameters)
//          case Nil => addApi(api)
//        }
//      }
//    }
  }

  def removeApiById(id:Long) = {
    def deleteApi(id:Option[Long]) = quote {
      query[Api].filter(_.id == lift(id)).delete
    }
    run(deleteApi(Some(id)))
  }
}
