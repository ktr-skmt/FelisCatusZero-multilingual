package uima.cc.ja

import exam.ja.JapaneseLengthCounter
import text.StringOption
import uima.ae.ja.JapaneseDocumentAnnotator
import uima.cc.MultiLingualEssayEvaluator

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseEssayEvaluator extends MultiLingualEssayEvaluator with JapaneseDocumentAnnotator {
  override protected def limit: String = {
    "CHAR"
  }

  override protected def count(textOpt: StringOption): Int = {
    JapaneseLengthCounter.count(textOpt)
  }
}