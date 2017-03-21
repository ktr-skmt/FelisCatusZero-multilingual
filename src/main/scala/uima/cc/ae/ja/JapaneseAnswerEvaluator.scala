package uima.cc.ae.ja

import uima.cc.ae.MultiLingualAnswerEvaluator
import uima.modules.common.ja.JapaneseDocumentAnnotator
import us.feliscat.exam.ja.JapaneseLengthCounter
import us.feliscat.text.StringOption

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseAnswerEvaluator extends MultiLingualAnswerEvaluator with JapaneseDocumentAnnotator {
  override protected def limit: String = {
    "CHAR"
  }

  override protected def count(textOpt: StringOption): Int = {
    JapaneseLengthCounter.count(textOpt)
  }
}