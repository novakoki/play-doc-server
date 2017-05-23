package parser

import java.io.File

import scala.meta._
import _root_.io.circe._
import _root_.io.circe.syntax._
import _root_.io.circe.generic.auto._
import scala.collection.immutable.Seq

/**
  * Created by szq on 2017/5/2.
  */
trait ControllerParser {
  def parseController(file:File):List[Controller] = {
    file.parse[Source] match {
      case parsers.Parsed.Success(tree) => {
        val controllers = tree.collect {
          case q"class $controller [..$tparams] ..$ctorMods (...$paramss) extends $template" => {
            template match {
              case template"Controller with ..$ctorcalls { $param => ..$block }" => (controller, block)
            }
          }
        }

        controllers.map {
          case (controller, block) => {
            val forms = collectForm(block)
            val actions = collectAction(block, forms)
            Controller(controller, actions)
          }
        }
      }
      case _ => Nil
    }
  }

  private def collectAction(block:Seq[Stat], forms:Seq[Form]) = {
    block.collect {
      case q"def $action(...$paras) = Action {$block}" => Action(action, paras, None)
      case q"def $action(...$paras) = Action.async {$block}" => Action(action, paras, None)
      case q"def $action(...$paras) = Action (parse.form($form)) {$block}" =>
        Action(action, paras, forms.find(_.name.toString == form.toString))
      case q"def $action(...$paras) = Action.async (parse.form($form)) {$block}" =>
        Action(action, paras, forms.find(_.name.toString == form.toString))
    }
  }

  private def collectForm(block:Seq[Stat]) = {
    block.collect {
      case q"val $form = Form(mapping(..$paras)($apply)($unapply))" => Form(form, paras)
    }
  }

  implicit val actionEncoder:Encoder[Action] = new Encoder[Action] {
    final def apply(action:Action):Json = {
      val paraList = action.parameters match {
        case paras::Nil => paras.map {
          case param"..$mods $key: $value = $default" => Parameter(key.toString, value.toString, "url")
        }.toList
        case _ => Nil
      }

      val formList = action.form match {
        case Some(f) => f.mappings.map {
          case q"$key -> $value" => Parameter(key.toString.drop(1).dropRight(1), value.toString, "body")
        }.toList
        case None => Nil
      }

      (paraList ++ formList).asJson
    }
  }
}
