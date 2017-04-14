package models

import io.circe.Json

/**
  * Created by szq on 2017/4/13.
  */

case class Api
(
  method:Method.Value,
  resource:String,
  summary:Option[String],
  description:Option[String],
  parameters:Option[Json],
  responses:Option[Json]
)
