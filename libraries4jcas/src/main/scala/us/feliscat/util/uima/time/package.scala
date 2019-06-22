package us.feliscat.util.uima

import us.feliscat.types.Time

/**
  * @author K. Sakamoto
  *         Created on 2017/05/25
  */
package object time {
  implicit class TimeUtils(val repr: Time) extends AnyVal {
    def toYearOpt: Option[Int] = {
      Option(repr) map {
        time => time.getYear
      }
    }
  }
}
