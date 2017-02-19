package time.en

import m17n.English
import text.{StringNone, StringOption, StringSome}
import time.{MultiLingualTimeExtractorInTimeExpression, TimeTmp}

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTimeExtractorInTimeExpression extends MultiLingualTimeExtractorInTimeExpression with English {
  override def extract(sentence: StringOption): Seq[TimeTmp] = {
    if (sentence.isEmpty) {
      return Nil
    }
    /** in the 18th century
      * from the 16th to the 18th century
      * during the period from the 1850s to the 1870s
      * from the 17th century to the 19th century
      * up to the 13th century
      * up to and including the first half of the 18th century
      * from around the 11th century through the 19th century
      * up to the 1950s
      * from the mid-19th century through the early 20th century
      */
    //TODO: support for B.C.
    val bc: String = """( BCE?| B.C.(?:E\\.)?)?"""
    val prefix: String = """(around |mid-|middle |early |late |the first half of |the last half of )?"""
    val century1: String = s"""the $prefix(\\d{1,2})(?:th|st|nd|rd) (?:century|c\\.)$bc"""
    val century2: String = s"""the $prefix(\\d{1,2})(?:th|st|nd|rd)(?: century| c\\.)?$bc"""
    val years: String   = s"""the $prefix(\\d{1,3}0)s$bc"""
    val year: String    = s"""$prefix(\\d{1,4})$bc,? (?!words)(?!English words)"""

    // in
    val rIn: String = "[Ii]n %s"

    // from
    val rFrom: String = """from %s"""

    // to
    val rTo: String = """(?:to|[Uu]p to|through)( and including) %s"""

    // from to
    val rFromTo: String = s"""(?:during the period)? $rFrom $rTo"""

    //var time = Seq.empty[String]

    val rInCentury: Regex = rIn.format(century1).r
    val rInYears:   Regex = rIn.format(years).r
    val rInYear:    Regex = rIn.format(year).r

    val rFromToCentury: Regex = rFromTo.format(century1, century2).r
    val rFromToYears:   Regex = rFromTo.format(years, years).r
    val rFromToYear:    Regex = rFromTo.format(year, year).r

    val rFromCentury: Regex = rFrom.format(century1).r
    val rFromYears:   Regex = rFrom.format(years).r
    val rFromYear:    Regex = rFrom.format(year).r

    val rToCentury: Regex = rTo.format(century1).r
    val rToYears:   Regex = rTo.format(years).r
    val rToYear:    Regex = rTo.format(year).r

    val text: String = sentence.get
    val buffer = ListBuffer.empty[TimeTmp]
    buffer ++= extractTime_inCentury(text, rInCentury)
    buffer ++= extractTime_inYears(text, rInYears)
    buffer ++= extractTime_inYear(text, rInYear)
    buffer ++= extractTime_fromToCentury(text, rFromToCentury)
    buffer ++= extractTime_fromToYears(text, rFromToYears)
    buffer ++= extractTime_fromToYear(text, rFromToYear)
    buffer ++= extractTime_fromCentury(text, rFromCentury)
    buffer ++= extractTime_fromYears(text, rFromYears)
    buffer ++= extractTime_fromYear(text, rFromYear)
    buffer ++= extractTime_toCentury(text, rToCentury)
    buffer ++= extractTime_toYears(text, rToYears)
    buffer ++= extractTime_toYear(text, rToYear)
    buffer.result
  }

  private val approximateRate: Float = 0.3F

  private def isThatBC(that: String): Boolean = {
    StringOption(that) match {
      case StringSome(bc) if that.contains("BC") || that.contains("B.C.") =>
        true
      case _ =>
        false
    }
  }

  private def convertYear(year: Int, isBC: Boolean): Int = {
    if (isBC) {
      year * -1 + 1
    } else {
      year
    }
  }

  private def getIsIncluding(str: String): Boolean = {
    StringOption(str.trim) match {
      case StringSome(including) if including.startsWith("including") =>
        true
      case _ =>
        false
    }
  }

  private def century(year: Int, isBC: Boolean, prefix: String): (Option[Int], Option[Int]) = {
    val y: Int = convertYear(year, isBC)
    prefix match {
      case "around" =>
        (
          Option((y - 1) *  100 +  1 - (100 * approximateRate).toInt),
          Option( y      *  100      + (100 * approximateRate).toInt)
        )
      case "mid-" | "middle" =>
        (
          Option((y - 1) *  100 + 51 - (100 * approximateRate).toInt),
          Option((y - 1) *  100 + 50 + (100 * approximateRate).toInt)
        )
      case "early" =>
        (
          Option((y - 1) *  100 +  1),
          Option((y - 1) *  100      + (100 * approximateRate).toInt)
        )
      case "late" =>
        (
          Option( y      *  100 +  1 - (100 * approximateRate).toInt),
          Option( y      *  100)
        )
      case "the first half of" =>
        (
          Option((y - 1) *  100 +  1),
          Option((y - 1) *  100 + 50)
        )
      case "the last half of" =>
        (
          Option( y      *  100 - 49),
          Option( y      *  100)
        )
      case _ =>
        (
          Option((y - 1) *  100 +  1),
          Option( y      *  100)
        )
    }
  }

  private def years(year: Int, isBC: Boolean, prefix: String): (Option[Int], Option[Int]) = {
    val y: Int = convertYear(year, isBC)
    prefix match {
      case "around" =>
        (
          Option(y      - (10 * approximateRate).toInt),
          Option(y +  9 + (10 * approximateRate).toInt)
        )
      case "mid-" | "middle" =>
        (
          Option(y +  5 - (10 * approximateRate).toInt),
          Option(y + 10 - (10 * approximateRate).toInt)
        )
      case "early" =>
        (
          Option(y),
          Option(y -  1 + (10 * approximateRate).toInt)
        )
      case "late" =>
        (
          Option(y +  1 - (10 * approximateRate).toInt),
          Option(y +  9)
        )
      case "the first half of" =>
        (
          Option(y),
          Option(y +  4)
        )
      case "the last half of" =>
        (
          Option(y +  5),
          Option(y +  9)
        )
      case _ =>
        (
          Option(y),
          Option(y +  9)
        )
    }
  }

  private def year(year: Int, isBC: Boolean, prefix: String, isIncluding: Boolean): (Option[Int], Option[Int]) = {
    val y: Int = convertYear(year, isBC)
    prefix match {
      case "around" =>
        (
          Option(y - (10 * approximateRate).toInt),
          Option(y + (10 * approximateRate).toInt)
        )
      case "mid-" | "middle" | "early" | "late" | "the first half of" | "the last half of" | _ =>
        if (isIncluding) {
          (
            Option(y),
            Option(y)
          )
        } else {
          (
            None,
            Option(y - 1)
          )
        }
    }
  }

  private def extractTime_inCentury(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)
      val p: String = StringOption(m.group(1)).getOrElse("").trim
      val isBC: Boolean = isThatBC(m.group(3))
      toIntOpt(StringOption(m.group(2))) match {
        case Some(year) =>
          val (beginYearOpt, endYearOpt) = century(year, isBC, p)
          buffer += new TimeTmp(
            beginYearOpt,
            endYearOpt,
            Seq[String](str),
            Seq[String](str))
        case None =>
        // Do nothing
      }
      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def extractTime_inYears(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)
      val p: String = StringOption(m.group(1)).getOrElse("").trim
      val isBC: Boolean = isThatBC(m.group(3))
      toIntOpt(StringOption(m.group(2))) match {
        case Some(y) =>
          val (beginYearOpt, endYearOpt) = years(y, isBC, p)
          buffer += new TimeTmp(
            beginYearOpt,
            endYearOpt,
            Seq[String](str),
            Seq[String](str))
        case None =>
        // Do nothing
      }
      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def extractTime_inYear(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)
      val p: String = StringOption(m.group(1)).getOrElse("").trim
      val isBC: Boolean = isThatBC(m.group(3))
      toIntOpt(StringOption(m.group(2))) match {
        case Some(y) =>
          val (beginYearOpt, endYearOpt) = year(y, isBC, p, isIncluding = true)
          buffer += new TimeTmp(
            beginYearOpt,
            endYearOpt,
            Seq[String](str),
            Seq[String](str))
        case None =>
        // Do nothing
      }
      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def extractTime_fromToCentury(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)

      var beginYearOpt = Option.empty[Int]
      var endYearOpt   = Option.empty[Int]

      val isBC_begin: Boolean = isThatBC(m.group(3))
      val p_begin: String = StringOption(m.group(1)).getOrElse("").trim
      toIntOpt(StringOption(m.group(2))) match {
        case Some(y) =>
          beginYearOpt = century(y, isBC_begin, p_begin)._1
        case None =>
        // Do nothing
      }

      val isBC_end: Boolean = isThatBC(m.group(7))
      val p_end: String = StringOption(m.group(5)).getOrElse("").trim
      toIntOpt(StringOption(m.group(6))) match {
        case Some(y) =>
          endYearOpt = century(y, isBC_end, p_end)._2
        case None =>
        // Do nothing
      }

      buffer += new TimeTmp(
        beginYearOpt,
        endYearOpt,
        Seq[String](str),
        Seq[String](str))
      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def extractTime_fromToYears(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)

      var beginYearOpt = Option.empty[Int]
      var endYearOpt   = Option.empty[Int]

      val isBC_begin: Boolean = isThatBC(m.group(3))
      val p_begin: String = StringOption(m.group(1)).getOrElse("").trim
      toIntOpt(StringOption(m.group(2))) match {
        case Some(y) =>
          beginYearOpt = years(y, isBC_begin, p_begin)._1
        case None =>
        // Do nothing
      }

      val isBC_end: Boolean = isThatBC(m.group(7))
      val p_end: String = StringOption(m.group(5)).getOrElse("").trim
      toIntOpt(StringOption(m.group(6))) match {
        case Some(y) =>
          endYearOpt = years(y, isBC_end, p_end)._2
        case None =>
        // Do nothing
      }

      buffer += new TimeTmp(
        beginYearOpt,
        endYearOpt,
        Seq[String](str),
        Seq[String](str))
      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def extractTime_fromToYear(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)
      var beginYearOpt = Option.empty[Int]
      var endYearOpt   = Option.empty[Int]

      val isBC_begin: Boolean = isThatBC(m.group(3))
      val p_begin: String = StringOption(m.group(1)).getOrElse("").trim
      toIntOpt(StringOption(m.group(2))) match {
        case Some(y) =>
          beginYearOpt = year(y, isBC_begin, p_begin, isIncluding = true)._1
        case None =>
        // Do nothing
      }
      val isIncluding: Boolean = getIsIncluding(m.group(4))
      val isBC_end: Boolean = isThatBC(m.group(7))
      val p_end: String = StringOption(m.group(5)).getOrElse("").trim
      toIntOpt(StringOption(m.group(6))) match {
        case Some(y) =>
          endYearOpt = year(y, isBC_end, p_end, isIncluding)._2
        case None =>
        // Do nothing
      }
      buffer += new TimeTmp(
        beginYearOpt,
        endYearOpt,
        Seq[String](str),
        Seq[String](str))
      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def extractTime_fromCentury(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)
      val isBC: Boolean = isThatBC(m.group(3))
      val p: String = StringOption(m.group(1)).getOrElse("").trim
      toIntOpt(StringOption(m.group(2))) match {
        case Some(y) =>
          val beginYearOpt: Option[Int] = century(y, isBC, p)._1
          buffer += new TimeTmp(
            beginYearOpt,
            Option.empty[Int],
            Seq[String](str),
            Seq.empty[String])
        case None =>
        // Do nothing
      }
      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def extractTime_fromYears(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)
      val isBC: Boolean = isThatBC(m.group(3))
      val p: String = StringOption(m.group(1)).getOrElse("").trim
      toIntOpt(StringOption(m.group(2))) match {
        case Some(y) =>
          val beginYearOpt: Option[Int] = years(y, isBC, p)._1
          buffer += new TimeTmp(
            beginYearOpt,
            Option.empty[Int],
            Seq[String](str),
            Seq.empty[String])
        case None =>
        // Do nothing
      }
      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def extractTime_fromYear(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)
      val isBC: Boolean = isThatBC(m.group(3))
      val p: String = StringOption(m.group(1)).getOrElse("").trim
      toIntOpt(StringOption(m.group(2))) match {
        case Some(y) =>
          val beginYearOpt: Option[Int] = year(y, isBC, p, isIncluding = true)._1
          buffer += new TimeTmp(
            beginYearOpt,
            Option.empty[Int],
            Seq[String](str),
            Seq.empty[String]
          )
        case None =>
        // Do nothing
      }
      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def extractTime_toCentury(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)
      val isBC: Boolean = isThatBC(m.group(4))
      val p: String = StringOption(m.group(2)).getOrElse("").trim
      toIntOpt(StringOption(m.group(3))) match {
        case Some(y) =>
          val endYearOpt: Option[Int] = century(y, isBC, p)._2
          buffer += new TimeTmp(
            Option.empty[Int],
            endYearOpt,
            Seq.empty[String],
            Seq[String](str))
        case None =>
        // Do nothing
      }

      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def extractTime_toYears(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)
      val isBC: Boolean = isThatBC(m.group(4))
      val p: String = StringOption(m.group(2)).getOrElse("").trim
      toIntOpt(StringOption(m.group(3))) match {
        case Some(y) =>
          val endYearOpt: Option[Int] = years(y, isBC, p)._2
          buffer += new TimeTmp(
            Option.empty[Int],
            endYearOpt,
            Seq.empty[String],
            Seq[String](str)
          )
        case None =>
        // Do nothing
      }
      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def extractTime_toYear(text: String, regex: Regex): Seq[TimeTmp] = {
    val buffer = ListBuffer.empty[TimeTmp]
    var currentText: String = text
    var matchOpt: Option[Regex.Match] = regex.findFirstMatchIn(currentText)
    while (matchOpt.nonEmpty) {
      val m: Regex.Match = matchOpt.get
      val str: String = currentText.substring(m.start, m.end)
      val isIncluding: Boolean = getIsIncluding(m.group(1))

      val isBC: Boolean = isThatBC(m.group(4))
      val p: String = StringOption(m.group(2)).getOrElse("").trim
      toIntOpt(StringOption(m.group(3))) match {
        case Some(y) =>
          val endYearOpt: Option[Int] = year(y, isBC, p, isIncluding)._2
          buffer += new TimeTmp(
            Option.empty[Int],
            endYearOpt,
            Seq.empty[String],
            Seq[String](str))
        case None =>
        // Do nothing
      }
      currentText = currentText.substring(m.end)
      matchOpt = regex.findFirstMatchIn(currentText)
    }
    buffer.result
  }

  private def toIntOpt(digitsOpt: StringOption): Option[Int] = {
    digitsOpt match {
      case StringSome(digits) =>
        try {
          Option(digits.toInt)
        } catch {
          case e: NumberFormatException =>
            e.printStackTrace()
            None
        }
      case StringNone =>
        None
    }
  }
}
