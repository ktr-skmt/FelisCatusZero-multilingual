package us.feliscat.time.ja

import us.feliscat.m17n.Japanese
import us.feliscat.time.MultiLingualTimeExtractorInGlossaryEntries
import us.feliscat.util.LibrariesConfig

/**
  * <pre>
  * Created on 2017/02/08.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseTimeExtractorInGlossaryEntries
  extends MultiLingualTimeExtractorInGlossaryEntries
    with Japanese {
  override protected val glossaryEntries: String = LibrariesConfig.glossaryEntriesForTimeExtractorInJapanese
}
