package samplemodels

import samplemodels.Employee.employees
import slick.jdbc.PostgresProfile.api._

import java.time.LocalDate

class Contract(tag: Tag) extends Table[(String, String, LocalDate, Option[LocalDate])](tag, "contracts") {
  def id: Rep[String] = column[String]("id", O.PrimaryKey)
  def contractorId: Rep[String] = column[String]("contractor_id")
  def signingDate: Rep[LocalDate] = column[LocalDate]("signing_date")
  def terminationDate: Rep[Option[LocalDate]] = column[Option[LocalDate]]("termination_date")
  def * = (id, contractorId, signingDate, terminationDate)

  def contractorFk = foreignKey("contractor_FK", contractorId, employees)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
}

object Contract {
  val contracts = TableQuery[Contract]
}