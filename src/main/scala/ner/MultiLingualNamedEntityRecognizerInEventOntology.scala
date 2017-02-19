package ner

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files

import m17n.MultiLingual
import text.{StringNone, StringOption, StringSome}
import time.{TimeMerger, TimeTmp}

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
trait MultiLingualNamedEntityRecognizerInEventOntology extends NamedEntityRecognizer with MultiLingual {
  protected override val recognizerName: String = "EventOntology"

  protected override lazy val entityList: NEList = initialize

  protected def extract(sentence: StringOption): Seq[TimeTmp]

  protected def eventOntologyClassFiles: Array[File]

  protected override def initialize: NEList = {
    val neBuffer = ListBuffer.empty[NE]
    eventOntologyClassFiles foreach {
      file: File =>
        val fileName: NEFile = file.getName
        val reader: java.util.Iterator[String] = Files.newBufferedReader(file.toPath, StandardCharsets.UTF_8).lines.iterator
        while (reader.hasNext) {
          val line: NELine = reader.next
          val metaInfo: MetaInfo = (fileName, line)
          val elements: Array[String] = line.split(',')
          if (5 < elements.length) {
            val years: Seq[StringOption] = Seq[StringOption](StringOption(elements(2)), StringOption(elements(3)))
            val time: TimeTmp = TimeMerger.union(
              for (year <- years) yield {
                TimeMerger.union(extract(year))
              }
            )
            for (i <- elements.indices) {
              if (!(Seq[Int](0, 2, 3) contains i)) {
                normalize(StringOption(elements(i).trim)) match {
                  case StringSome(str) =>
                    val synonyms: Array[String] = str.split('@')
                    synonyms foreach {
                      synonym: String =>
                        StringOption(synonym.trim) match {
                          case StringSome(text) =>
                            val ne: NE = (text, metaInfo, time, synonyms)
                            neBuffer += ne
                          case StringNone =>
                          //Do nothing
                        }
                    }
                  case StringNone =>
                  //Do nothing
                }
              }
            }
          }
        }
    }
    neBuffer.result
  }
}
