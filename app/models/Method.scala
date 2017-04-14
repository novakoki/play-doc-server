package models

import io.getquill._
import io.circe._
import io.circe.syntax._

/**
  * Created by szq on 2017/4/13.
  */
object Method extends Enumeration {
  val Get, Patch, Post, Put, Delete, Head, Option = Value
  implicit val encodeMethod = MappedEncoding[Value, String](_.toString)
  implicit val decodeMethod = MappedEncoding[String, Value](Method.withName(_))
  implicit val encodeJson: Encoder[Value] = new Encoder[Value] {
    final def apply(a: Value): Json = a.toString.asJson
  }
}
