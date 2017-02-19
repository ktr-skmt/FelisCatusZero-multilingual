package time.ja

import m17n.Japanese
import time.MultiLingualTimeExtractorForQuestion

/**
  * <pre>
  * Created on 2017/02/08.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseTimeExtractorForQuestion extends MultiLingualTimeExtractorForQuestion with Japanese {
  override protected val verbsOfDescribing: Seq[String] = Seq[String](
    "述べ",
    "説明",
    "答え",
    "論じ",
    "論述",
    "略述"
  )
}
