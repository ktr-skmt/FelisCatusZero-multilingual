package us.feliscat.text.normalizer.ja

import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.DictionaryBasedNormalizer

/**
  * @author K.Sakamoto
  *         Created on 2016/08/06
  */
object JapaneseSentenceBeginningNormalizer
  extends DictionaryBasedNormalizer(
    StringOption("sentence_beginning_normalization.yml")) {

  override protected def replaceAll(input: String, term: String, replacement: String): String = {
    input.replaceAll(raw"""^$term""", replacement)
  }
}
