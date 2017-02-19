package ner.en

import java.io.File

import m17n.English
import ner.MultiLingualNamedEntityRecognizerInEventOntology
import text.StringOption
import time.TimeTmp
import time.en.EnglishTimeExtractorInTimeExpression
import util.Config

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishNamedEntityRecognizerInEventOntology extends MultiLingualNamedEntityRecognizerInEventOntology with English {
  override protected def extract(sentence: StringOption): Seq[TimeTmp] = EnglishTimeExtractorInTimeExpression.extract(sentence)
  override protected def eventOntologyClassFiles: Array[File] = {
    Config.eventOntologyClassInEnglish.toFile.listFiles
  }
}
