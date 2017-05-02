package parser

import java.io.File
import scala.meta._

/**
  * Created by szq on 2017/5/2.
  */
trait ControllerParser {
  def parseController(filepath:String) = {
    val parseTry = new File(filepath).parse[Source]
    parseTry match {
      case parsers.Parsed.Success(tree) => tree.collect {
        case q"def $action(...$paras) = Action {$block}" => (action, paras)
        case q"def $action(...$paras) = Action.async {$block}" => (action, paras)
        case q"def $action(...$paras) = Action (parse.form($form)) {$block}" => (action, form)
        case q"def $action(...$paras) = Action.async (parse.form($form)) {$block}" => (action, paras, form)
        case q"val $form = Form(mapping(...$paras)($apply)($unapply))" => (form, paras)
      }
      case _ => None
    }
  }
}
