package us.feliscat.ir.fulltext.indri.ja

import us.feliscat.converter.MultiLingualNgramSegmentator
import us.feliscat.converter.ja.JapaneseNgramSegmentator
import us.feliscat.ir.fulltext.indri.MultiLingualTrecTextFileFormatReviser
import us.feliscat.m17n.Japanese
import us.feliscat.sentence.ja.JapaneseSentenceSplitter
import us.feliscat.text.StringOption
import us.feliscat.text.analyzer.mor.mecab.UnidicMecab

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
class JapaneseTrecTextFileFormatReviser(nGram: Int, isChar: Boolean)
  extends MultiLingualTrecTextFileFormatReviser(nGram, isChar)
    with Japanese {
  override protected def normalizeSentences(line: String): String = {
    val builder = new StringBuilder()
    JapaneseSentenceSplitter.split(StringOption(line)) foreach {
      sentence =>
        builder.append(sentence.text)
    }
    builder.result
  }

  override protected val segmentator: MultiLingualNgramSegmentator = {
    new JapaneseNgramSegmentator(nGram)
  }

  override protected def segment(text: StringOption, isContentWord: Boolean): String = {
    if (isContentWord) {
      segmentator.segmentateWithToken(UnidicMecab.extractWords(text)).getOrElse("")
    } else {
      segmentator.asInstanceOf[JapaneseNgramSegmentator].segmentateWithCharacter(text).getOrElse("")
    }
  }
}
