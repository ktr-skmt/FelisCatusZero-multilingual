package uima.ae

import java.util.Locale

import answer.MultiLingualAnswerCandidatesGenerator
import jeqa.types._
import m17n.MultiLingual
import org.apache.uima.cas.FSIterator
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import score.noupdate.NoUpdateTypeScorer
import score.update.UpdateTypeScorer
import sentence.MultiLingualSentenceGroup
import text.StringOption
import util.Config
import util.uima.FSListUtils._
import util.uima.SeqUtils._
import util.uima.JCasUtils

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualEssayGenerator extends MultiLingual {
  private val mIndriScoreIndex: Int = 0
  protected val mScoreIndex: Int = 1
  protected val mAnswerGenerator: MultiLingualAnswerCandidatesGenerator

  protected def groupSentences(keyword: Keyword,
                               sentences: Seq[Sentence]): MultiLingualSentenceGroup

  protected def mmrScore(summary: Seq[Sentence],
                         candidates: Seq[Sentence],
                         scoreIndex: Int): Seq[(Sentence, Double)] = {
    val scorer = new NoUpdateTypeScorer(summary)
    val candidateArray: Array[Sentence] = candidates.toArray
    for (i <- candidateArray.indices) yield {
      val candidate: Sentence = candidateArray(i)
      val restOfCandidates: Seq[Sentence] = candidateArray.drop(i).toSeq
      (
        candidate,
        Config.lambdaOfMMR * candidate.getScoreList(scoreIndex).getScore
          - (1D - Config.lambdaOfMMR) * scorer.score(restOfCandidates)
      )
    }
  }

  def process(aJCas: JCas): Unit = {
    println(s">> ${new Locale(localeId).getDisplayLanguage} Essay Generator Processing")
    val aView: JCas = aJCas.getView(localeId)
    JCasUtils.setAJCasOpt(Option(aView))

    @SuppressWarnings(Array[String]("rawtypes"))
    val itExam: FSIterator[Nothing] = aView.getAnnotationIndex(Exam.`type`).iterator(true)
    while (itExam.hasNext) {
      val exam: Exam = itExam.next
      println("Dataset:")
      print("* ")
      println(exam.getLabel)
      println("Question:")
      val questionSet: FSArray = exam.getQuestionSet
      for (i <- 0 until questionSet.size) {
        //score sentences
        val question: Question = questionSet.get(i).asInstanceOf[Question]
        print("- ")
        println(question.getLabel)
        val questionDocument: Document = question.getDocument
        val scorer: UpdateTypeScorer = {
          if (question.getKeywordSet.toSeq.nonEmpty) {
            new UpdateTypeScorer(questionDocument, mScoreIndex)
          } else {
            new UpdateTypeScorer(questionDocument, mIndriScoreIndex)
          }
        }
        val querySet: FSArray = question.getQuerySet
        val keywordQueryBuffer = ListBuffer.empty[KeywordQuery]
        val bowQueryBuffer     = ListBuffer.empty[BoWQuery]
        for (j <- 0 until querySet.size()) {
          querySet.get(j) match {
            case query: KeywordQuery =>
              keywordQueryBuffer += query
            case query: BoWQuery =>
              bowQueryBuffer += query
            case _ =>
            // Do nothing
          }
        }
        if (keywordQueryBuffer.nonEmpty) {
          generateAnswer(
            aView,
            question,
            keywordQueryBuffer.result.map(query => query.getKeyword),
            scorer,
            mScoreIndex)
        } else {
          generateAnswer(
            aView,
            question,
            bowQueryBuffer.result.map(query => query.getIndriQuery),
            scorer,
            mIndriScoreIndex)
        }
      }
    }

  }

  protected def countNumber(textOpt: StringOption): Int

  protected def combine(aJCas: JCas,
                        question: Question,
                        scoreIndex: Int,
                        selectedSentenceList: Seq[Sentence],
                        sentenceGroupList: Seq[MultiLingualSentenceGroup]): Unit

  private def generateAnswer(aJCas: JCas,
                             question: Question,
                             keywordSet: Seq[Keyword],
                             scorer: UpdateTypeScorer,
                             scoreIndex: Int): Unit = {
    val selectedSentenceBuffer = ListBuffer.empty[Sentence]
    keywordSet foreach {
      keyword: Keyword =>
        val sentenceList: Seq[Sentence] = keyword.getSentenceSet.toSeq.asInstanceOf[Seq[Sentence]]
        sentenceList foreach {
          sentence: Sentence =>
            scorer.scoreBySentence(sentence)
            selectedSentenceBuffer += sentence
        }
    }

    //select top-N sentences for each keyword
    val sentenceGroupBuffer = ListBuffer.empty[MultiLingualSentenceGroup]
    keywordSet foreach {
      keyword: Keyword =>
        val sentenceList: Seq[Sentence] = keyword.getSentenceSet.toSeq.asInstanceOf[Seq[Sentence]]
        sentenceGroupBuffer += groupSentences(
          keyword,
          sentenceList.sortWith {
            (a, b) =>
              a.getScoreList(scoreIndex).getScore > b.getScoreList(scoreIndex).getScore
          }.take(Config.limitOfSentenceSelection)
        )
    }
    combine(
      aJCas,
      question,
      scoreIndex,
      selectedSentenceBuffer.result,
      sentenceGroupBuffer.result
    )
  }

  protected def generateEmptyAnswer(aJCas: JCas,
                                    question: Question): Unit = {
    val answer = new Answer(aJCas)
    answer.addToIndexes()
    answer.setIsGoldStandard(false)
    answer.setWriter(Config.systemName)
    val document = new Document(aJCas)
    document.addToIndexes()
    document.setText("")
    answer.setDocument(document)
    println("System Answer")
    print("Writer: ")
    println(Config.systemName)
    print("Essay: ")
    println("(empty)")
    val answerSet: Seq[Answer] = question.getAnswerSet.toSeq.asInstanceOf[Seq[Answer]]
    question.setAnswerSet((answerSet:+ answer).toFSList)
  }
}
