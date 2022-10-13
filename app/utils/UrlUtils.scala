package utils

object UrlUtils {

  def readQuery(queries: Map[String, Seq[String]], key: String): Option[String] = {
    queries.get(key).flatMap(queries => queries.headOption)
  }
}
