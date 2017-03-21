package us.feliscat.evaluation.en

import us.feliscat.evaluation.MultiLingualEssayEvaluationResult
import us.feliscat.m17n.English

/**
  * <pre>
  * Created on 2017/02/09.
  * </pre>
  *
  * @author K.Sakamoto
  */
class EnglishEssayEvaluationResult(var rouge1: Double,
                                   var rouge2: Double) extends MultiLingualEssayEvaluationResult with English
