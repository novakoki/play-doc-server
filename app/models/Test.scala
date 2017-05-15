package models

/**
  * Created by szq on 2017/4/15.
  */
case class Test
(
  id:Option[Long],
  apiId:Long,
  parameters:Option[String],
  expectResponses:Option[String],
  actualResponses:Option[String],
  status:Option[Int]
)
