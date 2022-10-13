package logic

import logic.FilterKeyword.ContainsFilter
import logic.FilterKeyword.EqualityFilter
import logic.FilterKeyword.GreaterThanFilter
import logic.FilterKeyword.LesserThanFilter
import logic.FilterKeyword.filterKeywords
import lthv.LthvEnum
import scalikejdbc.ParameterBinderFactory
import scalikejdbc.interpolation.SQLSyntax
import utils.DateTimeUtils
import utils.NaiveParameterBinderUtils.NaivelyParseableString

import java.time.LocalDate

abstract class Filter[T](filter: String, columnName: SQLSyntax, value: T) {

  def toSqlSyntax: SQLSyntax = {
    filter match {
      case EqualityFilter.keyword => SQLSyntax.eq(columnName, value)(parameterBinderFactory)
      case LesserThanFilter.keyword => SQLSyntax.lt(columnName, value)(parameterBinderFactory)
      case GreaterThanFilter.keyword => SQLSyntax.gt(columnName, value)(parameterBinderFactory)
      case ContainsFilter.keyword => SQLSyntax.like(columnName, s"%${value.toString}%")
      case _ => sys.error(s"Improper filter keyword $filter")
    }
  }

  def parameterBinderFactory: ParameterBinderFactory[T]
}

case class StringFilter(filter: String, columnName: SQLSyntax, value: String) extends Filter(filter, columnName, value) {
  override def parameterBinderFactory: ParameterBinderFactory[String] = ParameterBinderFactory.stringParameterBinderFactory
}

case class LocalDateFilter(filter: String, columnName: SQLSyntax, value: LocalDate) extends Filter(filter, columnName, value) {
  override def parameterBinderFactory: ParameterBinderFactory[LocalDate] = ParameterBinderFactory.javaTimeLocalDateParameterBinderFactory
}

case class BigIntFilter(filter: String, columnName: SQLSyntax, value: BigInt) extends Filter(filter, columnName, value) {
  override def parameterBinderFactory: ParameterBinderFactory[BigInt] = ParameterBinderFactory.bigIntParameterBinderFactory
}

object Filter {
  val filterSeparator = ":"

  def apply(filterDefinition: String, columnName: SQLSyntax): Option[Filter[Any]] = {
    filterDefinition.split(filterSeparator).toSeq match {
      case Seq(filter, v) if filterKeywords.contains(filter) => Some(
        (filter, v) match {
          // TODO: this is proof that I need to do my homework on generics
          case _ if v.isBigInt => BigIntFilter(filter, columnName, BigInt(v)).asInstanceOf[Filter[Any]]
          case _ if v.isLocalDate => LocalDateFilter(filter, columnName, DateTimeUtils.parse(v)).asInstanceOf[Filter[Any]]
          case _ => StringFilter(filter, columnName, v).asInstanceOf[Filter[Any]]
        }
      )
      case _ => None
    }
  }
}

sealed abstract class FilterKeyword(val keyword: String) extends LthvEnum(keyword)

object FilterKeyword {

  val filterKeywords: Set[String] = Set(
    EqualityFilter.keyword,
    GreaterThanFilter.keyword,
    LesserThanFilter.keyword,
    ContainsFilter.keyword
  )

  final case object EqualityFilter extends FilterKeyword("eq")
  final case object GreaterThanFilter extends FilterKeyword("gt")
  final case object LesserThanFilter extends FilterKeyword("lt")
  final case object ContainsFilter extends FilterKeyword("contains")
}
