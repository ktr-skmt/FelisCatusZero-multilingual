package text.normalizer.en

import m17n.English
import text.StringOption
import text.normalizer.EscapeObject

/**
  * @author K.Sakamoto
  *         Created on 2016/08/07
  */
object EnglishEscapeCharacter extends EscapeObject(StringOption("escape_character.txt")) with English
