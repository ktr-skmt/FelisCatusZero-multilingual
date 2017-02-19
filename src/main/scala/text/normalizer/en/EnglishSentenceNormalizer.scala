package text.normalizer.en

import text.StringOption

/**
  * @author K.Sakamoto
  *         Created on 2016/08/06
  */
object EnglishSentenceNormalizer {
  def normalize(str: StringOption): StringOption = {
    EnglishSentenceEndingNormalizer.normalize(
      EnglishSentenceBeginningNormalizer.normalize(str))
  }
}
