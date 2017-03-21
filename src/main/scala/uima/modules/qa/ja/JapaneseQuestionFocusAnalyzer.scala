package uima.modules.qa.ja

import uima.modules.qa.MultiLingualQuestionFocusAnalyzer
import us.feliscat.m17n.Japanese
import us.feliscat.types.Sentence

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseQuestionFocusAnalyzer extends MultiLingualQuestionFocusAnalyzer with Japanese {
  override def analyze(sentenceSet: Seq[Sentence]): Seq[String] = {
    Nil
  }
}
