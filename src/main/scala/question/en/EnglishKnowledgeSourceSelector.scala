package question.en

import m17n.English
import question.MultiLingualKnowledgeSourceSelector
import util.Config

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishKnowledgeSourceSelector extends MultiLingualKnowledgeSourceSelector with English {
  def select(isKeywordQuery: Boolean): Seq[String] = {
    if (isKeywordQuery) {
      Config.tokenLevelIndriIndicesInEnglish
    } else {
      Config.wordLevelIndriIndicesInEnglish
    }
  }
}
