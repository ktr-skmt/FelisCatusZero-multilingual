package us.feliscat.text.normalizer.en

import us.feliscat.text.StringOption

/**
  * @author K. Sakamoto
  *         Created on 2017/07/12
  */
object EnglishPunctuations {
  def remove(sentenceOpt: StringOption): StringOption = {
    sentenceOpt map {
      sentence: String =>
        sentence.replaceAll("""[\p{Punct}&&[^.]]""", "")
    }
  }
}
