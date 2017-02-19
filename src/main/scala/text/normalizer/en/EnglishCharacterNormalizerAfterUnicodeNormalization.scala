package text.normalizer.en

import text.StringOption
import text.normalizer.DictionaryBasedNormalizer

/**
  * @author K.Sakamoto
  *         Created on 2016/02/20
  */
object EnglishCharacterNormalizerAfterUnicodeNormalization
  extends DictionaryBasedNormalizer(
    StringOption("character_dic_after_unicode_normalization.yml"))
