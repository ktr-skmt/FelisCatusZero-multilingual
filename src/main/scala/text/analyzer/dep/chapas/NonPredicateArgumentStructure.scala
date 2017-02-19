package text.analyzer.dep.chapas

import text.normalizer.ja.JapaneseNormalizedString

import scala.beans.BeanProperty

/**
  * @author Nakayama
  *         Created on 2015/11/19
  */
class NonPredicateArgumentStructure extends PredicateArgumentStructureAnalysisResult {
  @BeanProperty var text: JapaneseNormalizedString = _
}
