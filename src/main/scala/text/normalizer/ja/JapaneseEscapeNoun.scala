package text.normalizer.ja

import m17n.Japanese
import text.StringOption
import text.normalizer.EscapeObject

/**
  * @author K.Sakamoto
  *         Created on 2016/08/07
  */
object JapaneseEscapeNoun extends EscapeObject(StringOption("escape_noun.txt")) with Japanese
