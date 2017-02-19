package time.en

import m17n.English
import ner.en.EnglishNamedEntityRecognizerInEventOntology
import text.StringOption
import time.{MultiLingualTimeExtractorInEventOntologyEntities, TimeTmp}

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTimeExtractorInEventOntologyEntries extends MultiLingualTimeExtractorInEventOntologyEntities with English {
  override def extract(text: StringOption): Seq[TimeTmp] = {
    EnglishNamedEntityRecognizerInEventOntology.recognize(text).map {
      _.time
    }
  }
}
