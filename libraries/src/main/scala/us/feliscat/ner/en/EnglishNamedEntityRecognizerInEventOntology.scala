package us.feliscat.ner.en

import java.io.File

import us.feliscat.m17n.English
import us.feliscat.ner.MultiLingualNamedEntityRecognizerInEventOntology
import us.feliscat.text.StringOption
import us.feliscat.time.TimeTmp
import us.feliscat.time.en.EnglishTimeExtractorInTimeExpression
import us.feliscat.util.LibrariesConfig

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
    LibrariesConfig.eventOntologyClassInEnglish.toFile.listFiles
  }
}
