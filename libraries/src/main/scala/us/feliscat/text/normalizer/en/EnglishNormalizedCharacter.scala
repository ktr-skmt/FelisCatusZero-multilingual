package us.feliscat.text.normalizer.en

import us.feliscat.text.StringOption

/**
 * @author K.Sakamoto
 *         Created on 15/10/28
 */
class EnglishNormalizedCharacter(private var character: Char) {
  character = EnglishNormalizer.normalize(StringOption(character.toString)).get.head

  def toChar: Char = {
    character
  }
}
