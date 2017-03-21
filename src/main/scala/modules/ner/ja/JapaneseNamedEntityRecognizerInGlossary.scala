package modules.ner.ja

import java.nio.file.Path

import modules.ner.MultiLingualNamedEntityRecognizerInGlossary
import modules.util.ModulesConfig
import us.feliscat.m17n.Japanese
import us.feliscat.text.StringOption
import us.feliscat.time.TimeTmp
import us.feliscat.time.ja.JapaneseTimeExtractorInGlossaryEntries

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseNamedEntityRecognizerInGlossary extends MultiLingualNamedEntityRecognizerInGlossary with Japanese {
  override protected def extract(sentence: StringOption): Seq[TimeTmp] = JapaneseTimeExtractorInGlossaryEntries.extract(sentence)

  override protected val trecTextFormatGlossary: Seq[Path] = ModulesConfig.trecTextFormatGlossaryInJapanese
}
