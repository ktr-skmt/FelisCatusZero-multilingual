package ir.fulltext.indri.en

import converter.MultiLingualNgramSegmentator
import converter.en.EnglishNgramSegmentator
import ir.fulltext.indri.MultiLingualTrecTextFileFormatReviser
import m17n.English
import text.StringOption
import text.analyzer.CoreNLP4English
import text.normalizer.en.EnglishNormalizedString

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
class EnglishTrecTextFileFormatReviser(nGram: Int, isChar: Boolean)
  extends MultiLingualTrecTextFileFormatReviser(nGram, isChar)
    with English {
  override protected def normalizeSentences(line: String): String = {
    EnglishNormalizedString(StringOption(line)).toString
  }

  override protected val segmentator: MultiLingualNgramSegmentator = {
    new EnglishNgramSegmentator(nGram)
  }

  override protected def segment(text: StringOption, isContentWord: Boolean): String = {
    if (isContentWord) {
      segmentator.segmentateWithToken(CoreNLP4English.extractContentWords(text)).getOrElse("")
    } else {
      segmentator.segmentateWithToken(text.getOrElse("").split(' ')).getOrElse("")
    }
  }
}
