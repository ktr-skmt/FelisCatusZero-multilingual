package text.term.ja

import m17n.Japanese
import ner.NamedEntity
import ner.ja.JapaneseNamedEntityRecognizerInGlossary
import text.StringOption
import text.term.MultiLingualTermNormalizerInGlossary

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
