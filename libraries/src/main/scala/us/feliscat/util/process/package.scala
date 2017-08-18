package us.feliscat.util

import java.io.{BufferedWriter, InputStream, OutputStream, OutputStreamWriter}
import java.nio.charset.{Charset, CodingErrorAction}

import us.feliscat.text.{StringNone, StringOption}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, Future, Promise, TimeoutException}
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.{Codec, Source}
import scala.sys.process.{Process, ProcessBuilder, ProcessIO}
import scala.util.{Failure, Success, Try}

/**
  * @author K. Sakamoto
  *         Created on 2017/05/24
  */
package object process {
  private val cache: mutable.WeakHashMap[String, Seq[String]] = mutable.WeakHashMap.empty[String, Seq[String]]

  implicit class ProcessBuilderUtils(repr: ProcessBuilder) {
    def lineStream(encoding: Charset,
                   onMalformedInput: CodingErrorAction,
                   onUnmappableCharacter: CodingErrorAction,
                   replacementOpt: StringOption,
                   timeout: FiniteDuration): Iterator[String] = {
      lineStreamTry(
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
      lineStreamTry(
        encoding,
        onMalformedInput,
        onUnmappableCharacter,
        replacementOpt,
        inputText,
        needInputText = true,
        timeout)
    }

    private def lineStreamTry(encoding: Charset,
                              onMalformedInput: CodingErrorAction,
                              onUnmappableCharacter: CodingErrorAction,
                              replacementOpt: StringOption,
                              inputText: StringOption,
                              needInputText: Boolean,
                              timeout: FiniteDuration): Iterator[String] = {
      Try(
        lineStream(
          encoding,
          onMalformedInput,
          onUnmappableCharacter,
          replacementOpt,
          inputText,
          needInputText,
          timeout
        )
      ) match {
        case Success(result) =>
          result
        case Failure(err) =>
          err.printStackTrace(System.err)
          Iterator.empty
      }
    }

    @throws[TimeoutException]
    private def lineStream(encoding: Charset,
                           onMalformedInput: CodingErrorAction,
                           onUnmappableCharacter: CodingErrorAction,
                           replacementOpt: StringOption,
                           inputText: StringOption,
                           needInputText: Boolean,
                           timeout: FiniteDuration): Iterator[String] = {

      if (needInputText && inputText.isEmpty) {
        return Iterator.empty
      }

      val key: String = {
        if (needInputText) {
          repr.toString.concat(inputText.get.trim)
        } else {
          repr.toString
        }
      }

      if (cache.contains(key)) {
        return cache(key).iterator
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
              if (LibrariesConfig.runMode == RunModes.ProcessDetail) {
                println(correctText)
              }
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
              if (LibrariesConfig.runMode == RunModes.ProcessDetail) {
                println(line)
              }
              lineBuffer += line
          }
          in.close()
          val lines: Seq[String] = lineBuffer.result
          cache.put(key, lines)
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

      var result: Future[Iterator[String]] = Future(Iterator.empty)

      processFuture onComplete {
        case Success(_) =>
          result = promise.future
        case Failure(err) =>
          System.err.println(key)
          err.printStackTrace(System.err)
      }

      Await.ready(processFuture, timeout)

      result.onComplete {
        case Success(_) =>
        case Failure(err) =>
          System.err.println(key)
          err.printStackTrace(System.err)
      }

      Await.result(result, timeout)
    }
  }
}
