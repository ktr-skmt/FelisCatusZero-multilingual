package time.ja

import m17n.Japanese
import time.MultiLingualTimeExtractorInGlossaryEntries
import util.Config

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
  override protected val glossaryEntries: String = Config.glossaryEntriesForTimeExtractorInJapanese
}
