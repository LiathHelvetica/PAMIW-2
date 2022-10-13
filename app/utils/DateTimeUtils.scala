package utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateTimeUtils {
  val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  def parse(s: String): LocalDate = {
    LocalDate.parse(s, dateFormatter)
  }
}
