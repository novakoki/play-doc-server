package parser

/**
  * Created by szq on 2017/5/3.
  */

import scala.collection.immutable.Seq
import scala.meta._

case class Controller
(
  name:Type.Name,
  actions:Seq[Action]
)

case class Action
(
  name:Term.Name,
  parameters:Seq[Seq[Term.Param]],
  form:Option[Form]
)

case class Form
(
  name:Pat,
  mappings:Seq[Term.Arg]
)
