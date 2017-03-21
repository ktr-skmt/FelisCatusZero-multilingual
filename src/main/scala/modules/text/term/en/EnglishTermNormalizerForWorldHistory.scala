package modules.text.term.en

import modules.text.term.MultiLingualTermNormalizerForWorldHistory
import us.feliscat.m17n.English
import us.feliscat.text.StringOption
import us.feliscat.text.term.en.EnglishTermNormalizerInEventOntology

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
