package controllers

import logic.Filter
import logic.Order
import logic.Order.orderByAscSql
import logic.Order.orderByDescSql
import models.Competitor
import models.CompetitorDTO
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import play.api.libs.json._
import scalikejdbc.SQLSyntax
import scalikejdbc.interpolation.SQLSyntax.csv
import scalikejdbc.scalikejdbcSQLInterpolationImplicitDef
import utils.NaiveParameterBinderUtils.valueToParameterBinder
import utils.UrlUtils.readQuery

import java.time.LocalDate.now
import javax.inject.Inject
import javax.inject.Singleton
import scala.util.Success
import scala.util.Try

@Singleton // only one instance - reuse for requests
// inject controller components
class CompetitorController @Inject()(controllerComponents: ControllerComponents)
  extends AbstractController(controllerComponents) {

  // omit Json.toJson calls
  implicit val competitorFormatter: OFormat[Competitor] = Json.format[Competitor]
  implicit val competitorDtoFormatter: OFormat[CompetitorDTO] = Json.format[CompetitorDTO]

  def testGetCompetitor: Action[AnyContent] = Action {
    Ok(Json.toJson(Competitor("a", "b", "c", "d", now(), 0, 0)))
  }

  def postCompetitor: Action[AnyContent] = Action {
    request => {
      val competitor = request.body.asJson.get.as[CompetitorDTO]
      val id = Competitor.insert(competitor)
      Created(id)
    }
  }

  def deleteCompetitor(id: String): Action[AnyContent] = Action {
    Competitor.delete(id)
    Ok
  }

  def putCompetitor(id: String): Action[AnyContent] = Action {
    request => {
      request.body.asJson match {
        // TODO: warn
        case jsonOpt: Some[JsObject] => {
          val json = jsonOpt.get
          val columnMapper = Competitor.column
          val payload = json.fields.map(field =>
            columnMapper.field(field._1) -> valueToParameterBinder(field._2)
          ).toMap
          Competitor.update(id, payload)
          Ok
        }
        case None => NoContent
        case _ => BadRequest
      }
    }
  }

  /**
   * queries: column=lt:10, column=order:asc, page=1, limit=1
   * */
  def getCompetitor: Action[AnyContent] = Action {
    request => {
      val requestQueries = request.queryString
      val columnMapper = Competitor.column

      val page: Option[Int] = readQuery(requestQueries, "page").map(v => v.toInt)
      val limit: Option[Int] = readQuery(requestQueries, "limit").map(v => v.toInt)
      val offset: Option[Int] = (page, limit) match {
        case (Some(v1), Some(v2)) => Some(v1 * v2)
        case _ => None
      }

      val orderPieces = requestQueries.flatMap(query => {
        Try { columnMapper.field(query._1) } match {
          case Success(columnName) => query._2.map(Order(_, columnName))
          case _ => Seq.empty
        }
      }).flatten.toSeq
      val orderBySyntax = if (orderPieces.isEmpty) {
        SQLSyntax.empty
      } else {
        val sqlParts = orderPieces.map(op => op.toSqlSyntax)
        sqls"order by ${csv(sqlParts: _*)}"
      }

      val filter: SQLSyntax = requestQueries.flatMap(query => {
        Try { columnMapper.field(query._1) } match {
          case Success(columnName) => query._2.map(Filter(_, columnName))
          case _ => Seq.empty
        }
      }).flatten.foldLeft(sqls"1 = 1")((acc, filter) => acc.and(filter.toSqlSyntax))

      val outcome = Competitor.select(
        filter,
        limit,
        offset,
        orderBySyntax
      )
      Ok(Json.toJson(outcome))
    }
  }

  /*
  val filter: SQLSyntax = columnMapper.columns.flatMap(column =>
    requestQueries.get(column.value) match {
      case Some(filterDefinitions) => Some(filterDefinitions.map(Filter(column, _)))
      case _ => None
    }
  ).flatten.foldLeft(sqls"1 = 1")((acc, filter) => filter match {
    case Some(filter) => acc.and(filter.toSqlSyntax)
    case _ => acc
  })
  * */
}