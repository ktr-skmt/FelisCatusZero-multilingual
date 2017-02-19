package text.normalizer.ja

import text.StringOption

/**
  * @author K.Sakamoto
  *         Created on 2016/08/06
  */
object JapaneseSentenceNormalizer {
  def normalize(str: StringOption): StringOption = {
    JapaneseSentenceEndingNormalizer.normalize(
      JapaneseSentenceBeginningNormalizer.normalize(str))
  }
}
