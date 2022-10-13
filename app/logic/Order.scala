package logic

import logic.OrderDirection.Asc
import lthv.LthvEnum
import scalikejdbc.interpolation.SQLSyntax
import scalikejdbc.scalikejdbcSQLInterpolationImplicitDef

case class Order(orderDirection: OrderDirection, columnName: SQLSyntax) {
  def isAsc: Boolean = orderDirection.direction == Asc.direction

  def toSqlSyntax: SQLSyntax = {
    sqls"$columnName ${orderDirection.directionSql}"
  }
}

object Order {

  val filterSeparator = ":"
  val filterTag = "order"

  def apply(orderDefinition: String, columnName: SQLSyntax): Option[Order] = {
    orderDefinition.split(filterSeparator).toSeq match {
      case Seq(tag, orderDirection) if tag == filterTag => Some(Order(OrderDirection(orderDirection), columnName))
      case _ => None
    }
  }

  def orderByAscSql(columns: Seq[Order]): SQLSyntax = if (columns.isEmpty) {
    SQLSyntax.empty
  } else {
    val columnNames = columns.map(c => c.columnName)
    SQLSyntax.orderBy(columnNames: _*).asc
  }

  def orderByDescSql(columns: Seq[Order]): SQLSyntax = if (columns.isEmpty) {
    SQLSyntax.empty
  } else {
    val columnNames = columns.map(c => c.columnName)
    SQLSyntax.orderBy(columnNames: _*).desc
  }
}

sealed abstract class OrderDirection(val direction: String, val directionSql: SQLSyntax) extends LthvEnum(direction)

object OrderDirection {
  final case object Asc extends OrderDirection("asc", SQLSyntax.asc)
  final case object Desc extends OrderDirection("desc", SQLSyntax.desc)

  def apply(orderDirection: String): OrderDirection = {
    orderDirection match {
      case Asc.direction => Asc
      case Desc.direction => Desc
      case _ => sys.error(s"Improper order direction definition $orderDirection")
    }
  }
}
