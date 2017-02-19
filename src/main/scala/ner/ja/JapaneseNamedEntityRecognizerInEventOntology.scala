package ner.ja

import java.io.File

import m17n.Japanese
import ner.MultiLingualNamedEntityRecognizerInEventOntology
import text.StringOption
import time.TimeTmp
import time.ja.JapaneseTimeExtractorInTimeExpression
import util.Config

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
    Config.eventOntologyClassInJapanese.toFile.listFiles
  }
}
