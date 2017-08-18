package us.feliscat.text.normalizer.en

import us.feliscat.m17n.EnglishLocale
import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.MultiLingualDictionaryBasedNormalizer

/**
  * @author K.Sakamoto
  *         Created on 2016/02/20
  */
object EnglishCharacterNormalizerBeforeUnicodeNormalization
  extends MultiLingualDictionaryBasedNormalizer(
    StringOption("character_dic_before_unicode_normalization.yml")) with EnglishLocale
