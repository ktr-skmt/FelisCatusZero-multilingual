package uima.rs

import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, RunnableGraph, Sink, Source}
import akka.{Done, NotUsed}
import modules.util.ModulesConfig
import org.apache.uima.cas.CAS
import org.apache.uima.collection.{CollectionReader, CollectionReaderDescription}
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceManager
import org.apache.uima.resource.metadata.MetaDataObject
import org.apache.uima.util.{CasCreationUtils, XMLInputSource, XMLParser}
import org.apache.uima.{UIMAException, UIMAFramework}
import uima.ae.ag.en.EnglishAnswerGenerator
import uima.ae.ag.ja.JapaneseAnswerGenerator
import uima.ae.ir.en.EnglishInformationRetriever
import uima.ae.ir.ja.JapaneseInformationRetriever
import uima.ae.qa.en.EnglishQuestionAnalyzer
import uima.ae.qa.ja.JapaneseQuestionAnalyzer
import uima.cpe.IntermediatePoint
import uima.cr.en.EnglishQuestionReader
import uima.cr.ja.JapaneseQuestionReader
import uima.rs.en.EnglishQuestion
import uima.rs.ja.JapaneseQuestion
import us.feliscat.text.StringOption
import us.feliscat.types.Question
import us.feliscat.util.uima.{JCasID, JCasUtils}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * <pre>
  * Created on 2017/03/21.
  * </pre>
  *
  * @author K.Sakamoto
  */
object ReactiveStreamsRunner {
  private val systemName: String = "ReactiveStreamsRunner"
  private var aJCasOpt: Option[JCas] = Option.empty[JCas]

  /*
  private val parallelNumber: Int = 2
  private val parallelNumberOfQuestionAnalyzer:         Int = parallelNumber
  private val parallelNumberOfInformationRetriever:     Int = parallelNumber
  private val parallelNumberOfAnswerGenerator:          Int = parallelNumber
  private val parallelNumberOfAnswerWriterAndEvaluator: Int = parallelNumber
  */

  private var mExamDir = Option.empty[String]
  private val mExamFiles = ListBuffer.empty[File]

  private val questions = ListBuffer.empty[MultiLingualQuestion]
  private val metaData: java.util.Collection[MetaDataObject] = new java.util.ArrayList[MetaDataObject]()

  def main(args: Array[String]): Unit = {
    initialize()
    runPipeline()
  }

  private def initialize(): Unit = {
    println(s">> $systemName Initializing")
    try {
      val collectionReaderDescriptor: String = "src/main/resources/desc/cr/questionCRDescriptor.xml"
      val xmlParser: XMLParser = UIMAFramework.getXMLParser
      val xmlInputSource = new XMLInputSource(collectionReaderDescriptor)
      val aReader: CollectionReaderDescription = xmlParser.parseCollectionReaderDescription(xmlInputSource)
      val aResourceManager: ResourceManager = UIMAFramework.newDefaultResourceManager
      val aReaderInstance: CollectionReader = UIMAFramework.produceCollectionReader(aReader, aResourceManager, null)
      metaData.add(aReaderInstance.getProcessingResourceMetaData)
      val aCAS: CAS = CasCreationUtils.createCas(metaData)
      val aJCas: JCas = aCAS.getJCas
      aJCasOpt = Option(aJCas)

      mExamDir = ModulesConfig.essayExamDirOpt
      mExamDir match {
        case Some(examDir) =>
          val examDirFile = new File(examDir)
          if (examDirFile.canRead && examDirFile.isDirectory) {
            examDirFile.listFiles foreach {
              case file: File if file.canRead && file.isFile && file.getName.endsWith(".xml") =>
                mExamFiles += file
              case _ =>
                // Do nothing
            }
          }
        case None =>
          // Do nothing
      }

      val baseDirOpt = StringOption(Paths.get(mExamDir.get).toAbsolutePath.toString)
      val examFiles: Seq[File] = mExamFiles.result

      questions ++= JapaneseQuestionReader.read(metaData, baseDirOpt, examFiles)
      questions ++= EnglishQuestionReader.read(metaData, baseDirOpt, examFiles)

      questions foreach {
        mq: MultiLingualQuestion =>
          println(mq.casId)
      }
    } catch {
      case e: UIMAException =>
        throw new IllegalStateException(e)
    }
  }

  private def process(mq: MultiLingualQuestion)(proc : (JCasID, JCas, Question) => Unit): Unit = {
    println(mq.question.getLabel)
    val p = Future {
      if (JCasUtils.notContains(mq.casId)) {
        JCasUtils.setAJCas(mq.aJCas.getView(mq.locale.getLanguage))(mq.casId)
      }
      proc(mq.casId, JCasUtils.getAJCas(mq.casId), mq.question)
    }
    Await.ready(p, 10.minutes)
  }

  private def runPipeline(): Unit = {
    println(s">> $systemName Processing")
    if (aJCasOpt.isEmpty || mExamDir.isEmpty || mExamFiles.isEmpty) {
      return
    }

    implicit val system: ActorSystem = ActorSystem(systemName)
    //implicit val timeout = Timeout(1, TimeUnit.HOURS)
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val questionReaderSource: Source[MultiLingualQuestion, NotUsed] = {
      println(s">> ${IntermediatePoint.QuestionReader.name}")
      println(s"# of questions: ${questions.size}")
      questions foreach {
        case mq: MultiLingualQuestion =>
          println(mq.question.getLabel)
        case _ =>
          // Do nothing
      }
      Source(questions.result)
    }

    val answerWriterAndEvaluatorSink: Sink[MultiLingualQuestion, Future[Done]] = {
      val name: String = s"${IntermediatePoint.AnswerWriter.name} and ${IntermediatePoint.AnswerEvaluator.name}"
      println(s">> $name Sink Initializing")
      Sink.foreach {
        mq: MultiLingualQuestion =>
          println(s">> $name Sink Processing")
          mq match {
            case q: EnglishQuestion =>
              println(s">> ${q.locale.getDisplayLanguage} $name Sink Processing for ${q.question.getLabel}")
            case q: JapaneseQuestion =>
              println(s">> ${q.locale.getDisplayLanguage} $name Sink Processing for ${q.question.getLabel}")
            case _ =>
              // Do nothing
          }
      }
    }

    val questionAnalyzerFlow: Flow[MultiLingualQuestion, MultiLingualQuestion, NotUsed] = {
      val name: String = IntermediatePoint.QuestionAnalyzer.name
      println(s">> $name Flow Initializing")
      Flow[MultiLingualQuestion].map {
        mq: MultiLingualQuestion =>
          println(s">> $name Flow Processing")
          mq match {
            case q: EnglishQuestion =>
              println(s">> ${q.locale.getDisplayLanguage} $name Flow Processing for ${q.question.getLabel}")
              process(q){
                (jCasID, aJCas, question) =>
                  EnglishQuestionAnalyzer.processQuestion(aJCas, question)(jCasID)
              }
            case q: JapaneseQuestion =>
              println(s">> ${q.locale.getDisplayLanguage} $name Flow Processing for ${q.question.getLabel}")
              process(q){
                (jCasID, aJCas, question) =>
                  JapaneseQuestionAnalyzer.processQuestion(aJCas, question)(jCasID)
              }
            case _ =>
              // Do nothing
          }
          mq
      }
    }

    val informationRetrieverFlow: Flow[MultiLingualQuestion, MultiLingualQuestion, NotUsed] = {
      val name: String = IntermediatePoint.InformationRetriever.name
      println(s">> $name Flow Initializing")
      Flow[MultiLingualQuestion].map {
        mq: MultiLingualQuestion =>
          println(s">> $name Flow Processing")
          mq match {
            case q: EnglishQuestion =>
              println(s">> ${q.locale.getDisplayLanguage} $name Flow Processing for ${q.question.getLabel}")
              process(q){
                (jCasID, aJCas, question) =>
                  EnglishInformationRetriever.processQuestion(aJCas, question)(jCasID)
              }
            case q: JapaneseQuestion =>
              println(s">> ${q.locale.getDisplayLanguage} $name Flow Processing for ${q.question.getLabel}")
              process(q){
                (jCasID, aJCas, question) =>
                  JapaneseInformationRetriever.processQuestion(aJCas, question)(jCasID)
              }
            case _ =>
            // Do nothing
          }
          mq
      }
    }

    val answerGeneratorFlow: Flow[MultiLingualQuestion, MultiLingualQuestion, NotUsed] = {
      val name: String = IntermediatePoint.AnswerGenerator.name
      println(s">> $name Flow Initializing")
      Flow[MultiLingualQuestion].map {
        mq: MultiLingualQuestion =>
          println(s">> $name Flow Processing")
          mq match {
            case q: EnglishQuestion =>
              println(s">> ${q.locale.getDisplayLanguage} $name Flow Processing for ${q.question.getLabel}")
              process(q){
                (jCasID, aJCas, question) =>
                  EnglishAnswerGenerator.processQuestion(aJCas, question)(jCasID)
              }
            case q: JapaneseQuestion =>
              println(s">> ${q.locale.getDisplayLanguage} $name Flow Processing for ${q.question.getLabel}")
              process(q){
                (jCasID, aJCas, question) =>
                  JapaneseAnswerGenerator.processQuestion(aJCas, question)(jCasID)
              }
            case _ =>
            // Do nothing
          }
          mq
      }
    }

    val graph: RunnableGraph[NotUsed] =
      questionReaderSource.
        via(questionAnalyzerFlow).
        via(informationRetrieverFlow).
        via(answerGeneratorFlow).
        to(answerWriterAndEvaluatorSink)

    graph.run
  }
}
