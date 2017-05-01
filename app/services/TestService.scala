package services

import models._

/**
  * Created by szq on 2017/4/18.
  */
trait TestService extends QuillSupport {
  import QDB._
  def getTestsByApiId(apiId:Long) = {
    def selectTests(apiId:Long) = quote {
      query[Test].filter(_.apiId == lift(apiId))
    }
    run(selectTests(apiId))
  }

  def getTestById(id:Long) = {
    def selectTest(id:Option[Long]) = quote {
      query[Test].filter(_.id == lift(id))
    }
    run(selectTest(Some(id)))
  }

  def addTest(test:Test) = {
    def insertTest(test:Test) = quote {
      query[Test].insert(lift(test)).returning(_.id)
    }
    run(insertTest(test))
  }

  def updateParametersById(id:Long, parameters:Option[String]) = {
    def updateParameters(id:Option[Long], parameters:Option[String]) = quote {
      query[Test].filter(_.id == lift(id)).update(_.parameters -> lift(parameters))
    }
    run(updateParameters(Some(id), parameters))
  }

  def updateResponsesById(id:Option[Long], responses:String) = {
    def updateResponses(id:Option[Long], responses:Option[String]) = quote {
      query[Test].filter(_.id == lift(id)).update(_.responses -> lift(responses))
    }
    run(updateResponses(id, Some(responses)))
  }

  def deleteTestById(id:Long) = {
    def deleteTest(id:Option[Long]) = quote {
      query[Test].filter(_.id == lift(id)).delete
    }
    run(deleteTest(Some(id)))
  }
}
