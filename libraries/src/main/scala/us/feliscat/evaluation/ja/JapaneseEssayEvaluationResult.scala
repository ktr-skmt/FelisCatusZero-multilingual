package us.feliscat.evaluation.ja

import us.feliscat.evaluation.MultiLingualEssayEvaluationResult
import us.feliscat.m17n.Japanese

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
class JapaneseEssayEvaluationResult(var rouge1: Double,
                                    var rouge2: Double) extends MultiLingualEssayEvaluationResult with Japanese
