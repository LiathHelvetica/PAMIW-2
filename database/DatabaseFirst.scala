import slick.codegen.SourceCodeGenerator.{main => generateCode}

import Constants.jdbcDriver
import Constants.outputFolder
import Constants.packageName
import Constants.password
import Constants.profile
import Constants.uri
import Constants.username

object DatabaseFirst extends App {
  generateCode(
    Array(profile, jdbcDriver, uri, outputFolder, packageName, username, password)
  )
}
