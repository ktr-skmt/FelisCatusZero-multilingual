package us.feliscat.time.ja

import us.feliscat.m17n.Japanese
import us.feliscat.ner.ja.JapaneseNamedEntityRecognizerInEventOntology
import us.feliscat.text.StringOption
import us.feliscat.time.{MultiLingualTimeExtractorInEventOntologyEntities, TimeTmp}

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
