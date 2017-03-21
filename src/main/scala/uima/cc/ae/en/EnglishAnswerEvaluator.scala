package uima.cc.ae.en

import uima.cc.ae.MultiLingualAnswerEvaluator
import uima.modules.common.en.EnglishDocumentAnnotator
import us.feliscat.exam.en.EnglishLengthCounter
import us.feliscat.text.StringOption

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishAnswerEvaluator extends MultiLingualAnswerEvaluator with EnglishDocumentAnnotator {
  override protected def limit: String = {
    "WORD"
  }

  override protected def count(textOpt: StringOption): Int = {
    EnglishLengthCounter.count(textOpt)
  }
}