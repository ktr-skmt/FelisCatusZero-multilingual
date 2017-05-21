package us.feliscat.util.process

import java.io._
import java.nio.charset.{Charset, CodingErrorAction}

import us.feliscat.text.{StringNone, StringOption}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise, TimeoutException}
import scala.io.{Codec, Source}
import scala.sys.process.{Process, ProcessBuilder, ProcessIO}
import scala.util.{Failure, Success}

/**
  * @author K.Sakamoto
  *         Created on 2016/09/14
  */
object ProcessBuilderUtils {
  implicit def processToProcessUtils(repr: ProcessBuilder): ProcessBuilderUtils = {
    new ProcessBuilderUtils(repr)
  }

  val cache: mutable.WeakHashMap[String, Seq[String]] = mutable.WeakHashMap.empty[String, Seq[String]]
}

/**
  * @author K.Sakamoto
  * @param repr process builder
  */
class ProcessBuilderUtils(repr: ProcessBuilder) {


  def lineStream(encoding: Charset,
                 onMalformedInput: CodingErrorAction,
                 onUnmappableCharacter: CodingErrorAction,
                 replacementOpt: StringOption,
                 timeout: FiniteDuration): Iterator[String] = {
    lineStream(
      encoding,
      onMalformedInput,
      onUnmappableCharacter,
      replacementOpt,
      StringNone,
      needInputText = false,
      timeout)
  }

  def lineStream(encoding: Charset,
                 onMalformedInput: CodingErrorAction,
                 onUnmappableCharacter: CodingErrorAction,
                 replacementOpt: StringOption,
                 inputText: StringOption,
                 timeout: FiniteDuration): Iterator[String] = {
    lineStream(
      encoding,
      onMalformedInput,
      onUnmappableCharacter,
      replacementOpt,
      inputText,
      needInputText = true,
      timeout)

  }
  def lineStream(encoding: Charset,
                 onMalformedInput: CodingErrorAction,
                 onUnmappableCharacter: CodingErrorAction,
                 replacementOpt: StringOption,
                 inputText: StringOption,
                 needInputText: Boolean,
                 timeout: FiniteDuration): Iterator[String] = {
    var result: Iterator[String] = Iterator.empty

    if (needInputText && inputText.isEmpty) {
      return result
    }

    val key: String = {
      if (needInputText) {
        repr.toString.concat(inputText.get.trim)
      } else {
        repr.toString
      }
    }

    if (ProcessBuilderUtils.cache.contains(key)) {
      return ProcessBuilderUtils.cache(key).iterator
    }

    val promise = Promise[Iterator[String]]

    implicit val codec = Codec(encoding).
      onMalformedInput(onMalformedInput).
      onUnmappableCharacter(onUnmappableCharacter)

    if (replacementOpt.nonEmpty) {
      codec.decodingReplaceWith(replacementOpt.get)
      codec.encodingReplaceWith(replacementOpt.get.getBytes)
    }

    def writeJob(out: OutputStream): Unit = {
      if (needInputText) {
        inputText foreach {
          text: String =>
            val outputStreamWriter = new OutputStreamWriter(out, encoding)
            val writer = new BufferedWriter(outputStreamWriter)
            val correctText: String = Source.
              fromBytes(text.getBytes). //Codec
              getLines.
              mkString("\n")
            writer.write(correctText)
            writer.write('\n')
            writer.close()
            outputStreamWriter.close()
        }
      }
      out.close()
    }

    def readJob(in: InputStream): Unit = {
      if (!promise.isCompleted) {
        val lineBuffer = ListBuffer.empty[String]
        Source.
          fromInputStream(in). //Codec
          getLines foreach {
          line: String =>
            lineBuffer += line
        }
        in.close()
        val lines: Seq[String] = lineBuffer.result
        ProcessBuilderUtils.cache.put(key, lines)
        val iterator: Iterator[String] = lines.iterator
        promise.success(iterator)
      }
    }

    def errorJob(err: InputStream): Unit = {
      Source.
        fromInputStream(err). //Codec
        getLines.
        foreach(System.err.println)
      err.close()
    }

    val io = new ProcessIO(
      writeJob,
      readJob,
      errorJob,
      daemonizeThreads = false)

    val processFuture: Future[Int] = Future {
      val process: Process = repr.run(io)
      while (!promise.isCompleted) {
        Thread.sleep(1000)
      }
      process.exitValue
    }

    processFuture onComplete {
      case Success(_) =>
        val resultFuture: Future[Iterator[String]] = promise.future

        resultFuture onComplete {
          case Success(ret) =>
            result = ret
          case Failure(err) =>
            System.err.println(key)
            err.printStackTrace(System.err)
        }

      case Failure(err) =>
        System.err.println(key)
        err.printStackTrace(System.err)
    }

    try {
      Await.ready(processFuture, timeout)
    } catch {
      case e: TimeoutException =>
        System.err.println(key)
        e.printStackTrace(System.err)
    }

    result
  }
}
