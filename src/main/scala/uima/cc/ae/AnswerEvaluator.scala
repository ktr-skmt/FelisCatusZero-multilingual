package uima.cc.ae

import java.awt.Desktop
import java.io.{BufferedReader, File, IOException, PrintWriter}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}
import java.util.Locale

import org.apache.uima.cas.CAS
import org.apache.uima.collection.CasConsumer_ImplBase
import org.apache.uima.resource.ResourceProcessException
import org.jsoup.nodes.Element
import uima.cc.ae.en.EnglishAnswerEvaluator
import uima.cc.ae.ja.JapaneseAnswerEvaluator
import uima.cpe.IntermediatePoint
import uima.modules.common.ja.JapaneseDocumentAnnotator
import us.feliscat.converter.HtmlTextConverter
import us.feliscat.text.{StringNone, StringSome}
import us.feliscat.util.uima.JCasID
import util.Config

/**
  * <p>論述評価をファイルに書き出すプログラム</p>
  * @author K.Sakamoto
  *         Created on 15/10/30
  */
class AnswerEvaluator extends CasConsumer_ImplBase with JapaneseDocumentAnnotator {
  private val mHistoryId: String = "evaluation_result_history"
  private val mTemplateFormat: String =
    s"""<!DOCTYPE html>
        |<html>
        |<head>
        |<meta charset="utf-8">
        |<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        |<title>Evaluation Results</title>
        |<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
        |</head>
        |<body>
        |<div class="container docs-content">
        |%s
        |</div>
        |<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
        |<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
        |<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
        |</body>
        |</html>""".stripMargin


  override def initialize(): Unit = {
    println(s">> ${IntermediatePoint.AnswerEvaluator.name} Initializing")
    super.initialize()
  }

  @throws[ResourceProcessException]
  override def processCas(aCAS: CAS): Unit = {
    println(s">> ${IntermediatePoint.AnswerEvaluator.name} Processing")

    val builder = new StringBuilder()
    JapaneseAnswerEvaluator.process(aCAS)(JCasID(Locale.JAPANESE.getLanguage)) match {
      case StringSome(result) =>
        builder.append(result)
      case StringNone =>
        // Do nothing
    }

    EnglishAnswerEvaluator.process(aCAS)(JCasID(Locale.JAPANESE.getLanguage)) match {
      case StringSome(result) =>
        builder.append(result)
      case StringNone =>
        // Do nothing
    }

    val evaluationResult: String = mTemplateFormat.format(builder.result)

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
    val indexFile: Path            = Paths.get(resultDir.toString, "index.html")
    val evaluationResultPath: Path = Paths.get(outputDir.toString, "evaluation_result.html")
    var printWriterOpt           = Option.empty[PrintWriter]
    var reader4InputFileOpt      = Option.empty[BufferedReader]
    var printWriter4IndexFileOpt = Option.empty[PrintWriter]
    try {
      printWriterOpt = Option(new PrintWriter(
        Files.newBufferedWriter(evaluationResultPath, StandardCharsets.UTF_8)))
      if (printWriterOpt.nonEmpty) {
        printWriterOpt.get.print(evaluationResult)
      }
      val htmlBuilder = new StringBuilder()
      val indexHtmlFile: File = indexFile.toFile
      if (indexHtmlFile.canRead && indexHtmlFile.isFile) {
        reader4InputFileOpt = Option(Files.newBufferedReader(indexFile, StandardCharsets.UTF_8))
        if (reader4InputFileOpt.nonEmpty) {
          val reader: BufferedReader = reader4InputFileOpt.get
          val lines: java.util.Iterator[String] = reader.lines.iterator
          while (lines.hasNext) {
            val line: String = lines.next
            htmlBuilder.append(line)
          }
        }
      }
      printWriter4IndexFileOpt = Option(new PrintWriter(
        Files.newBufferedWriter(indexFile, StandardCharsets.UTF_8)))
      if (printWriter4IndexFileOpt.nonEmpty) {
        val printWriter4IndexFile: PrintWriter = printWriter4IndexFileOpt.get
        HtmlTextConverter.toHtml(htmlBuilder.result) match {
          case Some(document) =>
            val pastResultBuilder = new StringBuilder()
            val historyOpt: Option[Element] = Option(document.getElementById(mHistoryId))
            if (historyOpt.nonEmpty) {
              pastResultBuilder.append(historyOpt.get.html)
            }
            val partOfIndexHtml: String =
              s"""<h1>Essay Evaluation Result History</h1>
                 |<ol id="$mHistoryId">
                 |<li><a href="./${Config.timestamp}/evaluation_result.html">${Config.timestamp}</a></li>
                 |$pastResultBuilder
                 |</ol>""".stripMargin
            println(partOfIndexHtml.replaceAll("<[^>]+>", " "))
            val indexHtml: String = mTemplateFormat.format(partOfIndexHtml)
            printWriter4IndexFile.print(indexHtml)
          case None =>
            // Do nothing
        }
      }
    } catch {
      case e: IOException =>
        throw e
    } finally {
      try {
        if (printWriterOpt.nonEmpty) {
          printWriterOpt.get.close()
        }
        if (reader4InputFileOpt.nonEmpty) {
          reader4InputFileOpt.get.close()
        }
        if (printWriter4IndexFileOpt.nonEmpty) {
          printWriter4IndexFileOpt.get.close()
        }
        if (Config.wantToBrowse && Desktop.isDesktopSupported) {
          try {
            Desktop.getDesktop.browse(evaluationResultPath.toFile.toURI)
          } catch {
            case e: IOException =>
              throw e
          }
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
