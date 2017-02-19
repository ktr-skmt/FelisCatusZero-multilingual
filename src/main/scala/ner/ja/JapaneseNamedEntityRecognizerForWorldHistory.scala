package ner.ja

import m17n.Japanese
import ner.{MultiLingualNamedEntityRecognizerForWorldHistory, NamedEntity}
import text.StringOption

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseNamedEntityRecognizerForWorldHistory extends MultiLingualNamedEntityRecognizerForWorldHistory with Japanese {
  override def recognize(textOpt: StringOption): Seq[NamedEntity] = {
    JapaneseNamedEntityRecognizerInGlossary.recognize(textOpt).toList :::
    JapaneseNamedEntityRecognizerInEventOntology.recognize(textOpt).toList
  }
}
