package 
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends Tables {
  val profile = slick.jdbc.PostgresProfile
}

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Competitors.schema ++ Contracts.schema ++ Employees.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Competitors
   *  @param id Database column id SqlType(varchar), PrimaryKey
   *  @param name Database column name SqlType(varchar)
   *  @param surname Database column surname SqlType(varchar)
   *  @param country Database column country SqlType(varchar)
   *  @param birthDate Database column birth_date SqlType(date)
   *  @param height Database column height SqlType(int4)
   *  @param weight Database column weight SqlType(int4) */
  case class CompetitorsRow(id: String, name: String, surname: String, country: String, birthDate: java.sql.Date, height: Int, weight: Int)
  /** GetResult implicit for fetching CompetitorsRow objects using plain SQL queries */
  implicit def GetResultCompetitorsRow(implicit e0: GR[String], e1: GR[java.sql.Date], e2: GR[Int]): GR[CompetitorsRow] = GR{
    prs => import prs._
    CompetitorsRow.tupled((<<[String], <<[String], <<[String], <<[String], <<[java.sql.Date], <<[Int], <<[Int]))
  }
  /** Table description of table competitors. Objects of this class serve as prototypes for rows in queries. */
  class Competitors(_tableTag: Tag) extends profile.api.Table[CompetitorsRow](_tableTag, "competitors") {
    def * = (id, name, surname, country, birthDate, height, weight).<>(CompetitorsRow.tupled, CompetitorsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), Rep.Some(surname), Rep.Some(country), Rep.Some(birthDate), Rep.Some(height), Rep.Some(weight))).shaped.<>({r=>import r._; _1.map(_=> CompetitorsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey */
    val id: Rep[String] = column[String]("id", O.PrimaryKey)
    /** Database column name SqlType(varchar) */
    val name: Rep[String] = column[String]("name")
    /** Database column surname SqlType(varchar) */
    val surname: Rep[String] = column[String]("surname")
    /** Database column country SqlType(varchar) */
    val country: Rep[String] = column[String]("country")
    /** Database column birth_date SqlType(date) */
    val birthDate: Rep[java.sql.Date] = column[java.sql.Date]("birth_date")
    /** Database column height SqlType(int4) */
    val height: Rep[Int] = column[Int]("height")
    /** Database column weight SqlType(int4) */
    val weight: Rep[Int] = column[Int]("weight")
  }
  /** Collection-like TableQuery object for table Competitors */
  lazy val Competitors = new TableQuery(tag => new Competitors(tag))

  /** Entity class storing rows of table Contracts
   *  @param id Database column id SqlType(varchar), PrimaryKey
   *  @param contractorId Database column contractor_id SqlType(varchar)
   *  @param signingDate Database column signing_date SqlType(date)
   *  @param terminationDate Database column termination_date SqlType(date), Default(None) */
  case class ContractsRow(id: String, contractorId: String, signingDate: java.sql.Date, terminationDate: Option[java.sql.Date] = None)
  /** GetResult implicit for fetching ContractsRow objects using plain SQL queries */
  implicit def GetResultContractsRow(implicit e0: GR[String], e1: GR[java.sql.Date], e2: GR[Option[java.sql.Date]]): GR[ContractsRow] = GR{
    prs => import prs._
    ContractsRow.tupled((<<[String], <<[String], <<[java.sql.Date], <<?[java.sql.Date]))
  }
  /** Table description of table contracts. Objects of this class serve as prototypes for rows in queries. */
  class Contracts(_tableTag: Tag) extends profile.api.Table[ContractsRow](_tableTag, "contracts") {
    def * = (id, contractorId, signingDate, terminationDate).<>(ContractsRow.tupled, ContractsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(contractorId), Rep.Some(signingDate), terminationDate)).shaped.<>({r=>import r._; _1.map(_=> ContractsRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey */
    val id: Rep[String] = column[String]("id", O.PrimaryKey)
    /** Database column contractor_id SqlType(varchar) */
    val contractorId: Rep[String] = column[String]("contractor_id")
    /** Database column signing_date SqlType(date) */
    val signingDate: Rep[java.sql.Date] = column[java.sql.Date]("signing_date")
    /** Database column termination_date SqlType(date), Default(None) */
    val terminationDate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("termination_date", O.Default(None))

    /** Foreign key referencing Employees (database name contractor_FK) */
    lazy val employeesFk = foreignKey("contractor_FK", contractorId, Employees)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Contracts */
  lazy val Contracts = new TableQuery(tag => new Contracts(tag))

  /** Entity class storing rows of table Employees
   *  @param id Database column id SqlType(varchar), PrimaryKey
   *  @param age Database column age SqlType(int4)
   *  @param surname Database column surname SqlType(varchar)
   *  @param name Database column name SqlType(varchar) */
  case class EmployeesRow(id: String, age: Int, surname: String, name: String)
  /** GetResult implicit for fetching EmployeesRow objects using plain SQL queries */
  implicit def GetResultEmployeesRow(implicit e0: GR[String], e1: GR[Int]): GR[EmployeesRow] = GR{
    prs => import prs._
    EmployeesRow.tupled((<<[String], <<[Int], <<[String], <<[String]))
  }
  /** Table description of table employees. Objects of this class serve as prototypes for rows in queries. */
  class Employees(_tableTag: Tag) extends profile.api.Table[EmployeesRow](_tableTag, "employees") {
    def * = (id, age, surname, name).<>(EmployeesRow.tupled, EmployeesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(age), Rep.Some(surname), Rep.Some(name))).shaped.<>({r=>import r._; _1.map(_=> EmployeesRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey */
    val id: Rep[String] = column[String]("id", O.PrimaryKey)
    /** Database column age SqlType(int4) */
    val age: Rep[Int] = column[Int]("age")
    /** Database column surname SqlType(varchar) */
    val surname: Rep[String] = column[String]("surname")
    /** Database column name SqlType(varchar) */
    val name: Rep[String] = column[String]("name")
  }
  /** Collection-like TableQuery object for table Employees */
  lazy val Employees = new TableQuery(tag => new Employees(tag))
}
