package samplemodels

import slick.jdbc.PostgresProfile.api._

class Employee(tag: Tag) extends Table[(String, String, String, Int)](tag, "employees") {
  def id: Rep[String] = column[String]("id", O.PrimaryKey)
  def name: Rep[String] = column[String]("name")
  def surname: Rep[String] = column[String]("surname")
  def age: Rep[Int] = column[Int]("age")
  def * = (id, name, surname, age)
}

object Employee {
  val employees = TableQuery[Employee]
}
