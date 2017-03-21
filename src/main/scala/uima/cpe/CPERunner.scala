package uima.cpe

import java.io.{BufferedReader, File, InputStreamReader}

import modules.converter.QALabDataset2MultiLingualQACorpusConverter
import modules.ir.fulltext.indri.en.{EnglishIndriContentWordLevelIndexer, EnglishIndriTokenLevelIndexer}
import modules.ir.fulltext.indri.ja.{JapaneseIndriCharactereLevelIndexer, JapaneseIndriContentWordLevelIndexer}
import modules.text.vector.wordembedding.fastText.FastTextVectorGenerator
import org.apache.uima.UIMAFramework
import org.apache.uima.cas.CAS
import org.apache.uima.collection._
import org.apache.uima.collection.metadata.CpeDescription
import org.apache.uima.util.XMLInputSource
import org.kohsuke.args4j.{CmdLineException, CmdLineParser}
import uima.fc._
import us.feliscat.text.StringOption
import util.Config

import scala.util.control.Breaks

/**
  * @author K.Sakamoto
  *         Created on 15/10/30
  */
object CPERunner extends Thread {
  private var cpeOption = Option.empty[CollectionProcessingEngine]
  private var startTimeOption        = Option.empty[Long]
  private var initCompleteTimeOption = Option.empty[Long]

  private def startPoint(code: StringOption): IntermediatePoint = {
    IntermediatePoint.get(code, IntermediatePoint.QuestionReader)
  }

  private def endPoint(code: StringOption): IntermediatePoint = {
    IntermediatePoint.get(code, IntermediatePoint.AnswerEvaluator)
  }

  private def preProcess(option: CPERunnerOption): Unit = {
    if (option.getDoCharacterLevelIndriIndexInJapanese) {
      Config.doCharacterLevelIndriIndexAsPreprocessInJapanese = true
    }

    if (option.getDoContentWordLevelIndriIndexInJapanese) {
      Config.doContentWordLevelIndriIndexAsPreprocessInJapanese = true
    }

    if (option.getDoTokenLevelIndriIndexInEnglish) {
      Config.doTokenLevelIndriIndexAsPreprocessInEnglish = true
    }

    if (option.getDoContentWordLevelIndriIndexInEnglish) {
      Config.doContentWordLevelIndriIndexAsPreprocessInEnglish = true
    }

    if (option.getDoFastText) {
      Config.doFastTestAsPreprocess = true
    }

    if (option.wantToOutputForQALabExtractionSubtask) {
      Config.wantToOutputForQALabExtractionSubtask = true
    }

    if (option.wantToOutputForQALabSummarizationSubtask) {
      Config.wantToOutputForQALabSummarizationSubtask = true
    }

    if (option.wantToOutputForQALabEvaluationMethodSubtask) {
      Config.wantToOutputForQALabEvaluationMethodSubtask = true
    }

    if (Config.doCharacterLevelIndriIndexAsPreprocessInJapanese) {
      JapaneseIndriCharactereLevelIndexer.run()
    }

    if (Config.doContentWordLevelIndriIndexAsPreprocessInJapanese) {
      JapaneseIndriContentWordLevelIndexer.run()
    }

    if (Config.doTokenLevelIndriIndexAsPreprocessInEnglish) {
      EnglishIndriTokenLevelIndexer.run()
    }

    if (Config.doContentWordLevelIndriIndexAsPreprocessInEnglish) {
      EnglishIndriContentWordLevelIndexer.run()
    }

    if (Config.doFastTestAsPreprocess) {
      FastTextVectorGenerator.main(Array.empty[String])
    }

    QALabDataset2MultiLingualQACorpusConverter.convert()
  }

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val option = new CPERunnerOption()
    if (args.nonEmpty) {
      val parser = new CmdLineParser(option)
      try {
        parser.parseArgument(args: _*)
      } catch {
        case e: CmdLineException =>
          parser.printUsage(System.out)
          e.printStackTrace()
          System.exit(0)
      }
    }

    preProcess(option)

    startTimeOption = Option(System.nanoTime)
    println(">> Collection Processing Engine Processing")
    val startPointOpt = StringOption(option.getStartPoint)
    val startPointValue: IntermediatePoint = startPoint(startPointOpt)

    val endPointOpt = StringOption(option.getEndPoint)
    val endPointValue: IntermediatePoint = endPoint(endPointOpt)

    if (endPointValue.id < startPointValue.id) {
      System.err.println("Error: The start point was after the end point. They must be in regular order.")
      System.exit(1)
    }

    // Flow Controller
    println(">> Flow Controller Initializing")
    FlowController.clear()

    def needFlow(intermediatePoint: IntermediatePoint): Boolean = {
      startPointValue.id <= intermediatePoint.id && intermediatePoint.id <= endPointValue.id
    }

    val needQuestionAnalyzer:     Boolean = needFlow(IntermediatePoint.QuestionAnalyzer)
    val needInformationRetriever: Boolean = needFlow(IntermediatePoint.InformationRetriever)
    val needAnswerGenerator:      Boolean = needFlow(IntermediatePoint.AnswerGenerator)
    val needAnswerWriter:         Boolean = needFlow(IntermediatePoint.AnswerWriter)
    val needAnswerEvaluator:      Boolean = needFlow(IntermediatePoint.AnswerEvaluator)

    if (needQuestionAnalyzer) {
      println(s">> ${IntermediatePoint.QuestionAnalyzer.name} Flow Controller Initializing")
      FlowController.setAnalysisEngine(IntermediatePoint.QuestionAnalyzer.descriptor.get)
      QuestionAnalyzerFlowController.setAnalysisEngine(IntermediatePoint.QuestionAnalyzer.primitiveDescriptor.get)
    } else {
      QuestionAnalyzerFlowController.clear()
    }

    if (needInformationRetriever) {
      println(s">> ${IntermediatePoint.InformationRetriever.name} Flow Controller Initializing")
      FlowController.setAnalysisEngine(IntermediatePoint.InformationRetriever.descriptor.get)
      InformationRetrieverFlowController.setAnalysisEngine(IntermediatePoint.InformationRetriever.primitiveDescriptor.get)
      if (Config.wantToOutputForQALabExtractionSubtask) {
        InformationRetrieverFlowController.setAnalysisEngine("qalabExtractionSubtaskCCDescriptor")
      }
    } else {
      InformationRetrieverFlowController.clear()
    }

    if (needAnswerGenerator || needAnswerWriter || needAnswerEvaluator) {
      FlowController.setAnalysisEngine(IntermediatePoint.AnswerGenerator.descriptor.get)

      if (needFlow(IntermediatePoint.AnswerGenerator)) {
        AnswerGeneratorFlowController.setAnalysisEngine(IntermediatePoint.AnswerGenerator.primitiveDescriptor.get)
      }

      if (Config.wantToOutputForQALabSummarizationSubtask) {
        AnswerGeneratorFlowController.setAnalysisEngine("qalabSummarizationSubtaskCCDescriptor")
      }

      if (Config.wantToOutputForQALabEvaluationMethodSubtask) {
        AnswerGeneratorFlowController.setAnalysisEngine("qalabEvaluationMethodSubtaskCCDescriptor")
      }

      if (needFlow(IntermediatePoint.AnswerWriter)) {
        AnswerGeneratorFlowController.setAnalysisEngine(IntermediatePoint.AnswerWriter.descriptor.get)
      }

      if (needFlow(IntermediatePoint.AnswerEvaluator)) {
        AnswerGeneratorFlowController.setAnalysisEngine(IntermediatePoint.AnswerEvaluator.descriptor.get)
      }
    } else {
      AnswerGeneratorFlowController.clear()
    }

    val gzipXmiCasConsumerDescriptor: String = "gzipXmiCasConsumerDescriptor"

    if (option.unSave != "all") {
      val unSavedStates: Array[String] = option.unSave.split(',').map(_.trim.toLowerCase)
      if (FlowController.hasAnalysisEngine(IntermediatePoint.QuestionAnalyzer.descriptor.get) &&
        (!(unSavedStates.contains(IntermediatePoint.QuestionAnalyzer.code) || unSavedStates.contains(IntermediatePoint.QuestionAnalyzer.name.toLowerCase)))) {
        val index: Int = QuestionAnalyzerFlowController.indexOf(IntermediatePoint.QuestionAnalyzer.primitiveDescriptor.get) + 1
        QuestionAnalyzerFlowController.insert(index, gzipXmiCasConsumerDescriptor)
      }
      if (FlowController.hasAnalysisEngine(IntermediatePoint.InformationRetriever.descriptor.get) &&
        (!(unSavedStates.contains(IntermediatePoint.InformationRetriever.code) || unSavedStates.contains(IntermediatePoint.InformationRetriever.name.toLowerCase)))) {
        val index: Int = InformationRetrieverFlowController.indexOf(IntermediatePoint.InformationRetriever.primitiveDescriptor.get) + 1
        InformationRetrieverFlowController.insert(index, gzipXmiCasConsumerDescriptor)
      }
      if (FlowController.hasAnalysisEngine(IntermediatePoint.AnswerGenerator.descriptor.get) &&
        (!(unSavedStates.contains(IntermediatePoint.AnswerGenerator.code) || unSavedStates.contains(IntermediatePoint.AnswerGenerator.name.toLowerCase)))) {
        val index: Int = AnswerGeneratorFlowController.indexOf(IntermediatePoint.AnswerGenerator.primitiveDescriptor.get) + 1
        AnswerGeneratorFlowController.insert(index, gzipXmiCasConsumerDescriptor)
      }
    }

    val useIntermediatePoint: Boolean = {
      startPointValue match {
        case IntermediatePoint.InformationRetriever |
             IntermediatePoint.AnswerGenerator |
             IntermediatePoint.AnswerWriter |
             IntermediatePoint.AnswerEvaluator =>
          true
        case _ =>
          false
      }
    }

    val cpeDescriptor: String = raw"cpe${if (useIntermediatePoint) "FromIntermediatePoint" else ""}Descriptor.xml"
    println("Collection Processing Engine:")
    print("* ")
    println(cpeDescriptor)

    FlowController.printAnalysisEngines()
    QuestionAnalyzerFlowController.printAnalysisEngines()
    InformationRetrieverFlowController.printAnalysisEngines()
    AnswerGeneratorFlowController.printAnalysisEngines()

    val filePath: String = {
      new File(
        raw"src/main/resources/desc/cpe/$cpeDescriptor"
      ).toPath.toAbsolutePath.toString
    }

    val cpeDesc: CpeDescription = UIMAFramework.getXMLParser.parseCpeDescription(new XMLInputSource(filePath))
    cpeOption = Option(UIMAFramework.produceCollectionProcessingEngine(cpeDesc))
    cpeOption match {
      case Some(cpe) =>
        /*
        val casProcessors: Array[CasProcessor] = cpe.getCasProcessors
        val aae: AnalysisEngine = casProcessors(0).asInstanceOf[AnalysisEngine]
        val essayEvaluator: CasConsumer = casProcessors(1).asInstanceOf[CasConsumer]
        */
        if (useIntermediatePoint) {
          val collectionReader: CollectionReader = cpe.getCollectionReader.asInstanceOf[CollectionReader]
          collectionReader.setConfigParameterValue(
            "InputDirectory",
            s"out/xmi/${
              startPointValue match {
                case IntermediatePoint.InformationRetriever =>
                  IntermediatePoint.QuestionAnalyzer.code
                case IntermediatePoint.AnswerGenerator =>
                  IntermediatePoint.InformationRetriever.code
                case IntermediatePoint.AnswerWriter | IntermediatePoint.AnswerEvaluator =>
                  IntermediatePoint.AnswerGenerator.code
                case _ =>
                  IntermediatePoint.QuestionAnalyzer.code
              }
            }"
          )
          println(">> Collection Reader Reconfiguration Started")
          collectionReader.reconfigure()
        }
        cpe.addStatusCallbackListener(new StatusCallbackListenerImpl())
        cpe.process()

        val loop = new Breaks()
        loop.breakable {
          Iterator.continually(Option(
            new BufferedReader(
              new InputStreamReader(
                System.in)).readLine)) foreach {
            line: Option[String] =>
              if (line.isDefined && (line.get == "abort") && cpe.isProcessing) {
                println("Aborting...")
                cpe.stop()
                loop.break()
              }
          }
        }
      case None =>
      //Do nothing
    }
  }

  class StatusCallbackListenerImpl extends StatusCallbackListener {
    private var entityCount: Int = 0
    private var size: Int = 0

    override def entityProcessComplete(aCAS: CAS, aStatus: EntityProcessStatus): Unit = {
      if (aStatus.isException) {
        val exceptions: java.util.List[Exception] = aStatus.getExceptions
        for (i <- 0 until exceptions.size) {
          exceptions.get(i).asInstanceOf[Throwable].printStackTrace()
        }
        return
      }
      entityCount += 1
      val docText: Option[String] = Option(aCAS.getDocumentText)
      docText match {
        case Some(dt) =>
          size += dt.codePointCount(0, dt.length)
        case None =>
        //Do nothing
      }
    }

    override def resumed(): Unit = {
      println("Resumed")
    }

    override def initializationComplete(): Unit = {
      println("Collection Processing Management Initialization Complete")
      initCompleteTimeOption = Option(System.nanoTime)
    }

    override def paused(): Unit = {
      println("Paused")
    }

    private def printNumberOfDocumentsAndCharacters(): Unit = {
      printf("Completed %d documents", entityCount)
      if (0 < size) {
        printf("; %d characters", size)
      }
      println()
    }

    override def collectionProcessComplete(): Unit = {
      val time: Option[Long] = Option(System.nanoTime)
      printNumberOfDocumentsAndCharacters()
      val initTime:       Long = initCompleteTimeOption.getOrElse(0L) - startTimeOption.getOrElse(0L)
      val processingTime: Long = time.getOrElse(0L) - initCompleteTimeOption.getOrElse(0L)
      val elapsedTime:    Long = initTime + processingTime

      printf("Total Time Elapsed: %d nano seconds%n", elapsedTime)
      printf("Initialization Time: %d nano seconds%n", initTime)
      printf("Processing Time: %d nano seconds%n", processingTime)

      cpeOption match {
        case Some(cpe) =>
          printf("%n%n ------------------ PERFORMANCE REPORT ------------------%n%n")
          println(cpe.getPerformanceReport.toString)
        case None =>
        //Do nothing
      }

      // exit safely
      System.exit(0)
    }

    override def batchProcessComplete(): Unit = {
      printNumberOfDocumentsAndCharacters()
      printf("Time Elapsed: %d nano seconds%n", System.nanoTime - startTimeOption.getOrElse(0L))

      // exit safely
      System.exit(0)
    }

    override def aborted(): Unit = {
      println("Aborted")
      System.exit(1)
    }
  }
}