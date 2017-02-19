package ir.fulltext.indri.en

import ir.fulltext.indri.MultiLingualIndriIndexer
import m17n.English
import util.Config

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
object EnglishIndriContentWordLevelIndexer extends MultiLingualIndriIndexer with English {
  protected override val indices:       Array[String] = Config.wordLevelIndriIndicesInEnglish.toArray
  protected override val segmentations: Array[String] = Config.wordLevelIndriSegmentationsInEnglish.toArray
  protected override val resources:     Array[String] = Config.trecTextFormatDataInEnglish.toArray
  protected override val reviser = new EnglishTrecTextFileFormatReviser(1, true)
}
