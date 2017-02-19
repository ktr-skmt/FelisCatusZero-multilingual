package text.term.en

import m17n.English
import ner.NamedEntity
import ner.en.EnglishNamedEntityRecognizerInEventOntology
import text.StringOption
import text.term.MultiLingualTermNormalizerInEventOntology

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTermNormalizerInEventOntology extends MultiLingualTermNormalizerInEventOntology with English {
  override def recognize(textOpt: StringOption): Seq[NamedEntity] = {
    EnglishNamedEntityRecognizerInEventOntology.recognize(textOpt)
  }
}
