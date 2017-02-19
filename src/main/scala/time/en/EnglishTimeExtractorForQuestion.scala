package time.en

import m17n.English
import time.MultiLingualTimeExtractorForQuestion

/**
  * <pre>
  * Created on 2017/02/08.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishTimeExtractorForQuestion extends MultiLingualTimeExtractorForQuestion with English {
  override protected val verbsOfDescribing: Seq[String] = Seq[String](
    "Describe",
    "describe",
    "Provide an overview of",
    "provide an overview of",
    "Explain",
    "explain",
    "Discuss",
    "discuss",
    "Write",
    "write",
    "How",
    "how",
    "Be sure to list specific examples"
  )
}
