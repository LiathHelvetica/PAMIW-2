package models

import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import scalikejdbc.DB
import scalikejdbc.ParameterBinder
import scalikejdbc.SQLSyntax
import scalikejdbc.SQLSyntaxSupport
import scalikejdbc.scalikejdbcSQLInterpolationImplicitDef
import scalikejdbc.{insert => dslInsert}
import scalikejdbc.{delete => dslDelete}
import scalikejdbc.{update => dslUpdate}
import scalikejdbc.{select => dslSelect}
import scalikejdbc.withSQL
import utils.IdGenerator.generateId

import java.time.LocalDate

case class Competitor(
  id: String,
  name: String,
  surname: String,
  country: String,
  birthDate: LocalDate,
  height: Int,
  weight: Int
) {
  def toJson(implicit formatter: OFormat[Competitor]): JsValue = {
    Json.toJson(this)
  }
}

case class CompetitorDTO(
  id: Option[String],
  name: Option[String],
  surname: Option[String],
  country: Option[String],
  birthDate: Option[LocalDate],
  height: Option[Int],
  weight: Option[Int]
)

object Competitor extends SQLSyntaxSupport[Competitor] {

  override val schemaName: Option[String] = Some("public")
  override val tableName: String = "competitors"

  def insert(competitor: CompetitorDTO): String = {
    val id = generateId
    DB.localTx {
      implicit session =>
        withSQL {
          dslInsert.into(Competitor).namedValues(
            column.id -> id,
            column.name -> competitor.name,
            column.surname -> competitor.surname,
            column.country -> competitor.country,
            column.birthDate -> competitor.birthDate,
            column.height -> competitor.height,
            column.weight -> competitor.weight
          )
        }.update().apply()
    }
    id
  }

  def delete(id: String): Unit = DB.localTx {
    implicit session => withSQL {
      dslDelete.from(Competitor).where.eq(column.id, id)
    }.update().apply()
  }

  def update(id: String, payload: Map[SQLSyntax, ParameterBinder]): Unit = DB.localTx {
    implicit session => withSQL {
      dslUpdate(Competitor).set(payload).where.eq(column.id, id)
    }.update().apply()
  }

  def select(
    filter: SQLSyntax,
    limit: Option[Int],
    offset: Option[Int],
    orderBySyntax: SQLSyntax
  ): Seq[Competitor] = DB.readOnly {
    val c = Competitor.syntax("c")

    implicit session => withSQL {
      dslSelect
        .from(Competitor as c)
        .where(filter)
        .append(orderBySyntax)
        .append(limit.map(v => sqls"limit $v").getOrElse(sqls""))
        .offset(offset.getOrElse(0))
    }.map(rs => {
      new Competitor(
        id = rs.string(c.resultName.id),
        name = rs.string(c.resultName.name),
        surname = rs.string(c.resultName.surname),
        country = rs.string(c.resultName.country),
        birthDate = rs.localDate(c.resultName.birthDate),
        height = rs.int(c.resultName.height),
        weight = rs.int(c.resultName.weight)
      )
    }).list().apply()
  }
}

