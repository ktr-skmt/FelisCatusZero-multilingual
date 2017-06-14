package us.feliscat.util

import java.io.File
import java.nio.file.{Path, Paths}

import com.typesafe.config.{ConfigFactory, Config => TypeSafeConfig}
import net.ceedubs.ficus.Ficus._
import us.feliscat.text.{StringNone, StringOption}
import us.feliscat.text.similarity.{Dissimilarity, Overlap, Similarity}

/**
  * @author K.Sakamoto
  *         Created on 2016/07/25
  */
object LibrariesConfig {
  final private[this] var config: TypeSafeConfig = ConfigFactory.load()

  def set(configFile: File): Unit = {
    config = ConfigFactory.load(ConfigFactory.parseFile(configFile))
  }

  final lazy val native2ascii: String = config.as[Option[String]]("native2ascii").getOrElse(s"${System.getProperty("java.home")}/../bin/native2ascii")

  final lazy val mecabTimeout: Int = config.as[Option[Int]]("analyzer.mecab.timeout").getOrElse(1)

  var useTermNormalizer: Boolean = true

  final lazy val normalizationDirctionaryTimeout: Int = config.as[Option[Int]]("normalizationDictionary.timeout").getOrElse(10)

  final lazy val indriBuildIndexTimeout: Int = config.as[Option[Int]]("knowledgeSource.indriIndex.timeout").getOrElse(10)

  lazy val indriCount: Int = config.as[Option[Int]]("uima.modules.ir.indri.count").getOrElse(100)

  lazy val indriMemory: String = config.as[Option[String]]("uima.modules.ir.indri.memory").getOrElse("1200m")

  lazy val indriKnowledgeSourceHome: String = config.as[Option[String]]("indri.home").getOrElse("~")

  private def indriKnowledgeSourceHomePlus(relativePath: String): String = {
    indriKnowledgeSourceHome.concat(relativePath)
  }

  lazy val geoNameListInJapanese: String = indriKnowledgeSourceHomePlus(config.as[Option[String]]("knowledgeSource.geo.ja.nameList").getOrElse("wh/res/geo/list.txt"))

  lazy val geoNameListInEnglish: String = indriKnowledgeSourceHomePlus(config.as[Option[String]]("knowledgeSource.geo.en.nameList").getOrElse("wh/res/geo/list_en.txt"))

  lazy val glossaryEntriesForTimeExtractorInJapanese: String = config.as[Option[String]]("knowledgeSource.time.ja.listFromGlossary").getOrElse("src/main/resources/geotime/time_extracted_from_glossary_ja.csv")

  lazy val glossaryEntriesForTimeExtractorInEnglish: String = config.as[Option[String]]("knowledgeSource.time.en.listFromGlossary").getOrElse("src/main/resources/geotime/time_extracted_from_glossary_en.csv")

  lazy val eventOntologyClassInJapanese: Path = Paths.get(config.as[Option[String]]("knowledgeSource.eventOntology.class.ja").getOrElse("src/main/resources/ontology/class/ja/"))

  lazy val eventOntologyClassInEnglish: Path = Paths.get(config.as[Option[String]]("knowledgeSource.eventOntology.class.en").getOrElse("src/main/resources/ontology/class/en/"))

  //lazy val secondExamXmlDir: String = Paths.get(config.as[Option[String]]("exam.ja.secondStageExaminationDir").getOrElse("res/second_stage_examination/ja")).toAbsolutePath.toString

  final lazy val limitOfSentenceSelection: Int = config.as[Option[Int]]("sentenceSelection.limit").getOrElse(3)

  final lazy val resourcesDir: String = config.as[Option[String]]("resourcesDir").getOrElse("../../src/main/resources/")

  final lazy val nGram: Int = config.as[Option[Int]]("concept.nGram.n") match {
    case Some(n) if 1 <= n => n
    case _                 => 1
  }

  final lazy val nGramGap: Int = config.as[Option[Int]]("concept.nGram.gap") match {
    case Some(gap) if 0 <= gap => gap
    case Some(gap) if gap < 0  => Int.MaxValue
    case _                     => 0
  }

  final lazy val lambdaOfMMR: Double = {
    config.as[Option[Double]]("mmr.lambda").getOrElse(0.5D)
  }

  final lazy val granularity: Option[String] = config.as[Option[String]]("vector.granularity")

  final lazy val updateTypeScorer: Option[String] = config.as[Option[String]]("vector.updateTypeScorer")

  final lazy val noUpdateTypeScorer: Option[String] = config.as[Option[String]]("vector.noUpdateTypeScorer")

  final lazy val isFrequencyOtherwiseBinary: Boolean = config.as[Option[Boolean]]("vector.isFrequencyOtherwiseBinary").getOrElse(false)

  final lazy val tokenizer: String = config.as[Option[String]]("concept.tokenizer").getOrElse("CharacterNGram")

  final lazy val sentenceSplitter: String = config.as[Option[String]]("vector.concept.sentenceSplitter").getOrElse("none")

  final lazy val tverskyA: Double = config.as[Option[Double]]("vector.tverskyA").getOrElse(1D)

  final lazy val tverskyB: Double = config.as[Option[Double]]("vector.tverskyA").getOrElse(1D)

  final lazy val minkowskyQ: Double = config.as[Option[Double]]("vector.minkowskyQ").getOrElse(2D)

  final lazy val fScoreBeta: Double = config.as[Option[Double]]("vector.fScoreBeta").getOrElse(1D)

  final lazy val jaroWinklerThreshold: Double = config.as[Option[Double]]("vector.jaroWinklerThreshold").getOrElse(0.7D)

  final lazy val jaroWinklerScalingFactor: Double = config.as[Option[Double]]("vector.jaroWinklerScalingFactor").getOrElse(0.1D)

  final lazy val damerauLevenshteinDeleteCost: Double = config.as[Option[Double]]("vector.damerauLevenshteinDeleteCost").getOrElse(1D)

  final lazy val damerauLevenshteinInsertCost: Double = config.as[Option[Double]]("vector.damerauLevenshteinInsertCost").getOrElse(1D)

  final lazy val damerauLevenshteinReplaceCost: Double = config.as[Option[Double]]("vector.damerauLevenshteinReplaceCost").getOrElse(1D)

  final lazy val damerauLevenshteinSwapCost: Double = config.as[Option[Double]]("vector.damerauLevenshteinSwapCost").getOrElse(1D)

  final lazy val similarity: Similarity.Value = {
    config.as[Option[String]]("vector.similarity") match {
      case Some(sim) if sim equalsIgnoreCase "AverageTwoWayConversions" =>
        Similarity.AverageTwoWayConversions
      case Some(sim) if sim equalsIgnoreCase "Cosine" =>
        Similarity.Cosine
      case Some(sim) if sim equalsIgnoreCase "Covariance" =>
        Similarity.Covariance
      case Some(sim) if sim equalsIgnoreCase "Dice" =>
        Similarity.Dice
      case Some(sim) if sim equalsIgnoreCase "InnerProduct" =>
        Similarity.InnerProduct
      case Some(sim) if sim equalsIgnoreCase "Jaccard" =>
        Similarity.Jaccard
      case Some(sim) if sim equalsIgnoreCase "JaccardSimpson" =>
        Similarity.JaccardSimpson
      case Some(sim) if sim equalsIgnoreCase "Lin98" =>
        Similarity.Lin98
      case Some(sim) if sim equalsIgnoreCase "Mihalcea04" =>
        Similarity.Mihalcea04
      case Some(sim) if sim equalsIgnoreCase "PearsonProductMomentCorrelationCoefficient" =>
        Similarity.PearsonProductMomentCorrelationCoefficient
      case Some(sim) if sim equalsIgnoreCase "ReciprocalChebyshev" =>
        Similarity.ReciprocalChebyshev
      case Some(sim) if sim equalsIgnoreCase "ReciprocalEuclidean" =>
        Similarity.ReciprocalEuclidean
      case Some(sim) if sim equalsIgnoreCase "ReciprocalManhattan" =>
        Similarity.ReciprocalManhattan
      case Some(sim) if sim equalsIgnoreCase "ReciprocalMinkowsky" =>
        Similarity.ReciprocalMinkowsky
      case Some(sim) if sim equalsIgnoreCase "Simpson" =>
        Similarity.Simpson
      case Some(sim) if sim equalsIgnoreCase "Tversky" =>
        Similarity.Tversky
      case _ =>
        Similarity.Cosine
    }
  }

  final lazy val dissimilarity: Dissimilarity.Value = {
    config.as[Option[String]]("vector.dissimilarity") match {
      case Some(d) =>
        d.toLowerCase match {
          case "chebyshev" =>
            Dissimilarity.Chebyshev
          case "euclidean" =>
            Dissimilarity.Euclidean
          case "manhattan" =>
            Dissimilarity.Manhattan
          case "minkowsky" =>
            Dissimilarity.Minkowsky
          case _ =>
            Dissimilarity.None
        }
      case None =>
        Dissimilarity.None
    }
  }

  final lazy val convergence: Overlap.Value = {
    config.as[Option[String]]("vector.convergence") match {
      case Some(con) if con equalsIgnoreCase "Rus05" =>
        Overlap.Rus05
      case Some(con) if con equalsIgnoreCase "F" =>
        Overlap.F
      case Some(con) if con equalsIgnoreCase "F1" =>
        Overlap.F1
      case Some(con) if con equalsIgnoreCase "Precision" =>
        Overlap.Precision
      case Some(con) if con equalsIgnoreCase "Recall" =>
        Overlap.Recall
      case _ =>
        Overlap.Rus05
    }
  }

  private def dicDir(defaultDir: String, dir: String): String = {
    config.as[Option[String]](dir) match {
      case Some(s) if s != "" =>
        val f: File = new File(s)
        if (f.canRead) {
          f.toPath.toAbsolutePath.toString
        } else {
          defaultDir
        }
      case _ =>
        defaultDir
    }
  }

  final lazy val mecabIPADicDir: String = dicDir("/usr/local/lib/mecab/dic/ipadic", "analyzer.mecab.ipadic.dir")

  final lazy val mecabIPADicUserDic: StringOption = config.as[Option[String]]("analyzer.mecab.ipadic.userDic") match {
    case Some(userDic) => StringOption(userDic)
    case None => StringNone
  }

  final lazy val mecabUnidicDir: String = dicDir("/usr/local/lib/mecab/dic/unidic", "analyzer.mecab.unidic.dir")

  final lazy val mecabUnidicUserDic: StringOption = config.as[Option[String]]("analyzer.mecab.unidic.userDic") match {
    case Some(userDic) => StringOption(userDic)
    case None => StringNone
  }

  final lazy val mecabJumandicDir: String = dicDir("/usr/local/lib/mecab/dic/jumandic", "analyzer.mecab.jumandic.dir")

  final lazy val chasenIPADicDir: String = dicDir("/usr/local/lib/chasen/dic/ipadic", "analyzer.chasen.ipadic.dir")

  final lazy val chasenUnidicDir: String = dicDir("/usr/local/lib/chasen/dic/unidic", "analyzer.chasen.unidic.dir")

  final lazy val chasenNAISTDicDir: String = dicDir("/usr/local/lib/chasen/dic/naistdic", "analyzer.chasen.naistdic.dir")

}
