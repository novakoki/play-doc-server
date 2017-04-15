package services

import models._

/**
  * Created by szq on 2017/4/14.
  */
trait ApiService extends QuillSupport {
  import QDB._

  def getAllApis = {
    val selectAllApis = quote {
      query[Api].map(p => (p.id, p.method, p.resource, p.summary))
    }
    run(selectAllApis)
  }

  def getApiById(id:Long) = {
    def selectApi(id:Option[Long]) = quote {
      query[Api].filter(p => p.id == lift(id))
    }
    run(selectApi(Some(id)))
  }

  def addApi(api:Api) = {
    def insertApi(api:Api) = quote {
      query[Api].insert(lift(api))
    }
    run(insertApi(api))
  }

  def updateApiById(id:Long, api:Api) = {
    def updateApi(id:Option[Long], api:Api) = quote {
      query[Api].filter(p => p.id == lift(id)).update(lift(api))
    }
    run(updateApi(Some(id), api))
  }

  def removeApiById(id:Long) = {
    def deleteApi(id:Option[Long]) = quote {
      query[Api].filter(p => p.id == lift(id)).delete
    }
    run(deleteApi(Some(id)))
  }
}
