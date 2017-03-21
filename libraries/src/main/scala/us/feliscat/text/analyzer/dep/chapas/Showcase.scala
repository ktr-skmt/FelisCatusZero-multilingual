package us.feliscat.text.analyzer.dep.chapas

import java.nio.charset.{CodingErrorAction, StandardCharsets}

import us.feliscat.text.StringNone
import us.feliscat.text.analyzer.dep.cabocha.CaboCha
import us.feliscat.text.normalizer.ja.JapaneseNormalizedString
import us.feliscat.util.{SentenceUnitReconstructorConfig, ShowcaseCache}
import us.feliscat.util.process.EchoProcess
import us.feliscat.util.process.ProcessBuilderUtils._

import scala.sys.process.Process

/**
  * @author Nakayama
  *         Created on 2015/12/20
  */
object Showcase extends PasAnalyzer with EchoProcess with ShowcaseCache {
  private val showcaseCmd: String = {
    SentenceUnitReconstructorConfig.showcase match {
      case Some(c) => c
      case None => throw new RuntimeException("Showcase command was not found.")
    }
  }

  override def analyze(sentence: JapaneseNormalizedString): Iterator[String] = {
    CaboCha.cabochaLatticeJumanCmd(sentence).#|(Process(showcaseCmd)).lineStream(
      StandardCharsets.UTF_8,
      CodingErrorAction.IGNORE,
      CodingErrorAction.IGNORE,
      StringNone
    )
  }

  override def cabochaLattice(sentence: JapaneseNormalizedString): Iterator[String] = {
    CaboCha.cabochaLatticeJumanCmd(sentence).lineStream(
      StandardCharsets.UTF_8,
      CodingErrorAction.IGNORE,
      CodingErrorAction.IGNORE,
      StringNone
    )
  }

  override def cabochaTree(sentence: JapaneseNormalizedString): Iterator[String] = {
    CaboCha.cabochaTreeJumanCmd(sentence).lineStream(
      StandardCharsets.UTF_8,
      CodingErrorAction.IGNORE,
      CodingErrorAction.IGNORE,
      StringNone
    )
  }

  override final protected[this] def textToDoc(normalizedSentencesOption: JapaneseNormalizedString): Doc =
    (if (cacheEnable) getDocCache(normalizedSentencesOption) else None) getOrElse
      this.parser.parse(analyze(normalizedSentencesOption))

  override val parser: ShowcaseNtcParser.type = ShowcaseNtcParser
}