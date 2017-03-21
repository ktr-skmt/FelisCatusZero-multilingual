package us.feliscat.text.analyzer.dep.chapas

import us.feliscat.text.StringOption
import us.feliscat.text.normalizer.ja.JapaneseNormalizedString
import us.feliscat.util.Cache

/**
  * @author Nakayama
  *         Created on 2015/12/20
  */
abstract class PasAnalyzer extends Cache {
  def analyze(sentence: JapaneseNormalizedString): Iterator[String]

  def cabochaLattice(sentence: JapaneseNormalizedString): Iterator[String]

  def cabochaTree(sentence: JapaneseNormalizedString): Iterator[String]

  protected[this] def textToDoc(normalizedSentence: JapaneseNormalizedString): Doc

  val parser: NtcParser

  /*
  def parse(normalizedSentence: NormalizedString): Doc = {
    val doc: Doc = this.textToDoc(NormalizedStringOption(normalizedSentence))
    doc.sentences.foreach { s =>
      val normalizedString: NormalizedStringOption = NormalizedStringOption(NormalizedString(StringOption(s.us.feliscat.text)))
      s.depTree = this.cabochaTree(normalizedString).mkString("\n").stripLineEnd
    }
    doc
  }
  */

  def parse(normalizedSentence: JapaneseNormalizedString): Doc =
    textToDoc(normalizedSentence)

  def parse(normalizedSentences: Seq[JapaneseNormalizedString]): Doc =
    parse(JapaneseNormalizedString(StringOption(normalizedSentences.mkString("\n"))))
}
