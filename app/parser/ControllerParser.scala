package parser

import java.io.File

import scala.meta._
import _root_.io.circe._
import _root_.io.circe.syntax._
import scala.collection.immutable.Seq

/**
  * Created by szq on 2017/5/2.
  */
trait ControllerParser {
  def parseController(file:File):Seq[Controller] = {
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

  implicit def formToMap(form:Form):Map[String, String] = {
    def helper(mappings:Seq[Term.Arg], acc:Map[String, String]):Map[String, String] = {
      mappings match {
        case head::tail => {
          val q"$key -> $value" = head
          helper(tail, acc + (key.toString.drop(1).dropRight(1) -> value.toString))
        }
        case Nil => acc
      }
    }
    helper(form.mappings, Map.empty)
  }

  implicit def actionToMap(action:Action):Map[String, Map[String, String]] = {
    def helper(paras:Seq[Term.Param], acc:Map[String, String]):Map[String, String] = {
      paras match {
        case head::tail => {
          val param"..$mods $key: $value = $default" = head
          helper(tail, acc + (key.toString -> value.toString))
        }
        case Nil => acc
      }
    }

    val emptyMap = Map.empty[String,String]

    val parasMap = action.parameters match {
      case paras::Nil => helper(paras, emptyMap)
      case Nil => emptyMap
    }

    val formMap = action.form match {
      case Some(f) => f.toMap
      case None => emptyMap
    }

    Map(
      "query" -> parasMap,
      "body" -> formMap
    )
  }

  implicit val formEncoder:Encoder[Form] = new Encoder[Form] {
    final def apply(form:Form):Json = form.toMap.asJson
  }

  implicit val actionEncoder:Encoder[Action] = new Encoder[Action] {
    final def apply(action:Action):Json = action.toMap.asJson
  }
}
