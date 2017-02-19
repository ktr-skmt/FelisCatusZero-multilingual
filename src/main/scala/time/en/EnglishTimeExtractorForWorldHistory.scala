package time.en

import m17n.English
import text.StringOption
import time.{MultiLingualTimeExtractorForWorldHistory, TimeTmp}

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTimeExtractorForWorldHistory extends MultiLingualTimeExtractorForWorldHistory with English {
  override def extract(text: StringOption): Seq[TimeTmp] = {
    EnglishTimeExtractorInTimeExpression.extract(text).toList :::
    EnglishTimeExtractorInGlossaryEntries.extract(text).toList :::
    EnglishTimeExtractorInEventOntologyEntries.extract(text).toList
  }
}
