package us.feliscat.util

import us.feliscat.score.{Granularity, ScorerType}


/**
  * <pre>
  * Created on 2017/03/19.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JCasLibrariesConfig {
  final lazy val granularity: Granularity.Value = {
    LibrariesConfig.granularity match {
      case Some(g) =>
        g.toLowerCase() match {
          case "sentence" =>
            Granularity.Sentence
          case "sentences" =>
            Granularity.Sentences
          case _ =>
            Granularity.None
        }
      case None =>
        Granularity.None
    }
  }

  final lazy val updateTypeScorer: ScorerType.Value = {
    LibrariesConfig.updateTypeScorer match {
      case Some(s) =>
        s.toLowerCase match {
          case "entailment" =>
            ScorerType.Entailment
          case "relevance" =>
            ScorerType.Relevance
          case _ =>
            ScorerType.None
        }
      case None =>
        ScorerType.None
    }
  }

  final lazy val noUpdateTypeScorer: ScorerType.Value = {
    LibrariesConfig.noUpdateTypeScorer match {
      case Some(s) =>
        s.toLowerCase match {
          case "entailment" =>
            ScorerType.Entailment
          case "relevance" =>
            ScorerType.Relevance
          case _ =>
            ScorerType.None
        }
      case None =>
        ScorerType.None
    }
  }
}
