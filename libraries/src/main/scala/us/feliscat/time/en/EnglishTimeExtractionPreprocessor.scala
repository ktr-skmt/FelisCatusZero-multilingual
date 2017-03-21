package us.feliscat.time.en

import us.feliscat.m17n.English
import us.feliscat.text.StringOption
import us.feliscat.time.MultiLingualTimeExtractionPreprocessor

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
