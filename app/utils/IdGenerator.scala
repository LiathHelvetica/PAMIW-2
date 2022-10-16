package utils

import java.lang.System.currentTimeMillis
import scala.util.Random

object IdGenerator {
  val RNG = new Random()
  val stringPartPrecision = 16

  def generateId: String = {
    val time: String = currentTimeMillis().toString
    val random: String = RNG.alphanumeric.take(stringPartPrecision).mkString
    random + time
  }
}
