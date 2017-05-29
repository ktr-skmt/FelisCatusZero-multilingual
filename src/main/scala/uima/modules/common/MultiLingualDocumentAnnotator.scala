package uima.modules.common

import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.time.TimeTmp
import us.feliscat.types._
import us.feliscat.types.ja.{Morpheme, MorphemeAnalysis}
import us.feliscat.util.uima.array2fs._
import us.feliscat.util.uima.fsList._
import us.feliscat.util.uima.seq2fs._
import us.feliscat.util.uima.{FeatureStructure, JCasUtils}
import util.Config

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualDocumentAnnotator extends MultiLingualDocumentAnalyzer {
  private def indexOfMorpheme(list: Seq[Morpheme], morpheme: Morpheme): Int = {
    for (i <- list.indices) {
      list(i) match {
        case m if m.getOriginalText == morpheme.getOriginalText &&
          m.getText == morpheme.getText &&
          m.getPos == morpheme.getPos =>
          return i
        case _ =>
        // Do nothing
      }
    }
    -1
  }

  private def annotatePassage(aJCas: JCas,
                              sentenceList: Seq[Sentence],
                              document: Document,
                              scoreArrayOpt: Option[FSArray],
                              keywordSet: Seq[String]): Unit = {
    val sentencePassageList = mutable.Map.empty[Sentence, ListBuffer[Passage]]
    val passageBuffer = ListBuffer.empty[Passage]
    val sentenceTypeListSize: Int = sentenceList.size
    val passageWindow: Int = Config.passageWindow
    for (i <- 0 to sentenceTypeListSize - passageWindow) {
      val passage = FeatureStructure.create[Passage]

      val contentWordList4Passage = ListBuffer.empty[String]
      val morphemeList4Passage = ListBuffer.empty[Morpheme]

      scoreArrayOpt match {
        case Some(scoreArray) =>
          passage.setScoreList(scoreArray)
        case None =>
        // Do nothing
      }

      val geographyBuffer4Passage = ListBuffer.empty[String]

      var beginTimeOpt4Passage = Option.empty[Int]
      var endTimeOpt4Passage = Option.empty[Int]
      val beginTimeTextBuffer4Passage = ListBuffer.empty[String]
      val endTimeTextBuffer4Passage = ListBuffer.empty[String]
      val sentenceArray = new Array[Sentence](passageWindow)
      for (j <- 0 until passageWindow) {
        val sentenceType: Sentence = sentenceList(i + j)
        sentenceArray(j) = sentenceType
        if (sentencePassageList contains sentenceType) {
          sentencePassageList(sentenceType) += passage
        } else {
          sentencePassageList(sentenceType) = ListBuffer.empty[Passage]
          sentencePassageList(sentenceType) += passage
        }
        sentenceType.getMorphemeList.toSeq foreach {
          case morpheme: Morpheme =>
            val index: Int = indexOfMorpheme(morphemeList4Passage, morpheme)
            morphemeList4Passage += {
              if (0 <= index && index < morphemeList4Passage.size) {
                morphemeList4Passage(index)
              } else {
                morpheme
              }
            }
          case _ =>
          // Do nothing
        }
        contentWordList4Passage ++= sentenceType.getContentWordList.toSeq

        val beginTimeOpt4Sentence = Option[Int](sentenceType.getBeginTime.getYear)
        beginTimeOpt4Sentence match {
          case Some(beginTime4Sentence) =>
            beginTimeOpt4Passage match {
              case Some(beginTime4Passage) =>
                if (beginTime4Sentence < beginTime4Passage) {
                  beginTimeOpt4Passage = beginTimeOpt4Sentence
                }
              case None =>
                beginTimeOpt4Passage = beginTimeOpt4Sentence
            }
          case None =>
          // Do nothing
        }
        val beginTimeTextList4Sentence: Seq[String] = sentenceType.getBeginTime.getTextList.toSeq
        beginTimeTextBuffer4Passage ++= beginTimeTextList4Sentence

        val endTimeOpt4Sentence = Option[Int](sentenceType.getEndTime.getYear)
        endTimeOpt4Sentence match {
          case Some(endTime4Sentence) =>
            endTimeOpt4Passage match {
              case Some(endTime4Passage) =>
                if (endTime4Passage < endTime4Sentence) {
                  endTimeOpt4Passage = endTimeOpt4Sentence
                }
              case None =>
                endTimeOpt4Passage = endTimeOpt4Sentence
            }
          case None =>
          // Do nothing
        }
        val endTimeTextList4Sentence: Seq[String] = sentenceType.getEndTime.getTextList.toSeq
        endTimeTextBuffer4Passage ++= endTimeTextList4Sentence

        val geography4Sentence: Geography = sentenceType.getGeography
        geographyBuffer4Passage ++= geography4Sentence.getArea.toSeq
      }

      passage.setBegin(sentenceArray.head.getBegin)
      passage.setEnd(sentenceArray.last.getEnd)

      passage.setSentenceSet(sentenceArray.toFSArray)
      passage.setDocumentSet(Seq[Document](document).toFSList)

      passage.setContentWordList(contentWordList4Passage.result.toStringList)
      passage.setMorphemeList(morphemeList4Passage.result.toFSList)

      beginTimeOpt4Passage match {
        case Some(beginYear) =>
          val beginYearType4Passage = FeatureStructure.create[Time]
          beginYearType4Passage.setYear(beginYear)
          beginYearType4Passage.setTextList(beginTimeTextBuffer4Passage.result.distinct.toStringList)
          passage.setBeginTime(beginYearType4Passage)
        case None =>
        // Do nothing
      }
      endTimeOpt4Passage match {
        case Some(endYear) =>
          val endYearType4Passage = FeatureStructure.create[Time]
          endYearType4Passage.setYear(endYear)
          endYearType4Passage.setTextList(endTimeTextBuffer4Passage.result.distinct.toStringList)
          passage.setEndTime(endYearType4Passage)
        case None =>
        // Do nothing
      }

      val geographyType = FeatureStructure.create[Geography]
      geographyType.setArea(geographyBuffer4Passage.result.distinct.toStringList)
      passage.setGeography(geographyType)

      correct(passage, keywordSet)
      passageBuffer += passage
    }

    sentencePassageList foreach {
      case (sentence, passageListBuffer) =>
        sentence.setPassageSet(passageListBuffer.result.toFSList)
      case _ =>
      // Do nothing
    }

    document.setPassageSet(passageBuffer.result.toFSList)
  }

  protected def ssplit(textOpt: StringOption): Seq[String]
  protected def extractTime(sentenceOpt: StringOption): TimeTmp

  //document.addToIndexesとdocument.setTextを事前に行ったdocumentが対象
  def annotate(aJCas: JCas, document: Document, keywordSet: Seq[String]): Document = {
    //println(">> Document Annotator Processing")
    JCasUtils.setAJCasOpt(Option(aJCas))
    //println(document.getText)
    val contentWordList4Doc = ListBuffer.empty[String]
    val morphemeList4Doc = ListBuffer.empty[Morpheme]

    val sentenceList: Seq[String] = ssplit(StringOption(document.getText))
    val sentenceBuffer = ListBuffer.empty[Sentence]
    val scoreArrayOpt = Option[FSArray](document.getScoreList)

    var beginIndex: Int = 0
    var endIndex: Int = -1

    var beginTimeOpt4Doc = Option.empty[Int]
    var endTimeOpt4Doc = Option.empty[Int]
    val beginTimeTextBuffer4Doc = ListBuffer.empty[String]
    val endTimeTextBuffer4Doc = ListBuffer.empty[String]

    val geographyBuffer4Doc = ListBuffer.empty[String]

    val loop = new Breaks()
    loop.breakable {
      for (i <- sentenceList.indices) {
        val sentenceOpt = StringOption(sentenceList(i))
        if (sentenceOpt.isEmpty) {
          loop.break
        }

        val sentenceType = FeatureStructure.create[Sentence]

        val originalText: String = sentenceOpt.get
        sentenceType.setOriginalText(originalText)

        beginIndex = endIndex + 1
        sentenceType.setBegin(beginIndex)

        endIndex = beginIndex + originalText.length - 1
        sentenceType.setEnd(endIndex)

        scoreArrayOpt match {
          case Some(scoreArray) =>
            sentenceType.setScoreList(scoreArray)
          case None =>
          // Do nothing
        }

        sentenceType.setText(sentenceOpt.get)

        analyze(aJCas, sentenceType, sentenceOpt)

        val contentWords: Seq[String] = extractContentWords(sentenceOpt)
        sentenceType.setContentWordList(contentWords.toStringList)
        contentWordList4Doc ++= contentWords

        val geographyBuffer4Sentence = ListBuffer.empty[String]

        val time: TimeTmp = extractTime(sentenceOpt)
        val beginTimeOpt4Sentence: Option[Int] = time.beginTime
        val endTimeOpt4Sentence:   Option[Int] = time.endTime
        val beginTimeTextBuffer4Sentence = ListBuffer.empty[String]
        beginTimeTextBuffer4Sentence ++= time.beginTimeTextList
        val endTimeTextBuffer4Sentence = ListBuffer.empty[String]
        endTimeTextBuffer4Sentence ++= time.endTimeTextList


        beginTimeOpt4Sentence match {
          case Some(beginTime4Sentence) =>
            val beginTime = FeatureStructure.create[Time]
            beginTime.setYear(beginTime4Sentence)
            val beginTimeText4Sentence: Seq[String] = beginTimeTextBuffer4Sentence.result.distinct
            beginTime.setTextList(beginTimeText4Sentence.toStringList)
            sentenceType.setBeginTime(beginTime)

            beginTimeOpt4Doc match {
              case Some(beginTime4Doc) =>
                if (beginTime4Sentence < beginTime4Doc) {
                  beginTimeOpt4Doc = beginTimeOpt4Sentence
                }
              case None =>
                beginTimeOpt4Doc = beginTimeOpt4Sentence
            }
            beginTimeTextBuffer4Doc ++= beginTimeText4Sentence
          case None =>
          // Do nothing
        }
        endTimeOpt4Sentence match {
          case Some(endTime4Sentence) =>
            val endTime = FeatureStructure.create[Time]
            endTime.setYear(endTime4Sentence)
            val endTimeText4Sentence: Seq[String] = endTimeTextBuffer4Sentence.result.distinct
            endTime.setTextList(endTimeText4Sentence.toStringList)
            sentenceType.setEndTime(endTime)

            endTimeOpt4Doc match {
              case Some(endTime4Doc) =>
                if (endTime4Doc < endTime4Sentence) {
                  endTimeOpt4Doc = endTimeOpt4Sentence
                }
              case None =>
                endTimeOpt4Doc = endTimeOpt4Sentence
            }
            endTimeTextBuffer4Doc ++= endTimeText4Sentence
          case None =>
          // Do nothing
        }

        val geographyType = FeatureStructure.create[Geography]
        val geographyList4Sentence: Seq[String] = geographyBuffer4Sentence.result.distinct
        geographyType.setArea(geographyList4Sentence.toStringList)
        geographyBuffer4Doc ++= geographyList4Sentence
        sentenceType.setGeography(geographyType)

        sentenceType.getMorphemeAnalysisList.toSeq[MorphemeAnalysis] foreach {
          case morphemeAnalysis: MorphemeAnalysis if morphemeAnalysis.getAnalyzer equalsIgnoreCase Config.mainMorphemeAnalyzer =>
            morphemeAnalysis.getMorphemeList.toSeq[Morpheme] foreach {
              case morpheme: Morpheme =>
                val index: Int = indexOfMorpheme(morphemeList4Doc, morpheme)
                morphemeList4Doc += {
                  if (0 <= index && index < morphemeList4Doc.size) {
                    morphemeList4Doc(index)
                  } else {
                    morpheme
                  }
                }
              case _ =>
              // Do nothing
            }
          case _ =>
          // Do nothing
        }

        //照応解析
        if (0 < i) {
          StringOption(sentenceList(i - 1)) match {
            case StringSome(previousSentence) =>
            //TODO: 照応解析
            case StringNone =>
            // Do nothing
          }
        }

        correct(sentenceType, keywordSet)
        sentenceBuffer += sentenceType
      }
    }

    val sentenceTypeList: Seq[Sentence] = sentenceBuffer.result

    if (Config.usePassage) {
      annotatePassage(aJCas, sentenceTypeList, document, scoreArrayOpt, keywordSet)
    }

    document.setBegin(0)
    document.setEnd(sentenceTypeList.last.getEnd)

    document.setSentenceSet(sentenceTypeList.toFSList)

    document.setContentWordList(contentWordList4Doc.result.toStringList)
    document.setMorphemeList(morphemeList4Doc.result.toFSList)

    beginTimeOpt4Doc match {
      case Some(beginYear4Doc) =>
        val beginYearType4Doc = FeatureStructure.create[Time]
        beginYearType4Doc.setTextList(beginTimeTextBuffer4Doc.result.distinct.toStringList)
        beginYearType4Doc.setYear(beginYear4Doc)
        document.setBeginTime(beginYearType4Doc)
      case None =>
      // Do nothing
    }

    endTimeOpt4Doc match {
      case Some(endYear4Doc) =>
        val endYearType4Doc = FeatureStructure.create[Time]
        endYearType4Doc.setTextList(endTimeTextBuffer4Doc.result.distinct.toStringList)
        endYearType4Doc.setYear(endYear4Doc)
        document.setEndTime(endYearType4Doc)
      case None =>
      // Do nothing
    }

    val geographyType = FeatureStructure.create[Geography]
    geographyType.setArea(geographyBuffer4Doc.result.distinct.toStringList)
    document.setGeography(geographyType)

    correct(document, keywordSet)

    document
  }

  protected def correct(text: TextAnnotation, keywordSet: Seq[String]): Unit
}
