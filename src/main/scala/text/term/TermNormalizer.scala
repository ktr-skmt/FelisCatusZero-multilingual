package text.term

import ner.NamedEntity
import text.StringOption

/**
  * @author K.Sakamoto
  *         Created on 2015/11/27
  */
trait TermNormalizer {
  def recognize(textOpt: StringOption): Seq[NamedEntity]
}
