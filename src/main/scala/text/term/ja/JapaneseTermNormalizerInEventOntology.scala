package text.term.ja

import m17n.Japanese
import ner.NamedEntity
import ner.ja.JapaneseNamedEntityRecognizerInEventOntology
import text.StringOption
import text.term.MultiLingualTermNormalizerInEventOntology

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
