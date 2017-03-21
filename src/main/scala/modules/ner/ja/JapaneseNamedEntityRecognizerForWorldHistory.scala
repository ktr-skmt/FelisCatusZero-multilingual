package modules.ner.ja

import modules.ner.MultiLingualNamedEntityRecognizerForWorldHistory
import us.feliscat.m17n.Japanese
import us.feliscat.ner.ja.JapaneseNamedEntityRecognizerInEventOntology
import us.feliscat.ner.NamedEntity
import us.feliscat.text.StringOption

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
