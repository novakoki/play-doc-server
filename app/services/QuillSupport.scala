package services

import io.circe.Json
import io.circe.parser.parse
import io.getquill._

/**
  * Created by szq on 2017/4/14.
  */
trait QuillSupport {
  lazy val QDB = new MysqlAsyncContext[SnakeCase]("QDB")
  implicit val QDBcontext = scala.concurrent.ExecutionContext.Implicits.global
  implicit val encodeJson = MappedEncoding[Json, String](_.noSpaces)
  implicit val decodeJson = MappedEncoding[String, Json](parse(_) match {
    case Right(r) => r
    case _ => Json.Null
  })
}
