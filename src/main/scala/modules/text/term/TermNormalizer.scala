package modules.text.term

import us.feliscat.ner.NamedEntity
import us.feliscat.text.StringOption

/**
  * @author K.Sakamoto
  *         Created on 2015/11/27
  */
trait TermNormalizer {
  def recognize(textOpt: StringOption): Seq[NamedEntity]
}
