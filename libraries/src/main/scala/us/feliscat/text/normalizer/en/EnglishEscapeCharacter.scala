package us.feliscat.text.normalizer.en

import us.feliscat.m17n.English
import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.EscapeObject

/**
  * @author K.Sakamoto
  *         Created on 2016/08/07
  */
object EnglishEscapeCharacter extends EscapeObject(StringOption("escape_character.txt")) with English
