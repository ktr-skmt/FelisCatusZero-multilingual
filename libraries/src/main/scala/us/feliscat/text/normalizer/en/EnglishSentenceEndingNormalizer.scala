package us.feliscat.text.normalizer.en

import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.DictionaryBasedNormalizer

/**
  * @author K.Sakamoto
  *         Created on 2016/08/06
  */
object EnglishSentenceEndingNormalizer
  extends DictionaryBasedNormalizer(
      StringOption("sentence_ending_normalization.yml")) {
    override protected def replaceAll(input: String, term: String, replacement: String): String = {
      input.replaceAll(raw"""$term$$""", replacement)
    }
  }
