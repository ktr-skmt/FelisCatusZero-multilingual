package us.feliscat.text.normalizer.en

import us.feliscat.m17n.EnglishLocale
import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.MultiLingualDictionaryBasedNormalizer

/**
  * @author K.Sakamoto
  *         Created on 2016/08/06
  */
object EnglishSentenceBeginningNormalizer
  extends MultiLingualDictionaryBasedNormalizer(
      StringOption("sentence_beginning_normalization.yml")) with EnglishLocale {

    override protected def replaceAll(input: String, term: String, replacement: String): String = {
      input.replaceAll(raw"""^$term""", replacement)
    }
  }
