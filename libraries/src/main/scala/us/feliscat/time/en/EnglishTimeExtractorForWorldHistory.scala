package us.feliscat.time.en

import us.feliscat.m17n.English
import us.feliscat.text.StringOption
import us.feliscat.time.{MultiLingualTimeExtractorForWorldHistory, TimeTmp}

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
