package common

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

object DateUtils {

  lazy val dateFormat = new SimpleDateFormat("MM-dd-YYYY HH:mm:ss", Locale.ENGLISH)

  def dateToString(date: Date): String = {
    dateFormat.format(date)
  }

  def stringToDate(string: String): Date = {
    dateFormat.parse(string)
  }
}
