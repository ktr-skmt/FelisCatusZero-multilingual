package time.ja

import m17n.Japanese
import text.StringOption
import time.{MultiLingualTimeExtractorForWorldHistory, TimeTmp}

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseTimeExtractorForWorldHistory extends MultiLingualTimeExtractorForWorldHistory with Japanese {
  override def extract(text: StringOption): Seq[TimeTmp] = {
    JapaneseTimeExtractorInTimeExpression.extract(text).toList :::
    JapaneseTimeExtractorInGlossaryEntries.extract(text).toList :::
    JapaneseTimeExtractorInEventOntologyEntries.extract(text).toList
  }
}
