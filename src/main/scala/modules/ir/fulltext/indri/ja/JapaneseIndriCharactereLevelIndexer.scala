package modules.ir.fulltext.indri.ja

import modules.util.ModulesConfig
import us.feliscat.ir.fulltext.indri.MultiLingualIndriIndexer
import us.feliscat.ir.fulltext.indri.ja.JapaneseTrecTextFileFormatReviser
import us.feliscat.m17n.Japanese

/**
  * <pre>
  * Created on 2017/01/13.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseIndriCharactereLevelIndexer extends MultiLingualIndriIndexer with Japanese {
  protected override val indices:       Array[String] = ModulesConfig.characterLevelIndriIndicesInJapanese.toArray
  protected override val segmentations: Array[String] = ModulesConfig.characterLevelIndriSegmentationsInJapanese.toArray
  protected override val resources:     Array[String] = ModulesConfig.trecTextFormatDataInJapanese.toArray
  protected override val reviser = new JapaneseTrecTextFileFormatReviser(1, false)
}
