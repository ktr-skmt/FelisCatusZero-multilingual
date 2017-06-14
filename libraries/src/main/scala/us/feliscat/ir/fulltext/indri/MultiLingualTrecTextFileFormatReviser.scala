package us.feliscat.ir.fulltext.indri

import java.io.{BufferedReader, File, IOException, PrintWriter}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths, StandardOpenOption}

import us.feliscat.converter.{ExtensionReviser, MultiLingualNgramSegmentator}
import us.feliscat.m17n.MultiLingual
import us.feliscat.text.{StringNone, StringOption, StringSome}

import scala.util.matching.Regex

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
abstract class MultiLingualTrecTextFileFormatReviser(nGram: Int, isContentWord: Boolean) extends MultiLingual {
  //private val regexTagHead: Regex = "^<[^>]+>".r
  private def formatTitle(title: String): String = {
    s"<TITLE>$title</TITLE>"
  }
  private def formatText(text: String): String = {
    s"<TEXT>$text</TEXT>"
  }

  protected val segmentator: MultiLingualNgramSegmentator
  private val extensionReviser = new ExtensionReviser("xml")
  private val regexTitleTag:     Regex = formatTitle("([^<]+)").r
  private val regexTextTag:      Regex = formatText("([^<]*)").r
  private val regexTextStartTag: Regex = "<TEXT>([^<]*)$".r
  private val regexTextEndTag:   Regex = "^([^<]*)</TEXT>".r

  def reviseInDirectory(inputDirectoryPath: Path, outputDirectoryPath: Path): Unit = {
    try {
      val iterator: java.util.Iterator[Path] = Files.newDirectoryStream(inputDirectoryPath).iterator
      while (iterator.hasNext) {
        val next: Path = iterator.next
        if (next.toFile.getName != ".DS_Store") {// for macOS
          revise(
            next,
            outputDirectoryPath
          )
        }
      }
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }

  def revise(inputPath: Path, outputDirectoryPath: Path): Unit = {
    val reader: BufferedReader = Files.newBufferedReader(
      inputPath,
      StandardCharsets.UTF_8
    )
    val readerIterator: java.util.Iterator[String] = reader.lines.iterator
    val path: Path = extensionReviser.revise(
      Paths.get(outputDirectoryPath.toString, inputPath.toAbsolutePath.toString.split(File.separator).last)
    )
    val writer = new PrintWriter(
      Files.newBufferedWriter(
        path,
        StandardCharsets.UTF_8,
        StandardOpenOption.CREATE
      )
    )

    var isText = false
    try {
      while (readerIterator.hasNext) {
        StringOption(readerIterator.next) match {
          case StringSome(line) =>
            StringOption(reviseTag(line) match {
              case text if text contains "<TEXT></TEXT>" =>
                isText = false
                "<TEXT></TEXT>"
              case regexTextTag(text) =>
                formatText("\n%s\n" format segment(StringOption(normalizeSentences(text)), isContentWord))
              case regexTextStartTag(text) if !isText =>
                isText = true
                "<TEXT>".concat(normalizeSentences(text))
              case regexTextEndTag(text) if isText =>
                isText = false
                normalizeSentences(text).concat("</TEXT>")
              case text if isText =>
                segment(StringOption(normalizeSentences(text)), isContentWord)
              case regexTitleTag(title) =>
                formatTitle(segment(normalize(StringOption(title)), isContentWord))
              case otherwise =>
                otherwise
            }) match {
              case StringSome(l) =>
                writer.println(l)
              case StringNone =>
              // Do nothing
            }
          case StringNone =>
          // Do nothing
        }
      }
    } catch {
      case e: IOException =>
        e.printStackTrace()
    } finally {
      try {
        writer.close()
      } catch {
        case e: IOException =>
          e.printStackTrace()
      }
    }
  }

  protected def segment(text: StringOption, isContentWord: Boolean): String

  private def reviseTag(line: String): String = {
    line.
      replaceAll("<[dD][oO][cC]>",          "<DOC>").
      replaceAll("</[dD][oO][cC]>",         "</DOC>").
      replaceAll("<[dD][oO][cC][nN][oO]>",  "<DOCNO>").
      replaceAll("</[dD][oO][cC][nN][oO]>", "</DOCNO>").
      replaceAll("<[tT][eE][xX][tT]>",      "<TEXT>").
      replaceAll("</[tT][eE][xX][tT]>",     "</TEXT>").
      replaceAll("<[tT][iI][tT][lL][eE]>",  "<TITLE>").
      replaceAll("</[tT][iI][tT][lL][eE]>", "</TITLE>")
  }

  protected def normalizeSentences(line: String): String
}
