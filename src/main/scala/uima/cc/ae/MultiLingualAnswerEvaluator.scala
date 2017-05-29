package uima.cc.ae

import java.util.Locale

import org.apache.uima.cas.{CAS, FSIterator}
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import uima.modules.common.MultiLingualDocumentAnnotator
import us.feliscat.evaluation.{MacroAveraging, MicroAveraging, RougeN, SummaryStatistics}
import us.feliscat.text.{StringNone, StringOption}
import us.feliscat.types._
import us.feliscat.util.uima.fsList._
import us.feliscat.util.uima.JCasUtils
import util.Config

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualAnswerEvaluator extends MultiLingualDocumentAnnotator {
  def process(aCAS: CAS): StringOption = {
    val aJCas: JCas = aCAS.getView(localeId).getJCas
    JCasUtils.setAJCasOpt(Option(aJCas))
    process(aJCas)
  }

  //(has keywords?, character limit, Seq[(writer, rouge1, rouge2)])
  private type ParticularEvaluationResult = (Boolean, String, Seq[(String, Int, Double, Double)])

  protected def limit: String

  protected def count(textOpt: StringOption): Int

  def processQuestion(question: Question): Unit = {

  }

  private def process(aJCas: JCas): StringOption = {
    println(s">> ${new Locale(localeId).getDisplayLanguage} Essay Evaluator Processing")
    val rouge1 = new RougeN(1)
    val rouge2 = new RougeN(2)

    val rouge1Map = mutable.Map.empty[String, ArrayBuffer[Seq[Double]]]
    val rouge2Map = mutable.Map.empty[String, ArrayBuffer[Seq[Double]]]

    val particularExamMap = mutable.LinkedHashMap.empty[String, mutable.LinkedHashMap[String, ParticularEvaluationResult]]

    val examBuffer = ListBuffer.empty[Exam]
    @SuppressWarnings(Array[String]("rawtypes"))
    val itExam: FSIterator[Nothing] = aJCas.getAnnotationIndex(Exam.`type`).iterator(true)
    while (itExam.hasNext) {
      val exam: Exam = itExam.next
      examBuffer += exam
    }

    if (examBuffer.isEmpty) {
      return StringNone
    }

    examBuffer.result.sortWith((a, b) => a.getLabel < b.getLabel) foreach {
      exam: Exam =>
        val rouge1Map4Exam = mutable.Map.empty[String, ListBuffer[Double]]
        val rouge2Map4Exam = mutable.Map.empty[String, ListBuffer[Double]]

        val questionSet: FSArray = exam.getQuestionSet

        // for checking no answer
        val writerList = ListBuffer.empty[String]
        for (i <- 0 until questionSet.size) {
          val question: Question = questionSet.get(i).asInstanceOf[Question]
          val answerSet: Seq[Answer] = question.getAnswerSet.toSeq.asInstanceOf[Seq[Answer]]
          answerSet foreach {
            case answer: Answer if !answer.getIsGoldStandard && !writerList.contains(answer.getWriter) =>
              writerList += answer.getWriter
            case _ =>
            // Do nothing
          }
        }

        val particularQuestionMap = mutable.LinkedHashMap.empty[String, ParticularEvaluationResult]
        for (i <- 0 until questionSet.size) {
          val question: Question = questionSet.get(i).asInstanceOf[Question]
          val answerSet: Seq[Answer] = question.getAnswerSet.toSeq.asInstanceOf[Seq[Answer]]
          val writerAnswerMap = mutable.LinkedHashMap.empty[String, Seq[Sentence]]
          val goldStandardSet = ListBuffer.empty[Seq[Sentence]]
          answerSet foreach {
            answer: Answer =>
              val document: Document = answer.getDocument
              if (answer.getIsGoldStandard) {
                annotate(aJCas, document, Nil)
                goldStandardSet += document.getSentenceSet.toSeq.asInstanceOf[Seq[Sentence]]
              } else {
                writerAnswerMap(answer.getWriter) = document.getSentenceSet.toSeq.asInstanceOf[Seq[Sentence]]
              }
          }
          // for no answer
          writerList.result foreach {
            writer: String =>
              if (!writerAnswerMap.contains(writer)) {
                writerAnswerMap(writer) = Nil
              }
          }
          val goldStandardTermList: Seq[Seq[Seq[String]]] = goldStandardSet.result map {
            goldStandard: Seq[Sentence] =>
              goldStandard map {
                sentence: Sentence =>
                  sentence.getContentWordList.toSeq
              }
          }

          val particularWriterBuffer = ListBuffer.empty[(String, Int, Double, Double)]
          writerAnswerMap foreach {
            case (writer, sentenceList) =>
              val systemTermList: Seq[Seq[String]] = sentenceList map {
                sentence: Sentence => sentence.getContentWordList.toSeq
              }
              if (!(rouge1Map4Exam contains writer)) {
                rouge1Map4Exam(writer) = ListBuffer.empty[Double]
              }
              val rouge1Score: Double = rouge1.evaluate(systemTermList, goldStandardTermList)
              rouge1Map4Exam(writer) += rouge1Score

              if (!(rouge2Map4Exam contains writer)) {
                rouge2Map4Exam(writer) = ListBuffer.empty[Double]
              }
              val rouge2Score: Double = rouge2.evaluate(systemTermList, goldStandardTermList)
              rouge2Map4Exam(writer) += rouge2Score

              val length: Int = {
                var textOpt = StringOption.empty
                answerSet foreach {
                  case answer: Answer if !answer.getIsGoldStandard && answer.getWriter == writer =>
                    textOpt = StringOption(answer.getDocument.getText)
                  case _ =>
                  // Do nothing
                }
                count(textOpt)
              }

              particularWriterBuffer += ((writer, length, rouge1Score, rouge2Score))
            case _ =>
            // Do nothing
          }
          particularQuestionMap(question.getLabel) = (
            question.getKeywordSet.toSeq.nonEmpty,
            s"""${question.getBeginLengthLimit.toString} - ${question.getEndLengthLimit.toString}""",
            particularWriterBuffer.result
          )
        }
        particularExamMap(exam.getLabel) = particularQuestionMap

        rouge1Map4Exam foreach {
          case (writer, scores) =>
            if (!(rouge1Map contains writer)) {
              rouge1Map(writer) = ArrayBuffer.empty[Seq[Double]]
            }
            rouge1Map(writer) += scores.result
        }

        rouge2Map4Exam foreach {
          case (writer, scores) =>
            if (!(rouge2Map contains writer)) {
              rouge2Map(writer) = ArrayBuffer.empty[Seq[Double]]
            }
            rouge2Map(writer) += scores.result
        }
    }

    val rougeNCell: String = """<th rowspan="2" scope="row">ROUGE-%d F1 Score by Content Word</th>"""
    val rouge1Cell: String = rougeNCell.format(1)
    val rouge2Cell: String = rougeNCell.format(2)

    val tableSummaryBuilder     = new StringBuilder()
    val tableParticularsBuilder = new StringBuilder()
    val writers: Seq[String] = rouge1Map.keySet.toSeq.sorted
    if (writers.isEmpty) {
      return StringNone
    }
    writers foreach {
      writer: String =>
        val writerCell: String = s"""<th rowspan="4" scope="row">$writer</th>"""

        val microCell: String = """<th scope="row">Micro</th>"""
        val macroCell: String = """<th scope="row">Macro</th>"""

        val rouge1: Array[Seq[Double]] = rouge1Map(writer).toArray[Seq[Double]]
        val rouge1MicroAveragingSummaryStatistics: SummaryStatistics = MicroAveraging.summaryStatistics(rouge1)
        val rouge1MicroAveragingCells: String = microCell.concat(rouge1MicroAveragingSummaryStatistics.toString)

        val rouge1MacroAveragingSummaryStatistics: SummaryStatistics = MacroAveraging.summaryStatistics(rouge1)
        val rouge1MacroAveragingCells: String = macroCell.concat(rouge1MacroAveragingSummaryStatistics.toString)

        val rouge2: Array[Seq[Double]] = rouge2Map(writer).toArray[Seq[Double]]
        val rouge2MicroAveragingSummaryStatistics: SummaryStatistics = MicroAveraging.summaryStatistics(rouge2)
        val rouge2MicroAveragingCells: String = microCell.concat(rouge2MicroAveragingSummaryStatistics.toString)

        val rouge2MacroAveragingSummaryStatistics: SummaryStatistics = MacroAveraging.summaryStatistics(rouge2)
        val rouge2MacroAveragingCells: String = macroCell.concat(rouge2MacroAveragingSummaryStatistics.toString)

        tableSummaryBuilder.
          append(s"""<tr>$writerCell$rouge1Cell$rouge1MicroAveragingCells</tr>""").append('\n').
          append(s"""<tr>$rouge1MacroAveragingCells</tr>""").append('\n').
          append(s"""<tr>$rouge2Cell$rouge2MicroAveragingCells</tr>""").append('\n').
          append(s"""<tr>$rouge2MacroAveragingCells</tr>""").append('\n')
    }

    val datasetNumOfLinesMap = mutable.LinkedHashMap.empty[String, Int]
    particularExamMap foreach {
      case (datasetLabel, question) =>
        var counter: Int = 0
        question foreach {
          case (_, scores) =>
            counter += scores._3.size
        }
        datasetNumOfLinesMap(datasetLabel) = counter
      case _ =>
      // Do nothing
    }

    particularExamMap foreach {
      case (datasetLabel, question) =>
        var isFirst4Dataset: Boolean = true
        val datasetLabelCell: String = s"""<th rowspan="${datasetNumOfLinesMap(datasetLabel)}" scope="row"><a href="./${datasetLabel.dropRight(4)}_${Config.systemName}.xml" target="_blank">$datasetLabel</a></th>"""
        question foreach {
          case (questionLabel, scores) =>
            var isFirst4Question: Boolean = true
            val size: Int = scores._3.size
            val questionLabelCell: String = s"""<th rowspan="$size" scope="row">$questionLabel</th><td rowspan="$size">${if (scores._1) "Yes" else "No"}</td><td rowspan="$size">${scores._2}</td>"""
            scores._3 foreach {
              case (writer, length, r1, r2) =>
                val range: Array[Int] = scores._2.split('-').map {
                  l: String =>
                    try {
                      l.trim.toInt
                    } catch {
                      case e: NumberFormatException =>
                        e.printStackTrace()
                        0
                    }
                }
                val color: String = {
                  if (range.head <= length && length <= range.last) {
                    "text-success"
                  } else {
                    "text-danger"
                  }
                }
                val scoreCells: String = f"""<td><span class="$color">$length</span></td><td>$writer</td><td>$r1%.5f</td><td>$r2%.5f</td>"""
                tableParticularsBuilder.append(s"""<tr>${
                  if (isFirst4Dataset) {
                    isFirst4Dataset = false
                    datasetLabelCell
                  } else ""
                }${
                  if (isFirst4Question) {
                    isFirst4Question = false
                    questionLabelCell
                  } else ""
                }$scoreCells</tr>""").append('\n')
              case _ =>
              // Do nothing
            }
          case _ =>
          // Do nothing
        }
      case _ =>
      // Do nothing
    }
    val partOfEvaluationResult: String =
      s"""<h1>${new Locale(localeId).getDisplayLanguage} Essay Evaluation Results</h1>
         |<p>${Config.timestamp}</p>
         |<p><a href="../index.html">HISTORY</a></p>
         |<h2>Summary</h2>
         |<table class="table table-sm table-bordered">
         |<thead class="thead-inverse"><tr><th>WRITER</th><th>ROUGE</th><th>AVERAGING</th><th># OF DATASETs</th><th># OF Qs</th><th>MEAN</th><th>MAX</th><th>MEDIAN</th><th>MIN</th><th>VARIANCE</th><th>STANDARD DEVIATION</th></tr></thead>
         |<tbody>$tableSummaryBuilder</tbody>
         |</table>
         |<h2>Particulars</h2>
         |<table class="table table-sm table-bordered">
         |<thead class="thead-inverse"><tr><th>DATASET</th><th>QUESTION LABEL</th><th>HAS KEYWORDS?</th><th>$limit LIMIT</th><th>$limit LENGTH</th><th>WRITER</th><th>ROUGE-1 F1 Score by Content Word</th><th>ROUGE-2 F1 Score by Content Word</th></tr></thead>
         |<tbody>$tableParticularsBuilder</tbody>
         |</table><hr>""".stripMargin
    println(partOfEvaluationResult.replaceAll("<[^>]+>", " "))
    StringOption(partOfEvaluationResult)
  }
}
