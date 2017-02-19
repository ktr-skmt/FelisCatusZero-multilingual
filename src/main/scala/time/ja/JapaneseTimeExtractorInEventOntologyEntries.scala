package time.ja

import m17n.Japanese
import ner.ja.JapaneseNamedEntityRecognizerInEventOntology
import text.StringOption
import time.{MultiLingualTimeExtractorInEventOntologyEntities, TimeTmp}

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseTimeExtractorInEventOntologyEntries extends MultiLingualTimeExtractorInEventOntologyEntities with Japanese {
  override def extract(text: StringOption): Seq[TimeTmp] = {
    JapaneseNamedEntityRecognizerInEventOntology.recognize(text).map {
      _.time
    }
  }
}
