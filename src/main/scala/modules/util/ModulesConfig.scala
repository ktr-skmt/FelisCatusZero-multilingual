package modules.util

import java.io.File
import java.nio.file.{Path, Paths}

import com.typesafe.config.{ConfigFactory, Config => TypeSafeConfig}
import net.ceedubs.ficus.Ficus._
import us.feliscat.util.xml.XmlSchema

/**
  * <pre>
  * Created on 2017/03/19.
  * </pre>
  *
  * @author K.Sakamoto
  */
object ModulesConfig {
  final private[this] var config: TypeSafeConfig = ConfigFactory.load()

  def set(configFile: File): Unit = {
    config = ConfigFactory.load(ConfigFactory.parseFile(configFile))
  }

  final lazy val qaCorpusXmlSchema: XmlSchema = new XmlSchema(new File(config.as[Option[String]]("schema.qaCorpus").getOrElse("src/main/resources/schema/qa_corpus.xsd")))

  final lazy val qalabDatasetXmlSchema: XmlSchema = new XmlSchema(new File(config.as[Option[String]]("schema.qalabDataset").getOrElse("src/main/resources/schema/second_stage_exam.xsd")))

  lazy val qalabDatasetPath: String = config.as[Option[String]]("qalab.datasetPath").getOrElse("src/main/resources/qalab_dataset")

  final lazy val essayExamDirOpt: Option[String] = config.as[Option[String]]("exam.essayExamDir")

  lazy val centerTestJaXmlDir: String = Paths.get(config.as[Option[String]]("exam.ja.nationalCenterTestDir").getOrElse("res/national_center_test/uima.modules.qa.question/ja")).toAbsolutePath.toString

  lazy val centerTestEnXmlDir: String = Paths.get(config.as[Option[String]]("exam.en.nationalCenterTestDir").getOrElse("res/national_center_test/uima.modules.qa.question/en")).toAbsolutePath.toString

  lazy val fastTextResource: String = config.as[Option[String]]("wordEmbedding.fastText.resource").getOrElse("out/fastText/data.txt")

  lazy val fastTextModel: String = config.as[Option[String]]("wordEmbedding.fastText.model").getOrElse("out/fastText/model")

  lazy val fastTextModelBin: String = fastTextModel concat ".bin"

  lazy val fastTextModelVec: String = fastTextModel concat ".vec"

  lazy val fastTextQuery: String = config.as[Option[String]]("wordEmbedding.fastText.query").getOrElse("out/fastText/query.txt")

  lazy val characterLevelIndriSegmentationsInJapanese: Seq[String] =
    config.as[Option[Seq[String]]]("uima.modules.ir.indri.segmentation.characterLevel.textbook.ja").getOrElse(Nil) ++
      config.as[Option[Seq[String]]]("uima.modules.ir.indri.segmentation.characterLevel.glossary.ja").getOrElse(Nil)

  lazy val tokenLevelIndriSegmentationsInEnglish: Seq[String] =
    config.as[Option[Seq[String]]]("uima.modules.ir.indri.segmentation.tokenLevel.textbook.en").getOrElse(Nil) ++
      config.as[Option[Seq[String]]]("uima.modules.ir.indri.segmentation.tokenLevel.glossary.en").getOrElse(Nil)

  lazy val wordLevelIndriSegmentationsInJapanese: Seq[String] =
    config.as[Option[Seq[String]]]("uima.modules.ir.indri.segmentation.wordLevel.textbook.ja").getOrElse(Nil) ++
      config.as[Option[Seq[String]]]("uima.modules.ir.indri.segmentation.wordLevel.glossary.ja").getOrElse(Nil)

  lazy val wordLevelIndriSegmentationsInEnglish: Seq[String] =
    config.as[Option[Seq[String]]]("uima.modules.ir.indri.segmentation.wordLevel.textbook.en").getOrElse(Nil) ++
      config.as[Option[Seq[String]]]("uima.modules.ir.indri.segmentation.wordLevel.glossary.en").getOrElse(Nil)

  lazy val trecTextFormatGlossaryInJapanese: Seq[Path] = {
    config.as[Option[Seq[String]]]("knowledgeSource.trecTextFormat.glossary.ja").getOrElse(Nil).map(path => Paths.get(path, "yamakawa_world_history_glossary.xml"))
  }

  lazy val trecTextFormatGlossaryInEnglish: Seq[Path] = {
    config.as[Option[Seq[String]]]("knowledgeSource.trecTextFormat.glossary.en").getOrElse(Nil).map(path => Paths.get(path, "yamakawa_world_history_glossary_en.xml"))
  }

  lazy val characterLevelIndriIndicesInJapanese: Seq[String] =
    config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.characterLevel.textbook.ja").getOrElse(Nil) ++
      config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.characterLevel.glossary.ja").getOrElse(Nil)

  lazy val tokenLevelIndriIndicesInEnglish: Seq[String] =
    config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.tokenLevel.textbook.en").getOrElse(Nil) ++
      config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.tokenLevel.glossary.en").getOrElse(Nil)

  lazy val wordLevelIndriIndicesInJapanese: Seq[String] =
    config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.wordLevel.textbook.ja").getOrElse(Nil) ++
      config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.wordLevel.glossary.ja").getOrElse(Nil)

  lazy val wordLevelIndriIndicesInEnglish: Seq[String] =
    config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.wordLevel.textbook.en").getOrElse(Nil) ++
      config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.wordLevel.glossary.en").getOrElse(Nil)

  lazy val trecTextFormatDataInJapanese: Seq[String] = {
    config.as[Option[Seq[String]]]("knowledgeSource.trecTextFormat.textbook.ja").getOrElse(Nil) ++
      config.as[Option[Seq[String]]]("knowledgeSource.trecTextFormat.glossary.ja").getOrElse(Nil)
  }

  lazy val trecTextFormatDataInEnglish: Seq[String] = {
    config.as[Option[Seq[String]]]("knowledgeSource.trecTextFormat.textbook.en").getOrElse(Nil) ++
      config.as[Option[Seq[String]]]("knowledgeSource.trecTextFormat.glossary.en").getOrElse(Nil)
  }

  lazy val indriGeoCharacterlevelIndicesInJapanese: Seq[String] = config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.characterLevel.geo.ja").getOrElse(Nil)

  lazy val indriGeoTokenlevelIndicesInEnglish: Seq[String] = config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.tokenLevel.geo.en").getOrElse(Nil)

  lazy val indriWikipediaCharacterLevelIndicesInJapanese: Seq[String] = config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.characterLevel.wikipedia.ja").getOrElse(Nil)

  lazy val indriWikipediaTokenLevelIndicesInEnglish: Seq[String] = config.as[Option[Seq[String]]]("knowledgeSource.indriIndex.characterLevel.wikipedia.en").getOrElse(Nil)

}
