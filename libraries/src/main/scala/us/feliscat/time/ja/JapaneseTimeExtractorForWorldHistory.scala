package us.feliscat.time.ja

import us.feliscat.m17n.Japanese
import us.feliscat.text.StringOption
import us.feliscat.time.{MultiLingualTimeExtractorForWorldHistory, TimeTmp}

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
