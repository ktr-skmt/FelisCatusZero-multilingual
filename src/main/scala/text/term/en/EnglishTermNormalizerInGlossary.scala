package text.term.en

import m17n.English
import ner.NamedEntity
import ner.en.EnglishNamedEntityRecognizerInGlossary
import text.StringOption
import text.term.MultiLingualTermNormalizerInGlossary

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
