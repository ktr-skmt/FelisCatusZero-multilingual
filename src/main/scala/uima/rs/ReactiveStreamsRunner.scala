package uima.rs

import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.{Done, NotUsed}
import uima.rs.en.EnglishQuestion
import uima.rs.ja.JapaneseQuestion
import us.feliscat.types.Question

import scala.concurrent.Future

/**
  * <pre>
  * Created on 2017/03/21.
  * </pre>
  *
  * @author K.Sakamoto
  */
object ReactiveStreamsRunner {
  private def pipeline(questions: Seq[MultiLingualQuestion]): Unit = {
    implicit val system = ActorSystem("ReactiveStreamsRunner")
    implicit val materializer = ActorMaterializer()
    val source: Source[MultiLingualQuestion, SourceQueueWithComplete[MultiLingualQuestion]] = {
      Source.queue[MultiLingualQuestion](bufferSize = 65536, OverflowStrategy.backpressure)
    }

    val sink: Sink[MultiLingualQuestion, Future[Done]] = Sink.foreach[MultiLingualQuestion]{
      case q: EnglishQuestion =>

      case q: JapaneseQuestion =>

      case _ =>
    }

    val questionAnalyzerFlow: Flow[MultiLingualQuestion, MultiLingualQuestion, NotUsed] = {
      Flow[MultiLingualQuestion] map {
        mq: MultiLingualQuestion =>
          mq match {
            case q: EnglishQuestion =>
              q.questionOpt foreach {
                question: Question =>
                  //EnglishQuestionAnalyzer.processQuestion(, question)
              }
            case q: JapaneseQuestion =>
              q.questionOpt foreach {
                question: Question =>
                  //JapaneseQuestionAnalyzer.processQuestion(, question)
              }
            case _ =>
              // Do nothing
          }
          mq
      }
    }

    val informationRetrieverFlow: Flow[MultiLingualQuestion, MultiLingualQuestion, NotUsed] = {
      Flow[MultiLingualQuestion] map {
        mq: MultiLingualQuestion =>
          mq match {
            case q: EnglishQuestion =>
              q.questionOpt foreach {
                question: Question =>
                  //EnglishInformationRetriever.processQuestion(, question)
              }
            case q: JapaneseQuestion =>
              q.questionOpt foreach {
                question: Question =>
                  //JapaneseInformationRetriever.processQuestion(, question)
              }
            case _ =>
            // Do nothing
          }
          mq
      }
    }

    val answerGeneratorFlow: Flow[MultiLingualQuestion, MultiLingualQuestion, NotUsed] = {
      Flow[MultiLingualQuestion] map {
        mq: MultiLingualQuestion =>
          mq match {
            case q: EnglishQuestion =>
              q.questionOpt foreach {
                question: Question =>
                  //EnglishAnswerGenerator.processQuestion(, question)
              }
            case q: JapaneseQuestion =>
              q.questionOpt foreach {
                question: Question =>
                  //JapaneseAnswerGenerator.processQuestion(, question)
              }
            case _ =>
            // Do nothing
          }
          mq
      }
    }

    val answerWriterFlow: Flow[MultiLingualQuestion, MultiLingualQuestion, NotUsed] = {
      Flow[MultiLingualQuestion] map {
        mq: MultiLingualQuestion =>
          mq match {
            case q: EnglishQuestion =>
              q.questionOpt foreach {
                question: Question =>
                  question
              }
            case q: JapaneseQuestion =>
              q.questionOpt foreach {
                question: Question =>
                  question
              }
            case _ =>
            // Do nothing
          }
          mq
      }
    }

    val answerEvaluatorFlow: Flow[MultiLingualQuestion, MultiLingualQuestion, NotUsed] = {
      Flow[MultiLingualQuestion] map {
        mq: MultiLingualQuestion =>
          mq match {
            case q: EnglishQuestion =>
              q.questionOpt foreach {
                question: Question =>
                  question
              }
            case q: JapaneseQuestion =>
              q.questionOpt foreach {
                question: Question =>
                  question
              }
            case _ =>
            // Do nothing
          }
          mq
      }
    }

    val graph: RunnableGraph[SourceQueueWithComplete[MultiLingualQuestion]] = {
      source via
        questionAnalyzerFlow via
        informationRetrieverFlow via
        answerGeneratorFlow via
        answerWriterFlow via
        answerEvaluatorFlow to
        sink
    }

    graph.run()
  }
}
