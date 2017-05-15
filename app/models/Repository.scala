package models

/**
  * Created by szq on 2017/5/13.
  */
case class Repository
(
  id:Option[Long],
  name:String,
  baseUrl:String,
  rootPath:String,
  description:String
)
