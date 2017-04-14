package services

import models._

/**
  * Created by szq on 2017/4/14.
  */
trait ApiService extends QuillSupport {
  import QDB._
  def getAllApis = {
    val selectApi = quote {
      query[Api]
    }
    run(selectApi)
  }
}
