package question.ja

import m17n.Japanese
import question.MultiLingualKnowledgeSourceSelector
import util.Config

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseKnowledgeSourceSelector extends MultiLingualKnowledgeSourceSelector with Japanese {
  def select(isKeywordQuery: Boolean): Seq[String] = {
    if (isKeywordQuery) {
      Config.characterLevelIndriIndicesInJapanese
    } else {
      Config.wordLevelIndriIndicesInJapanese
    }
  }
}
