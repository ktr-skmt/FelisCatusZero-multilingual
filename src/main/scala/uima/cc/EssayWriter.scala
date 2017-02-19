package uima.cc

import java.io.{File, IOException, PrintWriter}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import org.apache.uima.cas.CAS
import org.apache.uima.collection.CasConsumer_ImplBase
import org.apache.uima.resource.ResourceProcessException
import uima.cc.en.EnglishEssayWriter
import uima.cc.ja.JapaneseEssayWriter
import util.Config

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.xml._

/**
  * <p>論述文をファイルに書き出すプログラム</p>
  * @author K.Sakamoto
  *         Created on 15/10/30
  */
class EssayWriter extends CasConsumer_ImplBase {
  override def initialize(): Unit = {
    println(">> Essay Writer Initializing")
    super.initialize()
  }

  // question -> locale (-> answer)
  private type QuestionLocale = mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Elem]]
  // writer -> question -> locale (-> answer)
  private type WriterQuestionLocale = mutable.LinkedHashMap[String, QuestionLocale]

  @throws[ResourceProcessException]
  override def processCas(aCAS: CAS): Unit = {
    println(">> Essay Writer Processing")

    val japaneseResults: mutable.LinkedHashMap[String, WriterQuestionLocale] = {
      JapaneseEssayWriter.process(aCAS)
    }
    val englishResults:  mutable.LinkedHashMap[String, WriterQuestionLocale] = {
      EnglishEssayWriter.process(aCAS)
    }

    val allExams: Seq[String] = (japaneseResults.keySet ++ englishResults.keySet).toSeq.distinct

    allExams foreach {
      exam: String =>
        val fileNameFormat: String = {
          s"""${
            if (exam.endsWith(".xml")) {
              exam.dropRight(4)
            } else {
              exam
            }
          }_%s.xml"""
        }

        val writerQuestionLocaleList = ListBuffer.empty[WriterQuestionLocale]
        if (japaneseResults.contains(exam)) {
          writerQuestionLocaleList += japaneseResults(exam)
        }
        if (englishResults.contains(exam)) {
          writerQuestionLocaleList += englishResults(exam)
        }

        val mergedWriterQuestionLocaleList: WriterQuestionLocale = merge(writerQuestionLocaleList.result)
        val writerIterator: Iterator[String] = mergedWriterQuestionLocaleList.keysIterator
        while (writerIterator.hasNext) {
          val writer: String = writerIterator.next
          val questionLocaleMap: QuestionLocale = mergedWriterQuestionLocaleList(writer)
          print(fileNameFormat, writer, questionLocaleMap)
        }
    }
  }

  private def merge(multiLingualWriterQuestionLocale: Seq[WriterQuestionLocale]): WriterQuestionLocale = {
    val map = mutable.LinkedHashMap.empty[String, QuestionLocale]
    val writerListBuffer = ListBuffer.empty[String]
    multiLingualWriterQuestionLocale foreach {
      writerQuestionLocale: WriterQuestionLocale =>
        val writerIterator: Iterator[String] = writerQuestionLocale.keysIterator
        while (writerIterator.hasNext) {
          val writer: String = writerIterator.next
          if (!writerListBuffer.contains(writer)) {
            writerListBuffer += writer
          }
        }
    }
    writerListBuffer.result foreach {
      writer: String =>
        val questionLocaleBuffer = ListBuffer.empty[QuestionLocale]
        multiLingualWriterQuestionLocale foreach {
          writerQuestionLocale: WriterQuestionLocale =>
            if (writerQuestionLocale.contains(writer)) {
              //writerQuestionLocale(writer)
              questionLocaleBuffer += writerQuestionLocale(writer)
            }
        }
        map(writer) = merge$(questionLocaleBuffer.result)
    }
    map
  }

  private def merge$(multiLingualQuestionLocale: Seq[QuestionLocale]): QuestionLocale = {
    val questionLabelList = ListBuffer.empty[String]
    multiLingualQuestionLocale foreach {
      questionLocale: QuestionLocale =>
        val questionLocaleIterator: Iterator[String] = questionLocale.keysIterator
        while (questionLocaleIterator.hasNext) {
          val questionLabel: String = questionLocaleIterator.next
          if (!questionLabelList.contains(questionLabel)) {
            questionLabelList += questionLabel
          }
        }
    }
    val map = mutable.LinkedHashMap.empty[String, mutable.LinkedHashMap[String, Elem]]
    questionLabelList.result foreach {
      questionLabel: String =>
        if (!map.contains(questionLabel)) {
          map(questionLabel) = mutable.LinkedHashMap.empty[String, Elem]
        }
        multiLingualQuestionLocale foreach {
          questionLocale: mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, Elem]] =>
            if (questionLocale.contains(questionLabel)) {
              questionLocale(questionLabel) foreach {
                case (locale, xml) if !map(questionLabel).contains(locale) =>
                  map(questionLabel)(locale) = xml
                case _ =>
                  // Do nothing
              }
            }
        }
    }
    map
  }

  private def print(fileNameFormat: String, writer: String, questionLocaleMap: QuestionLocale): Unit = {
    val printer = new PrettyPrinter(10000, 2)
    val answerSheet: Elem = <answer_sheet ver="0.1">{
      var id: Int = 0
      questionLocaleMap map {
        case (questionLabel, localeMap) =>
          id += 1
          val buffer = ListBuffer.empty[Elem]
          localeMap foreach {
            case (_, xml) =>
              buffer += xml
            case _ =>
              // Do nothing
          }
          <answer_section id={id.toString} label={questionLabel}>
            {buffer.result}
          </answer_section>
        case _ =>
          // Do nothing
      }
    }</answer_sheet>
    var printWriterOpt = Option.empty[PrintWriter]
    try {
      val resultDir: File = Paths.get("out", "result").toAbsolutePath.toFile
      if (!resultDir.canRead) {
        resultDir.mkdir
      } else if (!resultDir.isDirectory) {
        resultDir.delete
        resultDir.mkdir
      }
      val outputDir: File = Paths.get(resultDir.toString, Config.timestamp).toAbsolutePath.toFile
      if (!outputDir.canRead) {
        outputDir.mkdir
      } else if (!outputDir.isDirectory) {
        outputDir.delete
        outputDir.mkdir
      }
      printWriterOpt = Option(new PrintWriter(
        Files.newBufferedWriter(
          Paths.get(outputDir.toString, fileNameFormat.format(writer)),
          StandardCharsets.UTF_8)))
      if (printWriterOpt.nonEmpty) {
        val printWriter: PrintWriter = printWriterOpt.get
        printWriter.println("""<?xml version="1.0" encoding="UTF-8"?>""")
        printWriter.print(printer.format(answerSheet))
      }
    } catch {
      case e: IOException =>
        throw e
    } finally {
      try {
        if (printWriterOpt.nonEmpty) {
          printWriterOpt.get.close()
        }
      } catch {
        case e: IOException =>
          throw e
      }
    }
  }

  @throws[ResourceProcessException]
  override def destroy(): Unit = {
    //Do nothing
  }
}
