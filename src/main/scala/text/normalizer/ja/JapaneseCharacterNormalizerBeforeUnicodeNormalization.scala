package text.normalizer.ja

import text.StringOption
import text.normalizer.DictionaryBasedNormalizer

/**
  * @author K.Sakamoto
  *         Created on 2016/02/20
  */
object JapaneseCharacterNormalizerBeforeUnicodeNormalization
  extends DictionaryBasedNormalizer(
    StringOption("character_dic_before_unicode_normalization.yml"))
