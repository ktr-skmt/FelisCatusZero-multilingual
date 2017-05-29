package util

import java.io.File
import java.time.format.DateTimeFormatter
import java.time.{ZoneId, ZonedDateTime}

import com.typesafe.config.{ConfigFactory, Config => TypeSafeConfig}
import net.ceedubs.ficus.Ficus._
import us.feliscat.text.{StringNone, StringOption}
import us.feliscat.util.LibrariesConfig
import us.feliscat.util.primitive.StringUtils

/**
  * <pre>
  * Created on 2017/03/19.
  * </pre>
  *
  * @author K.Sakamoto
  */
object Config {
  val timestamp: String = {
    val now: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault)
    DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(now).replaceAllLiteratim(":", "")
  }

  final private[this] var config: TypeSafeConfig = ConfigFactory.load()

  def set(configFile: File): Unit = {
    config = ConfigFactory.load(ConfigFactory.parseFile(configFile))
  }

  lazy val systemName: String = config.as[Option[String]]("systemName").getOrElse("Baseline")

  lazy val teamIdOpt: StringOption = config.as[Option[String]]("qalab.teamId") match {
    case Some(teamId) => StringOption(teamId)
    case None => StringNone
  }

  lazy val runIdOpt: Option[Int] = config.as[Option[Int]]("qalab.runId")

  lazy val wantToBrowse: Boolean = config.as[Option[Boolean]]("wantToBrowse").getOrElse(false)

  var wantToOutputForQALabExtractionSubtask:       Boolean = config.as[Option[Boolean]]("qalab.wantToOutputForExtractionSubtask").getOrElse(false)
  var wantToOutputForQALabSummarizationSubtask:    Boolean = config.as[Option[Boolean]]("qalab.wantToOutputForSummarizationSubtask").getOrElse(false)
  var wantToOutputForQALabEvaluationMethodSubtask: Boolean = config.as[Option[Boolean]]("qalab.wantToOutputForEvaluationMethodSubtask").getOrElse(false)

  var doCharacterLevelIndriIndexAsPreprocessInJapanese: Boolean = config.as[Option[Boolean]]("preprocess.ja.doCharacterLevelIndriIndex").getOrElse(false)

  var doContentWordLevelIndriIndexAsPreprocessInJapanese: Boolean = config.as[Option[Boolean]]("preprocess.ja.doContentWordLevelIndriIndex").getOrElse(false)

  var doTokenLevelIndriIndexAsPreprocessInEnglish: Boolean = config.as[Option[Boolean]]("preprocess.en.doTokenLevelIndriIndex").getOrElse(false)

  var doContentWordLevelIndriIndexAsPreprocessInEnglish: Boolean = config.as[Option[Boolean]]("preprocess.en.doContentWordLevelIndriIndex").getOrElse(false)

  var doFastTestAsPreprocess: Boolean = config.as[Option[Boolean]]("preprocess.doFastText").getOrElse(false)

  lazy val numOfScores: Int = config.as[Option[Int]]("numOfScores").getOrElse(3)

  final lazy val indriRunQueryTimeout: Int = config.as[Option[Int]]("indri.timeout").getOrElse(10)

  final lazy val indriTfidfK1: String = config.as[Option[String]]("uima.modules.ir.indri.tfidf.k1").getOrElse("1.2")

  final lazy val indriTfidfB: String = config.as[Option[String]]("uima.modules.ir.indri.tfidf.b").getOrElse("0.75")

  final lazy val indriBm25K1: String = config.as[Option[String]]("uima.modules.ir.indri.bm25.k1").getOrElse("1.2")

  final lazy val indriBm25B: String = config.as[Option[String]]("uima.modules.ir.indri.bm25.b").getOrElse("0.75")

  final lazy val indriBm25K3: String = config.as[Option[String]]("uima.modules.ir.indri.bm25.k3").getOrElse("7")

  lazy val indriCount: Int = LibrariesConfig.indriCount

  lazy val indriMemory: String = LibrariesConfig.indriMemory

  final lazy val needInitialSentenceTimeAnalysisByTextbook: Boolean = config.as[Option[Boolean]]("needInitialSentenceTimeAnalysisByTextbook").getOrElse(false)

  final lazy val usePassage: Boolean = config.as[Option[Boolean]]("passage.use").getOrElse(false)

  final lazy val passageWindow: Int = config.as[Option[Int]]("passage.window").getOrElse(3)

  final lazy val mainMorphemeAnalyzer: String = config.as[Option[String]]("analyzer.mainMorphemeAnalyzer").getOrElse("IPADicMeCab")

  final lazy val useIpadicMecab: Boolean = config.as[Option[Boolean]]("analyzer.mecab.ipadic.use").getOrElse(false)

  final lazy val useIpadicCabocha: Boolean = {
    if (useIpadicMecab) {
      config.as[Option[Boolean]]("analyzer.mecab.ipadic.cabocha.use").getOrElse(false)
    } else {
      false
    }
  }

  final lazy val useIpadicChaPAS: Boolean = {
    if (useIpadicMecab && useIpadicCabocha) {
      config.as[Option[Boolean]]("analyzer.mecab.ipadic.use").getOrElse(false)
    } else {
      false
    }
  }

  final lazy val useUnidicMecab: Boolean = config.as[Option[Boolean]]("analyzer.mecab.unidic.use").getOrElse(false)

  final lazy val useUnidicCabocha: Boolean = config.as[Option[Boolean]]("analyzer.mecab.unidic.cabocha.use").getOrElse(false)

  final lazy val useJumandicMecab: Boolean = config.as[Option[Boolean]]("analyzer.mecab.jumandic.use").getOrElse(false)

  final lazy val useJumandicCabocha: Boolean = config.as[Option[Boolean]]("analyzer.mecab.jumandic.cabocha.use").getOrElse(false)

  final lazy val useIpadicChasen: Boolean = config.as[Option[Boolean]]("analyzer.chasen.ipadic.use").getOrElse(false)

  final lazy val useUnidicChasen: Boolean = config.as[Option[Boolean]]("analyzer.chasen.unidic.use").getOrElse(false)

  final lazy val useJumandicChasen: Boolean = config.as[Option[Boolean]]("analyzer.chasen.jumandic.use").getOrElse(false)

  final lazy val useNaistdicChasen: Boolean = config.as[Option[Boolean]]("analyzer.chasen.naistdic.use").getOrElse(false)


  final lazy val useJuman: Boolean = config.as[Option[Boolean]]("analyzer.juman.use").getOrElse(false)

  final lazy val useJumanPlusPlus: Boolean = config.as[Option[Boolean]]("analyzer.jumanplusplus.use").getOrElse(false)

  final lazy val useKNP: Boolean = {
    if (useJuman) {
      config.as[Option[Boolean]]("analyzer.knp.use").getOrElse(false)
    } else {
      false
    }
  }

  final lazy val useKyTea: Boolean = config.as[Option[Boolean]]("analyzer.kytea.use").getOrElse(false)
}
