package time.en

import m17n.English
import text.StringOption
import time.MultiLingualTimeExtractionPreprocessor

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTimeExtractionPreprocessor extends MultiLingualTimeExtractionPreprocessor with English {
  override def convert(text: StringOption): StringOption = StringOption.empty
}
