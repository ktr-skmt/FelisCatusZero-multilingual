package modules.text.term.ja

import modules.text.term.MultiLingualTermNormalizerForWorldHistory
import us.feliscat.m17n.Japanese
import us.feliscat.text.StringOption
import us.feliscat.text.term.ja.JapaneseTermNormalizerInEventOntology

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
