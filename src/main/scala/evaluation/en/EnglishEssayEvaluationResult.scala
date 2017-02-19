package evaluation.en

import evaluation.MultiLingualEssayEvaluationResult
import m17n.English

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
class EnglishEssayEvaluationResult(var rouge1: Double,
                                   var rouge2: Double) extends MultiLingualEssayEvaluationResult with English
