package modules.question.en

import modules.question.MultiLingualKnowledgeSourceSelector
import modules.util.ModulesConfig
import us.feliscat.m17n.English

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
      ModulesConfig.tokenLevelIndriIndicesInEnglish
    } else {
      ModulesConfig.wordLevelIndriIndicesInEnglish
    }
  }
}
