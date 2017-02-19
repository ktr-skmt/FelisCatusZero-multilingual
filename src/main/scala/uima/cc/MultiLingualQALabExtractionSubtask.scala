package uima.cc

import jeqa.types.{Question, TextAnnotation, Time}
import text.similarity.OverlapCalculator
import text.vector.BinaryVectorGenerator

/**
  * <pre>
  * Created on 2017/02/18.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualQALabExtractionSubtask extends MultiLingualQALabSubmission {
  protected def reviseText(docno: String, title: String, text: String): String

  protected def isGlossary(docno: String): Boolean = {
    docno.startsWith("YamakawaWorldHistoryGlossary-")
  }

  protected def sourceIdType(docno: String): String = {
    if (isGlossary(docno)) {
      teamId
    } else {
      "QALab3"
    }
  }

  protected def scoreForText(question: TextAnnotation, document: TextAnnotation): Double = {
    OverlapCalculator.calculate(
      BinaryVectorGenerator.getVectorFromAnnotation(question),
      BinaryVectorGenerator.getVectorFromAnnotation(document)
    )
  }

  protected def normalizeScoreWithBoW(score: Double): Double = {
    math.atan(score) * 2 / math.Pi
  }


  protected def need(question: Question, beginTimeOpt: Option[Time], endTimeOpt: Option[Time]): Boolean = {
    val beginTimeLimitOpt = Option[Time](question.getBeginTimeLimit)
    val endTimeLimitOpt   = Option[Time](question.getEndTimeLimit)
    val beginYearOpt: Option[Int] = beginTimeOpt.map(_.getYear)
    val endYearOpt:   Option[Int] = endTimeOpt.map(_.getYear)

    if (beginTimeLimitOpt.isEmpty && endTimeLimitOpt.isEmpty) {
      true
    } else if (beginTimeLimitOpt.nonEmpty) {
      val beginYearLimit: Int = beginTimeLimitOpt.get.getYear
      beginYearOpt match {
        case Some(beginYear) =>
          beginYearLimit <= beginYear
        case None =>
          endYearOpt match {
            case Some(endYear) =>
              beginYearLimit <= endYear
            case None =>
              true
          }
      }
    } else if (endTimeLimitOpt.nonEmpty) {
      val endYearLimit: Int = endTimeLimitOpt.get.getYear
      endYearOpt match {
        case Some(endYear) =>
          endYear <= endYearLimit
        case None =>
          beginYearOpt match {
            case Some(beginYear) =>
              beginYear <= endYearLimit
            case None =>
              true
          }
      }
    } else {
      val beginYearLimit: Int = beginTimeLimitOpt.get.getYear
      val endYearLimit:   Int = endTimeLimitOpt.get.getYear
      if (beginYearOpt.isEmpty && endYearOpt.isEmpty) {
        true
      } else if (beginYearOpt.nonEmpty) {
        val beginYear: Int = beginYearOpt.get
        beginYearLimit <= beginYear
      } else if (endYearOpt.nonEmpty) {
        val endYear: Int = endYearOpt.get
        endYear <= endYearLimit
      } else {
        val beginYear: Int = beginYearOpt.get
        val endYear: Int = endYearOpt.get
        beginYearLimit <= beginYear && endYear <= endYearLimit
      }
    }
  }
}
