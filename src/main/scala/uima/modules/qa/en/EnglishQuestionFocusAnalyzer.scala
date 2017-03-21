package uima.modules.qa.en

import uima.modules.qa.MultiLingualQuestionFocusAnalyzer
import us.feliscat.m17n.English
import us.feliscat.types.Sentence

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishQuestionFocusAnalyzer extends MultiLingualQuestionFocusAnalyzer with English {
  override def analyze(sentenceSet: Seq[Sentence]): Seq[String] = {
    Nil
  }
}
