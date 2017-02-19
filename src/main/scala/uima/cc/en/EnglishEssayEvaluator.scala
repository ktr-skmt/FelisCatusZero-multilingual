package uima.cc.en

import exam.en.EnglishLengthCounter
import text.StringOption
import uima.ae.en.EnglishDocumentAnnotator
import uima.cc.MultiLingualEssayEvaluator

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishEssayEvaluator extends MultiLingualEssayEvaluator with EnglishDocumentAnnotator {
  override protected def limit: String = {
    "WORD"
  }

  override protected def count(textOpt: StringOption): Int = {
    EnglishLengthCounter.count(textOpt)
  }
}