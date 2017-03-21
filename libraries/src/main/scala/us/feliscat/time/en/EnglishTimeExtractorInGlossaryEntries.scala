package us.feliscat.time.en

import us.feliscat.m17n.English
import us.feliscat.time.MultiLingualTimeExtractorInGlossaryEntries
import us.feliscat.util.LibrariesConfig

/**
  * <pre>
  * Created on 2017/02/08.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTimeExtractorInGlossaryEntries extends MultiLingualTimeExtractorInGlossaryEntries with English {
  override protected val glossaryEntries: String = LibrariesConfig.glossaryEntriesForTimeExtractorInEnglish
}
