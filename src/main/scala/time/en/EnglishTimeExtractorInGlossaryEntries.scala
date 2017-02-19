package time.en

import m17n.English
import time.MultiLingualTimeExtractorInGlossaryEntries
import util.Config

/**
  * <pre>
  * Created on 2017/02/08.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTimeExtractorInGlossaryEntries extends MultiLingualTimeExtractorInGlossaryEntries with English {
  override protected val glossaryEntries: String = Config.glossaryEntriesForTimeExtractorInEnglish
}
