package evaluation.ja

import evaluation.MultiLingualEssayEvaluationResult
import m17n.Japanese

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
class JapaneseEssayEvaluationResult(var rouge1: Double,
                                    var rouge2: Double) extends MultiLingualEssayEvaluationResult with Japanese
