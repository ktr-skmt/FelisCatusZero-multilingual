package modules.ner.en

import java.nio.file.Path

import modules.ner.MultiLingualNamedEntityRecognizerInGlossary
import modules.util.ModulesConfig
import us.feliscat.m17n.English
import us.feliscat.text.StringOption
import us.feliscat.time.TimeTmp
import us.feliscat.time.en.EnglishTimeExtractorInGlossaryEntries

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishNamedEntityRecognizerInGlossary extends MultiLingualNamedEntityRecognizerInGlossary with English {
  override protected def extract(sentence: StringOption): Seq[TimeTmp] = EnglishTimeExtractorInGlossaryEntries.extract(sentence)

  override protected val trecTextFormatGlossary: Seq[Path] = ModulesConfig.trecTextFormatGlossaryInEnglish
}
