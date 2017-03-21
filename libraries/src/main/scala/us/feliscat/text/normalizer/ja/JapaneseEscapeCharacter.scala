package us.feliscat.text.normalizer.ja

import us.feliscat.m17n.Japanese
import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.EscapeObject

/**
  * @author K.Sakamoto
  *         Created on 2016/08/07
  */
object JapaneseEscapeCharacter extends EscapeObject(StringOption("escape_character.txt")) with Japanese
