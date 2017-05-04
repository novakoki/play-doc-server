package parser

import scala.collection.immutable.Seq
import scala.meta.Term

/**
  * Created by szq on 2017/5/1.
  */

case class Route
(
  method: String,
  path: Seq[Path],
  action: ActionCall
)

case class Path
(
  `type`: Int,
  name: String,
  regex: Option[String]
) {
  def getType = `type` match {
    case 0 => "static"
    case 1 => "default"
    case 2 => "wildcard"
    case 3 => "custom"
  }
}

case class ActionCall
(
  controller: Term.Name,
  method: Term.Name,
  params: Option[Seq[Term.Arg]]
)
