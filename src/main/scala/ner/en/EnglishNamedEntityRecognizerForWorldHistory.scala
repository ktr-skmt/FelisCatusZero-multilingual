package ner.en

import m17n.English
import ner.{MultiLingualNamedEntityRecognizerForWorldHistory, NamedEntity}
import text.StringOption

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishNamedEntityRecognizerForWorldHistory extends MultiLingualNamedEntityRecognizerForWorldHistory with English {
  override def recognize(textOpt: StringOption): Seq[NamedEntity] = {
    EnglishNamedEntityRecognizerInGlossary.recognize(textOpt).toList :::
    EnglishNamedEntityRecognizerInEventOntology.recognize(textOpt).toList
  }
}
