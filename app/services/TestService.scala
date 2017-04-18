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

  def addTest(test:Test) = {
    def insertTest(test:Test) = quote {
      query[Test].insert(lift(test))
    }
    run(insertTest(test))
  }

  def updateTestById(id:Long, test:Test) = {
    def updateTest(id:Long, test:Test) = quote {
      query[Test].filter(_.id == lift(id))
    }
    run(updateTest(id, test))
  }

  def deleteTestById(id:Long) = {
    def deleteTest(id:Long) = quote {
      query[Test].filter(_.id == lift(id)).delete
    }
    run(deleteTest(id))
  }
}
