package models

/**
  * Created by szq on 2017/4/13.
  */

case class Api
(
  id:Option[Long],
  method:String,
  resource:String,
  summary:Option[String],
  description:Option[String],
  parameters:Option[String],
  responses:Option[String],
  status:Int
)

case class Overview
(
  id:Option[Long],
  method:String,
  resource:String,
  summary:Option[String],
  status:Int
)
