package us.feliscat.converter

import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.ja.{JapaneseNormalizedCharacter, JapaneseNormalizedString}

/**
 * <pre>
 * Created on 2014/11/24
 * </pre>
 * @author K.Sakamoto
 */
object CharacterConverter {
  def convertCharacters(str: JapaneseNormalizedString,
                        conversions: Seq[Conversion]): StringOption = {
    str.toStringOption map {
      nStr: String =>
        val chars: Array[Char] = nStr.toCharArray
        conversions foreach {
          conversion: Conversion =>
            val diff = conversion.diff
            for (i <- chars.indices) {
              val character: Char = chars(i)
              if (conversion.isInRange(character)) {
                chars(i) = (character + diff).toChar
              }
            }
        }
      new String(chars)
    }
  }

  def convertCharacter(normalizedCharacter: JapaneseNormalizedCharacter,
                       conversions: Seq[Conversion]): Char = {
    var character: Char = normalizedCharacter.toChar
    conversions foreach {
      conversion: Conversion =>
        if (conversion.isInRange(character)) {
          character = (character + conversion.diff).toChar
        }
    }
    character
  }

  private def getConversions(conversions: Conversions): Seq[Conversion] = {
    conversions.seq
  }

  def convertCharacters(str: JapaneseNormalizedString,
                        conversions: Conversions): StringOption = {
    CharacterConverter.convertCharacters(str,
      getConversions(conversions))
  }

  def convertCharacter(normalizedCharacter: JapaneseNormalizedCharacter, conversions: Conversions): Char = {
    CharacterConverter.convertCharacter(normalizedCharacter,
      getConversions(conversions))
  }

  def convertKatakanaToHiragana(str: JapaneseNormalizedString): StringOption = {
    CharacterConverter.convertCharacters(str,
      Conversions.FROM_KATAKANA_TO_HIRAGANA)
  }

  def convertKatakanaToHiragana(normalizedCharacter: JapaneseNormalizedCharacter): Char = {
    CharacterConverter.convertCharacter(normalizedCharacter,
      Conversions.FROM_KATAKANA_TO_HIRAGANA)
  }

  def convertHiraganaToKatakana(str: JapaneseNormalizedString): StringOption = {
    CharacterConverter.convertCharacters(str,
      Conversions.FROM_HIRAGANA_TO_KATAKANA)
  }

  def convertHiraganaToKatakana(normalizedCharacter: JapaneseNormalizedCharacter): Char = {
    CharacterConverter.convertCharacter(normalizedCharacter,
      Conversions.FROM_HIRAGANA_TO_KATAKANA)
  }
}
