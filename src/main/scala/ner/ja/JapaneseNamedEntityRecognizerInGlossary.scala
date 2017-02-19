package ner.ja

import java.nio.file.Path

import m17n.Japanese
import ner.MultiLingualNamedEntityRecognizerInGlossary
import text.StringOption
import time.TimeTmp
import time.ja.JapaneseTimeExtractorInGlossaryEntries
import util.Config

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseNamedEntityRecognizerInGlossary extends MultiLingualNamedEntityRecognizerInGlossary with Japanese {
  override protected def extract(sentence: StringOption): Seq[TimeTmp] = JapaneseTimeExtractorInGlossaryEntries.extract(sentence)

  override protected val trecTextFormatGlossary: Seq[Path] = Config.trecTextFormatGlossaryInJapanese
}
