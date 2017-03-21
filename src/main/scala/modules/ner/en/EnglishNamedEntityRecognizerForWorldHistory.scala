package modules.ner.en

import modules.ner.MultiLingualNamedEntityRecognizerForWorldHistory
import us.feliscat.m17n.English
import us.feliscat.ner.en.EnglishNamedEntityRecognizerInEventOntology
import us.feliscat.ner.NamedEntity
import us.feliscat.text.StringOption

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
