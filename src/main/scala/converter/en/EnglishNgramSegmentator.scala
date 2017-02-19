package converter.en

import converter.MultiLingualNgramSegmentator
import m17n.English

/**
  * <pre>
  * Created on 2017/02/11.
  * </pre>
  *
  * @author K.Sakamoto
  */
class EnglishNgramSegmentator(nGram: Int) extends MultiLingualNgramSegmentator(nGram) with English
