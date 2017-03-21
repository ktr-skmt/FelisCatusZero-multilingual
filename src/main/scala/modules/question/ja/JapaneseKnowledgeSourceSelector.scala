package modules.question.ja

import modules.question.MultiLingualKnowledgeSourceSelector
import modules.util.ModulesConfig
import us.feliscat.m17n.Japanese

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
      ModulesConfig.characterLevelIndriIndicesInJapanese
    } else {
      ModulesConfig.wordLevelIndriIndicesInJapanese
    }
  }
}
