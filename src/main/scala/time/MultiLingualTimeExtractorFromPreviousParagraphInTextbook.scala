package time

import java.io.{BufferedReader, File}
import java.nio.charset.StandardCharsets
import java.nio.file.Files

import ir.fulltext.indri.IndriResult
import m17n.MultiLingual
import text.{StringNone, StringOption, StringSome}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/08.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualTimeExtractorFromPreviousParagraphInTextbook extends TimeExtractor with MultiLingual {
  protected val trecTextFormatData: Seq[String]

  private lazy val docnoTimeTmpListMap: Map[String, Seq[TimeTmp]] = initialize

  protected def extractForWorldHistory(sentenceOpt: StringOption): Seq[TimeTmp]

  protected def toIndriResultMap(lines: Iterator[String],
                                 keywordOriginalTextOpt: StringOption,
                                 expansionOnlyList: Seq[String],
                                 indriResultMap: mutable.Map[String, IndriResult]): Map[String, IndriResult]

  def initialize: Map[String, Seq[TimeTmp]] = {
    val map = mutable.Map.empty[String, Seq[TimeTmp]]

    val buffer = ListBuffer.empty[String]
    trecTextFormatData foreach {
      dir: String =>
        new File(dir).listFiles foreach {
          case file: File if file.canRead && file.isFile && file.getName.endsWith(".xml") =>
            val reader: BufferedReader = Files.newBufferedReader(file.toPath, StandardCharsets.UTF_8)
            val iterator: java.util.Iterator[String] = reader.lines.iterator
            while (iterator.hasNext) {
              val line: String = iterator.next
              buffer += line
            }
            reader.close()
          case _ =>
          // Do nothing
        }
    }
    val iterator: Iterator[IndriResult] = toIndriResultMap(
      buffer.result.iterator,
      StringNone,
      Nil,
      mutable.Map.empty[String, IndriResult]).valuesIterator
    while (iterator.hasNext) {
      val result: IndriResult = iterator.next
      result.docno match {
        case StringSome(docno) if !map.contains(docno) =>
          map(docno) = extractForWorldHistory(result.text)
        case _ =>
        // Do nothing
      }
    }

    map.toMap
  }

  override def extract(docnoOpt: StringOption): Seq[TimeTmp] = {
    docnoOpt match {
      case StringSome(docno) if isOk(docno) =>
        docnoTimeTmpListMap(docno)
      case _ =>
        Nil
    }
  }

  private def isOk(docno: String): Boolean = {
    docno.matches("""^(?:T-WH-[ABS]|Y-JH)-.+$""") &&
      !isFirstDoc(docno) &&
      !docno.startsWith("YamakawaWorldHistoryGlossary") && // just in case
      docnoTimeTmpListMap.contains(docno)
  }

  private def isFirstDoc(docno: String): Boolean = {
    Seq[String](
      "T-WH-A-1-0-0-0",
      "T-WH-B-1-0-0-0",
      "T-WH-S-1-0-0-0",
      "Y-JH-00-00-0"
    ) contains docno
  }
}
