package utils

import play.api.libs.json.JsArray
import play.api.libs.json.JsNull
import play.api.libs.json.JsNumber
import play.api.libs.json.JsString

object Playground extends App {
  val a = JsNumber(1)
  val b = JsString("qwe")
  val c = JsNull
  val d = JsArray(Seq(JsNumber(1), JsString("qwe")))

  println(a.toString())
  println(b.toString())
  println(c.toString())
  println(d.toString())
}
