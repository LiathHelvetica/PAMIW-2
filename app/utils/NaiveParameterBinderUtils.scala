package utils

import play.api.libs.json.JsNumber
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import scalikejdbc.AsIsParameterBinder
import scalikejdbc.ParameterBinder

import java.time.LocalDate
import scala.util.Failure
import scala.util.Success
import scala.util.Try

object NaiveParameterBinderUtils {

  def getParameterBinderForValue(a: Any): ParameterBinder = {
    a match {
      case i: BigInt => AsIsParameterBinder(i)
      case s: String => AsIsParameterBinder(s)
      case b: Boolean => AsIsParameterBinder(b)
      case d: LocalDate => AsIsParameterBinder(d)
      case _ => sys.error(s"ParameterBinder getter does not support value $a")
    }
  }

  def valueToParameterBinder(value: JsValue): ParameterBinder = {
    value match {
      case v: JsString => {
        Try {
          DateTimeUtils.parse(v.value)
        } match {
          case Success(date) => AsIsParameterBinder(date)
          case Failure(_) => AsIsParameterBinder(v.value)
        }
      }
      case v: JsNumber => AsIsParameterBinder(v.value.toBigInt)
      case _ => sys.error(s"$value not supported for parameter binders")
    }
  }

  implicit class NaivelyParseableString(val s: String) {

    def isBigInt: Boolean = Try { BigInt(s) }.isSuccess

    def isLocalDate: Boolean = Try { DateTimeUtils.parse(s) }.isSuccess
  }
}