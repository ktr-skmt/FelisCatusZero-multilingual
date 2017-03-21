package us.feliscat.text.normalizer.ja

import us.feliscat.text.StringOption

/**
 * @author K.Sakamoto
 *         Created on 15/10/28
 */
class JapaneseNormalizedCharacter(private var character: Char) {
  character = JapaneseNormalizer.normalize(StringOption(character.toString)).get.head

  def toChar: Char = {
    character
  }
}
