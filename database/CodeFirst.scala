import Constants.jdbcDriver
import Constants.password
import Constants.uri
import Constants.username
import samplemodels.Contract.contracts
import samplemodels.Employee.employees
import slick.jdbc.PostgresProfile.api._
import slick.migration.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Failure
import scala.util.Success

object CodeFirst extends App {
  val db = Database.forURL(uri, username, password, driver = jdbcDriver)
  implicit val dialect: PostgresDialect = new PostgresDialect

  val migration =
    TableMigration(employees)
      .create
      .addColumns(_.id, _.age, _.surname, _.name) &
    TableMigration(contracts)
      .create
      .addColumns(_.id, _.contractorId, _.signingDate, _.terminationDate)
      .addForeignKeys(_.contractorFk)

  Await.ready(
    db.run(migration()),
    Duration.Inf
  ).value.get match {
    case Success(_) => println("Successful migration")
    case Failure(t) => {
      println("ERROR during migration")
      t.printStackTrace()
    }
  }
}
