package ner.en

import java.nio.file.Path

import m17n.English
import ner.MultiLingualNamedEntityRecognizerInGlossary
import text.StringOption
import time.TimeTmp
import time.en.EnglishTimeExtractorInGlossaryEntries
import util.Config

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishNamedEntityRecognizerInGlossary extends MultiLingualNamedEntityRecognizerInGlossary with English {
  override protected def extract(sentence: StringOption): Seq[TimeTmp] = EnglishTimeExtractorInGlossaryEntries.extract(sentence)

  override protected val trecTextFormatGlossary: Seq[Path] = Config.trecTextFormatGlossaryInEnglish
}
