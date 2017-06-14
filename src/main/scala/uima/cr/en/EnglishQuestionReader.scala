package uima.cr.en

import java.io.File

import org.apache.uima.jcas.JCas
import org.apache.uima.resource.metadata.MetaDataObject
import us.feliscat.m17n.English
import uima.cr.MultiLingualQuestionReader
import uima.rs.MultiLingualQuestion
import uima.rs.en.EnglishQuestion
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
object EnglishQuestionReader extends MultiLingualQuestionReader with English {
  protected override val lengthLimitBeginRegex: Regex = """no less than ([0-9]+) (?:English )?words""".r
  protected override val lengthLimitEndRegex:   Regex = """no more than ([0-9]+) (?:English )?words""".r
  protected override val lengthLimitApproximatorRegex: Regex = """(?:about|approximately) ([0-9]+) (?:English )?words""".r
  protected override val concisenessSet: Set[String] = Set[String](
    "concisely",
    "briefly",
    "in brief",
    "shortly",
    "in short"
  )

  override def read(metaData: java.util.Collection[MetaDataObject],
                    baseDirOpt: StringOption,
                    examFiles: Seq[File]): Seq[MultiLingualQuestion] = {
    multiCAS(metaData, baseDirOpt, examFiles) map {
      case (casId: JCasID, aJCas: JCas, question: Question) =>
        new EnglishQuestion(casId, aJCas, question)
    }
  }
}
