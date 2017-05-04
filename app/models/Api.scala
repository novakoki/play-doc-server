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
  parameters:Option[String] = None,
  responses:Option[String] = None,
  status:Option[Int] = None
)

case class Overview
(
  id:Option[Long] = None,
  method:String,
  resource:String,
  summary:Option[String] = None,
  status:Option[Int] = None
)
