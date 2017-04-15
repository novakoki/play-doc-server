package models

/**
  * Created by szq on 2017/4/13.
  */
object Method extends Enumeration {
  val GET, PATCH, POST, PUT, DELETE, HEAD, OPTION = Value
  def isMethod(s:String) = values.exists(p => p.toString == s)
}
