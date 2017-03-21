package modules.text.term.en

import modules.ner.en.EnglishNamedEntityRecognizerInGlossary
import modules.text.term.MultiLingualTermNormalizerInGlossary
import us.feliscat.m17n.English
import us.feliscat.ner.NamedEntity
import us.feliscat.text.StringOption

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTermNormalizerInGlossary extends MultiLingualTermNormalizerInGlossary with English {
  override def recognize(textOpt: StringOption): Seq[NamedEntity] = {
    EnglishNamedEntityRecognizerInGlossary.recognize(textOpt)
  }
}
