package text.term.en

import m17n.English
import text.StringOption
import text.term.MultiLingualTermNormalizerForWorldHistory

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTermNormalizerForWorldHistory extends MultiLingualTermNormalizerForWorldHistory with English {
  override def normalize(textOpt: StringOption): StringOption = {
    EnglishTermNormalizerInGlossary.normalize(
    EnglishTermNormalizerInEventOntology.normalize(textOpt))
  }
}
