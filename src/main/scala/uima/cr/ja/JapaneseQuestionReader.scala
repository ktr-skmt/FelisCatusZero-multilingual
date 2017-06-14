package uima.cr.ja

import java.io.File

import org.apache.uima.jcas.JCas
import org.apache.uima.resource.metadata.MetaDataObject
import us.feliscat.m17n.Japanese
import uima.cr.MultiLingualQuestionReader
import uima.rs.MultiLingualQuestion
import uima.rs.ja.JapaneseQuestion
import us.feliscat.text.StringOption
import us.feliscat.types.Question
import us.feliscat.util.uima.JCasID

import scala.util.matching.Regex

/**
  * <pre>
  * Created on 2017/02/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
object JapaneseQuestionReader extends MultiLingualQuestionReader with Japanese {
  protected override val lengthLimitBeginRegex: Regex = """([0-9]+)字以上""".r
  protected override val lengthLimitEndRegex:   Regex = """([0-9]+)字以[下内]""".r
  protected override val lengthLimitApproximatorRegex: Regex = """([0-9]+)字(?:程度|前後)""".r
  protected override val concisenessSet: Set[String] = Set[String](
    "簡潔",
    "短文"
  )

  override def read(metaData: java.util.Collection[MetaDataObject],
                    baseDirOpt: StringOption,
                    examFiles: Seq[File]): Seq[MultiLingualQuestion] = {
    multiCAS(metaData, baseDirOpt, examFiles) map {
      case (casId: JCasID, aJCas: JCas, question: Question) =>
        new JapaneseQuestion(casId, aJCas, question)
    }
  }
}
