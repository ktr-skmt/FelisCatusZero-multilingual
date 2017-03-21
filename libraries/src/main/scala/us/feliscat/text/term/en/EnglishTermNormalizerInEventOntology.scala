package us.feliscat.text.term.en

import us.feliscat.m17n.English
import us.feliscat.ner.NamedEntity
import us.feliscat.ner.en.EnglishNamedEntityRecognizerInEventOntology
import us.feliscat.text.StringOption
import us.feliscat.text.term.MultiLingualTermNormalizerInEventOntology

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
