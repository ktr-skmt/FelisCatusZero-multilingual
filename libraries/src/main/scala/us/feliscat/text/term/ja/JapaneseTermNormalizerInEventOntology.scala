package us.feliscat.text.term.ja

import us.feliscat.m17n.Japanese
import us.feliscat.ner.NamedEntity
import us.feliscat.ner.ja.JapaneseNamedEntityRecognizerInEventOntology
import us.feliscat.text.StringOption
import us.feliscat.text.term.MultiLingualTermNormalizerInEventOntology

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseTermNormalizerInEventOntology extends MultiLingualTermNormalizerInEventOntology with Japanese {
  override def recognize(textOpt: StringOption): Seq[NamedEntity] = {
    JapaneseNamedEntityRecognizerInEventOntology.recognize(textOpt)
  }
}
