package text.term.ja

import m17n.Japanese
import text.StringOption
import text.term.MultiLingualTermNormalizerForWorldHistory

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseTermNormalizerForWorldHistory extends MultiLingualTermNormalizerForWorldHistory with Japanese {
  override def normalize(textOpt: StringOption): StringOption = {
    JapaneseTermNormalizerInGlossary.normalize(
    JapaneseTermNormalizerInEventOntology.normalize(textOpt))
  }
}
