package uima.ae.ag.ja

import org.apache.uima.jcas.JCas
import uima.ae.ag.MultiLingualAnswerGenerator
import us.feliscat.answer.MultiLingualAnswerCandidatesGenerator
import us.feliscat.answer.ja.{JapaneseAnswerCandidate, JapaneseAnswerCandidates, JapaneseAnswerCandidatesGenerator}
import us.feliscat.exam.ja.JapaneseLengthCounter
import us.feliscat.m17n.Japanese
import us.feliscat.sentence.MultiLingualSentenceGroup
import us.feliscat.sentence.ja.{JapaneseSentenceCombination, JapaneseSentenceCombinationGenerator, JapaneseSentenceGroup}
import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.types._
import us.feliscat.util.uima.fsList.FSListUtils
import us.feliscat.util.uima.{FeatureStructure, JCasID}
import us.feliscat.util.uima.seq2fs.SeqUtils
import util.Config

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseAnswerGenerator
  extends MultiLingualAnswerGenerator
    with Japanese {
  override protected val mAnswerGenerator: MultiLingualAnswerCandidatesGenerator = {
    new JapaneseAnswerCandidatesGenerator(mScoreIndex)
  }

  override protected def groupSentences(keyword: Keyword,
                                        sentences: Seq[Sentence]): MultiLingualSentenceGroup = {
    new JapaneseSentenceGroup(keyword, sentences)
  }
  override protected def countNumber(textOpt: StringOption): Int = {
    JapaneseLengthCounter.count(textOpt)
  }
  override def combine(aJCas: JCas,
                       question: Question,
                       scoreIndex: Int,
                       selectedSentenceList: Seq[Sentence],
                       sentenceGroupList: Seq[MultiLingualSentenceGroup])(implicit id: JCasID): Unit = {
    //combinations
    val sentenceCombinationGenerator = new JapaneseSentenceCombinationGenerator(scoreIndex)
    val sentenceCombinationList: Seq[JapaneseSentenceCombination] = {
      sentenceCombinationGenerator.
        generate(
          sentenceGroupList.asInstanceOf[Seq[JapaneseSentenceGroup]],
          question.getEndLengthLimit)
    }

    //Maximal Marginal Relevance (MMR) for filling the rest of character limit
    val answerCandidateBuffer = ListBuffer.empty[JapaneseSentenceCombination]
    val beginCharacterLimit: Int = question.getBeginLengthLimit
    val endCharacterLimit:   Int = question.getEndLengthLimit
    sentenceCombinationList foreach {
      sentenceCombination: JapaneseSentenceCombination =>
        val characterNumber: Int = sentenceCombination.number
        val restOfCharacterLimit: Int = endCharacterLimit - characterNumber
        if (0 < restOfCharacterLimit) {
          //generally come on here because characterNumber < endCharacterLimit in general

          //sort sentences by MMR
          val sentenceMMR: Seq[(Sentence, Double)] = mmrScore(
            sentenceCombination.sentences,
            selectedSentenceList,
            scoreIndex).
            sortWith((a, b) => a._2 > b._2)
          val additionalSentenceBuffer = ListBuffer.empty[Sentence]
          var counter: Int = sentenceCombination.number
          sentenceMMR.map(_._1) foreach {
            s: Sentence =>
              val count: Int = countNumber(StringOption(s.getText))
              if (counter + count <= endCharacterLimit) {
                counter += count
                additionalSentenceBuffer += s
              }
          }

          if (additionalSentenceBuffer.nonEmpty &&
            beginCharacterLimit <= counter &&
            counter <= endCharacterLimit) {
            val sc = new JapaneseSentenceCombination()
            sc.setSentences(sentenceCombination.sentences ++ additionalSentenceBuffer.result)
            sc.setNumber(counter)
            sc.setScore(sentenceCombination.score)
            sc.setLinkScore(sentenceCombination.linkScore)
            sc.setMergedScores(sentenceCombination.mergedScores)
            answerCandidateBuffer += sc
          } else if (beginCharacterLimit <= characterNumber &&
            characterNumber <= endCharacterLimit) {
            answerCandidateBuffer += sentenceCombination
          } else {
            // Do nothing
          }
        } else if (beginCharacterLimit <= characterNumber &&
          characterNumber <= endCharacterLimit) {
          // logically, (beginCharacterLimit <=) characterNumber == endCharacterLimit
          answerCandidateBuffer += sentenceCombination
        } else {
          // Do nothing
        }
    }
    if (0 < answerCandidateBuffer.size) {
      val answerResults: JapaneseAnswerCandidates = mAnswerGenerator.
        generate(answerCandidateBuffer.result).asInstanceOf[JapaneseAnswerCandidates]
      val topAnswerResult: JapaneseAnswerCandidate = answerResults.
        answerResults.
        sortWith((a, b) => a.score > b.score).head.asInstanceOf[JapaneseAnswerCandidate]
      topAnswerResult.text match {
        case StringSome(text) =>
          val answer = FeatureStructure.create[Answer]
          answer.setIsGoldStandard(false)
          answer.setWriter(Config.systemName)
          val document = FeatureStructure.create[Document]
          document.setText(text)
          document.setSentenceSet(
            topAnswerResult.
              sentenceList.
              toFSList)
          answer.setDocument(document)
          println("System Answer")
          print("Writer: ")
          println(Config.systemName)
          print("Essay: ")
          println(text)
          val answerSet: Seq[Answer] = question.getAnswerSet.toSeq.asInstanceOf[Seq[Answer]]
          question.setAnswerSet((answerSet :+ answer).toFSList)
        case StringNone =>
          generateAnswerIgnoringLengthLimit(aJCas, question, sentenceCombinationList)
      }
    } else {
      generateAnswerIgnoringLengthLimit(aJCas, question, sentenceCombinationList)
    }
  }

  private def generateAnswerIgnoringLengthLimit(aJCas: JCas,
                                                question: Question,
                                                sentenceCombinationList: Seq[JapaneseSentenceCombination])(implicit id: JCasID): Unit = {
    if (0 < sentenceCombinationList.size) {
      val answerResults: JapaneseAnswerCandidates = mAnswerGenerator.
        generate(sentenceCombinationList.toList).asInstanceOf[JapaneseAnswerCandidates]
      val topAnswerResult: JapaneseAnswerCandidate = answerResults.
        answerResults.
        sortWith((a, b) => a.score > b.score).head.asInstanceOf[JapaneseAnswerCandidate]
      topAnswerResult.text match {
        case StringSome(text) =>
          val answer = FeatureStructure.create[Answer]
          answer.setIsGoldStandard(false)
          answer.setWriter(Config.systemName)
          val document = FeatureStructure.create[Document]
          document.setText(text)
          document.setSentenceSet(topAnswerResult.sentenceList.toFSList)
          answer.setDocument(document)
          println("System Answer")
          print("Writer: ")
          println(Config.systemName)
          print("Essay: ")
          println(text)
          val answerSet: Seq[Answer] = question.getAnswerSet.toSeq.asInstanceOf[Seq[Answer]]
          question.setAnswerSet((answerSet :+ answer).toFSList)
        case StringNone =>
          generateEmptyAnswer(aJCas, question)
      }
    } else {
      generateEmptyAnswer(aJCas, question)
    }
  }
}