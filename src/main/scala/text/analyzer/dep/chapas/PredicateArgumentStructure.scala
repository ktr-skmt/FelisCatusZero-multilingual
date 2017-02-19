package text.analyzer.dep.chapas

import text.normalizer.ja.JapaneseNormalizedString

import scala.beans.BeanProperty

/**
  * @author Nakayama
  *         Created on 2015/11/19
  */
class PredicateArgumentStructure extends PredicateArgumentStructureAnalysisResult {
  @BeanProperty var phraseType: PhraseType.Value = _
  @BeanProperty var predicate: JapaneseNormalizedString = _
  @BeanProperty var ga:        JapaneseNormalizedString = _
  @BeanProperty var o:         JapaneseNormalizedString = _
  @BeanProperty var ni:        JapaneseNormalizedString = _
  @BeanProperty var other:     JapaneseNormalizedString = _
}

/**
  * @author Nakayama
  */
object PhraseType extends Enumeration {
  val None, Noun, Pred = Value
}
