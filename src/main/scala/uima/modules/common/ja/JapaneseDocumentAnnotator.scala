package uima.modules.common.ja

import java.util.regex.{Matcher, Pattern}

import uima.modules.common.MultiLingualDocumentAnnotator
import us.feliscat.sentence.ja.JapaneseSentenceSplitter
import us.feliscat.text.StringOption
import us.feliscat.time.TimeTmp
import us.feliscat.time.ja.JapaneseTimeExtractorForWorldHistory
import us.feliscat.types.TextAnnotation
import us.feliscat.types.ja.Morpheme
import us.feliscat.util.uima.FSListUtils._

import scala.collection.mutable

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait JapaneseDocumentAnnotator extends MultiLingualDocumentAnnotator with JapaneseDocumentAnalyzer {
  override protected def ssplit(textOpt: StringOption): Seq[String] = {
    JapaneseSentenceSplitter.split(textOpt).map(_.text)
  }

  override protected def extractTime(sentenceOpt: StringOption): TimeTmp = {
    JapaneseTimeExtractorForWorldHistory.extractUnionTime(sentenceOpt)
  }

  override protected def correct(text: TextAnnotation, keywordSet: Seq[String]): Unit = {
    val builder = new mutable.StringBuilder(text.getText.length)
    text.getMorphemeList.toSeq.asInstanceOf[Seq[Morpheme]] foreach {
      morpheme: Morpheme =>
        if (!morpheme.getPos.startsWith("接続詞")) {
          builder.append(morpheme.getOriginalText)
        }
    }
    text.setText(recursivelyRemoveNoise(builder.result, keywordSet, 0))
  }

  private def recursivelyRemoveNoise(text: String, keywordSet: Seq[String], index: Int): String = {
    val p: Pattern = Pattern.compile("""\([^\)]*\)""")
    val m: Matcher = p.matcher(text)
    if (m.find(index)) {
      if (keywordSet.contains(m.group)) {
        recursivelyRemoveNoise(
          text,
          keywordSet,
          m.end)
      } else {
        recursivelyRemoveNoise(
          text.substring(0, m.start) concat text.substring(m.end),
          keywordSet,
          m.start)
      }
    } else {
      text
    }
  }
}
