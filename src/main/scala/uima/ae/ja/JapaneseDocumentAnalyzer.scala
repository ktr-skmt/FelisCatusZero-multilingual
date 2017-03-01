package uima.ae.ja

import jeqa.types.Sentence
import jeqa.types.ja.{Morpheme, MorphemeAnalysis, SemanticType}
import m17n.Japanese
import org.apache.uima.jcas.JCas
import text.{StringNone, StringOption, StringSome}
import text.analyzer.mor.juman.{Juman, JumanPlusPlus}
import text.analyzer.mor.mecab.{IpadicMecab, UnidicMecab}
import uima.ae.MultiLingualDocumentAnalyzer
import util.Config
import util.uima.FeatureStructure
import util.uima.SeqStringUtils._
import util.uima.SeqUtils._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait JapaneseDocumentAnalyzer extends MultiLingualDocumentAnalyzer with Japanese {
  override protected def extractContentWords(sentence: StringOption): Seq[String] = {
    Config.mainMorphemeAnalyzer.toLowerCase match {
      case "unidicmecab" =>
        UnidicMecab.extractContentWords(sentence)
      case "ipadicmecab" =>
        IpadicMecab.extractContentWords(sentence)
      case "jumandicmecab" =>
        Nil
      case "unidicchasen" =>
        Nil
      case "ipadicchasen" =>
        Nil
      case "naistdicchasen" =>
        Nil
      case "juman" =>
        Juman.extractContentWords(sentence)
      case "juman++" =>
        JumanPlusPlus.extractContentWords(sentence)
      case "kytea" =>
        Nil
      case _ =>
        UnidicMecab.extractContentWords(sentence)
    }
  }

  override protected def analyze(aJCas: JCas, sentence: Sentence, normalizedSentence: StringOption): Unit = {
    val morphemeAnalysisResultBuffer = ListBuffer.empty[MorphemeAnalysis]
    if (Config.useIpadicMecab) {
      ipadicMecab(aJCas, normalizedSentence) match {
        case Some(ipadicMecabAnalysisResult) =>
          morphemeAnalysisResultBuffer += ipadicMecabAnalysisResult
        case None =>
        // Do nothing
      }

      if (Config.useIpadicCabocha) {
        //sentence.setIpadicCabocha()
        if (Config.useIpadicChaPAS) {
          //sentence.setIpadicChapas()
        }
      }
    }

    if (Config.useUnidicMecab) {
      unidicMecab(aJCas, normalizedSentence) match {
        case Some(unidicMecabAnalysisResult) =>
          morphemeAnalysisResultBuffer += unidicMecabAnalysisResult
        case None =>
        // Do nothing
      }

      if (Config.useUnidicCabocha) {
        //sentence.setUnidicCabocha()
      }
    }

    if (Config.useJumandicMecab) {
      jumandicMecab(aJCas, normalizedSentence) match {
        case Some(jumandicMecabAnalysisResult) =>
          morphemeAnalysisResultBuffer += jumandicMecabAnalysisResult
        case None =>
        // Do nothing
      }

      if (Config.useJumandicCabocha) {
        //sentence.setJumandicCabocha()
      }
    }

    if (Config.useIpadicChasen) {
      ipadicChasen(aJCas, normalizedSentence) match {
        case Some(ipadicChasenAnalysisResult) =>
          morphemeAnalysisResultBuffer += ipadicChasenAnalysisResult
        case None =>
        // Do nothing
      }
    }

    if (Config.useUnidicChasen) {
      unidicChasen(aJCas, normalizedSentence) match {
        case Some(unidicChasenAnalysisResult) =>
          morphemeAnalysisResultBuffer += unidicChasenAnalysisResult
        case None =>
        // Do nothing
      }
    }

    if (Config.useNaistdicChasen) {
      naistdicChasen(aJCas, normalizedSentence) match {
        case Some(naistdicChasenAnalysisResult) =>
          morphemeAnalysisResultBuffer += naistdicChasenAnalysisResult
        case None =>
        // Do nothing
      }
    }

    if (Config.useJuman) {
      juman(aJCas, normalizedSentence) match {
        case Some(jumanAnalysisResult) =>
          morphemeAnalysisResultBuffer += jumanAnalysisResult
        case None =>
        // Do nothing
      }

      if (Config.useKNP) {
        //sentence.setKnp()
      }
    }

    if (Config.useJumanPlusPlus) {
      jumanPlusPlus(aJCas, normalizedSentence) match {
        case Some(jumanPlusPlusAnalysisResult) =>
          morphemeAnalysisResultBuffer += jumanPlusPlusAnalysisResult
        case None =>
        // Do nothing
      }
    }

    if (Config.useKyTea) {
      kytea(aJCas, normalizedSentence) match {
        case Some(kyteaAnalysisResult) =>
          morphemeAnalysisResultBuffer += kyteaAnalysisResult
        case None =>
        // Do nothing
      }
    }

    sentence.setMorphemeAnalysisList(morphemeAnalysisResultBuffer.result.toFSList)
  }

  private lazy val ipadicMecabMorphemeCache = mutable.Map.empty[(String, String, String), Morpheme]

  private def ipadicMecab(aJCas: JCas, sentence: StringOption): Option[MorphemeAnalysis] = {
    val analysis = FeatureStructure.create[MorphemeAnalysis]
    analysis.setAnalyzer("IPADicMeCab")
    IpadicMecab.analysisResult(sentence) match {
      case StringSome(analysisResult) =>
        analysis.setAnalysisResult(analysisResult)
      case StringNone =>
        // Do nothing
        return None
    }

    val morphemeList = ListBuffer.empty[Morpheme]
    IpadicMecab.extractMorphemes(sentence) foreach {
      case (originalText, text, pos, categoryOpt, typeList) =>
        val morpheme: Morpheme =
          if (ipadicMecabMorphemeCache contains ((originalText, text, pos))) {
            ipadicMecabMorphemeCache((originalText, text, pos))
          } else {
            val mor = FeatureStructure.create[Morpheme]
            mor.setOriginalText(originalText)
            mor.setText(text)
            mor.setPos(pos)
            categoryOpt match {
              case StringSome(category) =>
                val semanticType = FeatureStructure.create[SemanticType]
                semanticType.setCategory(category)
                semanticType.setTypeList(typeList.toStringList)
                mor.setSemanticType(semanticType)
                mor.setIsTerm(true)
              case StringNone =>
                mor.setIsTerm(false)
            }
            ipadicMecabMorphemeCache((originalText, text, pos)) = mor
            mor
          }
        morphemeList += morpheme
      case _ =>
      // Do nothing
    }

    analysis.setMorphemeList(morphemeList.result.toFSList)
    Option(analysis)
  }

  private lazy val unidicMecabMorphemeCache = scala.collection.mutable.Map.empty[(String, String, String), Morpheme]

  private def unidicMecab(aJCas: JCas, sentence: StringOption): Option[MorphemeAnalysis] = {
    val analysis = FeatureStructure.create[MorphemeAnalysis]
    UnidicMecab.analysisResult(sentence) match {
      case StringSome(analysisResult) =>
        analysis.setAnalysisResult(analysisResult)
      case StringNone =>
        // Do nothing
        return None
    }
    analysis.setAnalyzer("UniDicMeCab")
    val morphemeList = ListBuffer.empty[Morpheme]
    UnidicMecab.extractMorphemes(sentence) foreach {
      case (originalText, text, pos, categoryOpt, typeList) =>
        val morpheme: Morpheme =
          if (unidicMecabMorphemeCache contains ((originalText, text, pos))) {
            unidicMecabMorphemeCache((originalText, text, pos))
          } else {
            val mor = FeatureStructure.create[Morpheme]
            mor.setOriginalText(originalText)
            mor.setText(text)
            mor.setPos(pos)
            categoryOpt match {
              case StringSome(category) =>
                val semanticType = FeatureStructure.create[SemanticType]
                semanticType.setCategory(category)
                semanticType.setTypeList(typeList.toStringList)
                mor.setSemanticType(semanticType)
                mor.setIsTerm(true)
              case StringNone =>
                mor.setIsTerm(false)
            }
            unidicMecabMorphemeCache((originalText, text, pos)) = mor
            mor
          }
        morphemeList += morpheme
      case _ =>
      // Do nothing
    }
    analysis.setMorphemeList(morphemeList.result.toFSList)
    Option(analysis)
  }

  private lazy val jumandicMecabMorphemeCache = mutable.Map.empty[(String, String, String), Morpheme]

  private def jumandicMecab(aJCas: JCas, sentence: StringOption): Option[MorphemeAnalysis] = {
    val analysis = FeatureStructure.create[MorphemeAnalysis]
    analysis.setAnalyzer("JUMANDicMeCab")
    val morphemeList = ListBuffer.empty[Morpheme]

    analysis.setMorphemeList(morphemeList.result.toFSList)
    Option(analysis)
  }

  private lazy val ipadicChasenMorphemeCache = mutable.Map.empty[(String, String, String), Morpheme]

  private def ipadicChasen(aJCas: JCas, sentence: StringOption): Option[MorphemeAnalysis] = {
    val analysis = FeatureStructure.create[MorphemeAnalysis]
    analysis.setAnalyzer("IPADicChaSen")
    val morphemeList = ListBuffer.empty[Morpheme]

    analysis.setMorphemeList(morphemeList.result.toFSList)
    Option(analysis)
  }

  private lazy val unidicChasenMorphemeCache = mutable.Map.empty[(String, String, String), Morpheme]

  private def unidicChasen(aJCas: JCas, sentence: StringOption): Option[MorphemeAnalysis] = {
    val analysis = FeatureStructure.create[MorphemeAnalysis]
    analysis.setAnalyzer("UniDicChaSen")
    val morphemeList = ListBuffer.empty[Morpheme]

    analysis.setMorphemeList(morphemeList.result.toFSList)
    Option(analysis)
  }

  private lazy val naistdicChasenMorphemeCache = mutable.Map.empty[(String, String, String), Morpheme]

  private def naistdicChasen(aJCas: JCas, sentence: StringOption): Option[MorphemeAnalysis] = {
    val analysis = FeatureStructure.create[MorphemeAnalysis]
    analysis.setAnalyzer("NAISTDicChaSen")
    val morphemeList = ListBuffer.empty[Morpheme]

    analysis.setMorphemeList(morphemeList.result.toFSList)
    Option(analysis)
  }

  private lazy val jumanMorphemeCache = mutable.Map.empty[(String, String, String), Morpheme]

  private def juman(aJCas: JCas, sentence: StringOption): Option[MorphemeAnalysis] = {
    val analysis = FeatureStructure.create[MorphemeAnalysis]
    analysis.setAnalyzer("JUMAN")
    val morphemeList = ListBuffer.empty[Morpheme]

    analysis.setMorphemeList(morphemeList.result.toFSList)
    Option(analysis)
  }

  private lazy val jumanPlusPlusMorphemeCache = mutable.Map.empty[(String, String, String), Morpheme]

  private def jumanPlusPlus(aJCas: JCas, sentence: StringOption): Option[MorphemeAnalysis] = {
    val analysis = FeatureStructure.create[MorphemeAnalysis]
    analysis.setAnalyzer("JUMAN++")
    val morphemeList = ListBuffer.empty[Morpheme]

    analysis.setMorphemeList(morphemeList.result.toFSList)
    Option(analysis)
  }

  private lazy val kyteaMorphemeCache = mutable.Map.empty[(String, String, String), Morpheme]

  private def kytea(aJCas: JCas, sentence: StringOption): Option[MorphemeAnalysis] = {
    val analysis = FeatureStructure.create[MorphemeAnalysis]
    analysis.setAnalyzer("KyTea")
    val morphemeList = ListBuffer.empty[Morpheme]

    analysis.setMorphemeList(morphemeList.result.toFSList)
    Option(analysis)
  }
}
