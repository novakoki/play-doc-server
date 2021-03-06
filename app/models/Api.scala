package models

/**
  * Created by szq on 2017/4/13.
  */

case class Api
(
  id:Option[Long] = None,
  method:String,
  resource:String,
  summary:Option[String] = None,
  description:Option[String] = None,
  actualParameters:Option[String] = None,
  actualResponses:Option[String] = None,
  expectParameters:Option[String] = None,
  expectResponses:Option[String] = None,
  status:Option[Int] = None,
  repoId:Long
)

case class Overview
(
  id:Option[Long] = None,
  method:String,
  resource:String,
  summary:Option[String] = None,
  status:Option[Int] = None,
  repoId:Long
)
