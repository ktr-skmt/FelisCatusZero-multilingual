package us.feliscat.time.en

import us.feliscat.m17n.English
import us.feliscat.ner.en.EnglishNamedEntityRecognizerInEventOntology
import us.feliscat.text.StringOption
import us.feliscat.time.{MultiLingualTimeExtractorInEventOntologyEntities, TimeTmp}

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
