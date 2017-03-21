package modules.text.term.ja

import modules.ner.ja.JapaneseNamedEntityRecognizerInGlossary
import modules.text.term.MultiLingualTermNormalizerInGlossary
import us.feliscat.m17n.Japanese
import us.feliscat.ner.NamedEntity
import us.feliscat.text.StringOption

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseTermNormalizerInGlossary extends MultiLingualTermNormalizerInGlossary with Japanese {
  override def recognize(textOpt: StringOption): Seq[NamedEntity] = {
    JapaneseNamedEntityRecognizerInGlossary.recognize(textOpt)
  }
}
