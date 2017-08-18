package us.feliscat.text.normalizer.ja

import us.feliscat.m17n.JapaneseLocale
import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.MultiLingualDictionaryBasedNormalizer

/**
  * @author K.Sakamoto
  *         Created on 2016/08/06
  */
object JapaneseSentenceEndingNormalizer
  extends MultiLingualDictionaryBasedNormalizer(
    StringOption("sentence_ending_normalization.yml")) with JapaneseLocale {
  override protected def replaceAll(input: String, term: String, replacement: String): String = {
    input.replaceAll(raw"""$term$$""", replacement)
  }
}
