package us.feliscat.ner.ja

import java.io.File

import us.feliscat.m17n.Japanese
import us.feliscat.ner.MultiLingualNamedEntityRecognizerInEventOntology
import us.feliscat.text.StringOption
import us.feliscat.time.TimeTmp
import us.feliscat.time.ja.JapaneseTimeExtractorInTimeExpression
import us.feliscat.util.LibrariesConfig

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseNamedEntityRecognizerInEventOntology
  extends MultiLingualNamedEntityRecognizerInEventOntology
    with Japanese {
  override protected def extract(sentence: StringOption): Seq[TimeTmp] = {
    JapaneseTimeExtractorInTimeExpression.extract(sentence)
  }
  override protected def eventOntologyClassFiles: Array[File] = {
    LibrariesConfig.eventOntologyClassInJapanese.toFile.listFiles
  }
}
