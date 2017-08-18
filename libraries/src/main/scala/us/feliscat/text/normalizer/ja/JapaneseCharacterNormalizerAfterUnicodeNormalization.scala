package us.feliscat.text.normalizer.ja

import us.feliscat.m17n.JapaneseLocale
import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.MultiLingualDictionaryBasedNormalizer

/**
  * @author K.Sakamoto
  *         Created on 2016/02/20
  */
object JapaneseCharacterNormalizerAfterUnicodeNormalization
  extends MultiLingualDictionaryBasedNormalizer(
    StringOption("character_dic_after_unicode_normalization.yml")) with JapaneseLocale
