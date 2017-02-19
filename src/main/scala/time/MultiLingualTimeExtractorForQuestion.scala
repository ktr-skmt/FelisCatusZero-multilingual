package time

import jeqa.types.{Sentence, Time}
import m17n.MultiLingual
import text.StringOption
import util.uima.StringListUtils._

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/08.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualTimeExtractorForQuestion extends MultiLingual {
  protected val verbsOfDescribing: Seq[String]

  protected def isQuestionSentence(sentence: Sentence): Boolean = {
    verbsOfDescribing foreach {
      case x if StringOption(sentence.getText).nonEmpty && (sentence.getText contains x) =>
        return true
      case _ =>
      // Do nothing
    }
    false
  }

  def extract(sentenceList: Seq[Sentence]): TimeTmp = {
    val timeBuffer = ListBuffer.empty[TimeTmp]
    sentenceList.reverse foreach {
      sentence: Sentence =>
        if (isQuestionSentence(sentence)) {
          val beginTimeOpt = Option[Time](sentence.getBeginTime)
          val endTimeOpt = Option[Time](sentence.getEndTime)
          timeBuffer += new TimeTmp(
            if (beginTimeOpt.nonEmpty) {
              Option(beginTimeOpt.get.getYear)
            } else {
              None
            }
            ,
            if (endTimeOpt.nonEmpty) {
              Option(endTimeOpt.get.getYear)
            } else {
              None
            }
            ,
            if (beginTimeOpt.nonEmpty) {
              beginTimeOpt.get.getTextList.toSeq
            } else {
              Nil
            }
            ,
            if (endTimeOpt.nonEmpty) {
              endTimeOpt.get.getTextList.toSeq
            } else {
              Nil
            }
          )
        }
    }
    TimeMerger.union(timeBuffer.result)
  }
}
