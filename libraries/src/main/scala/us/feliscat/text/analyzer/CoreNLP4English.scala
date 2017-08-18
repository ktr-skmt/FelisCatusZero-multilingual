package us.feliscat.text.analyzer

import java.io.{BufferedReader, PrintWriter, StringWriter}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}
import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations._
import edu.stanford.nlp.ling.{CoreAnnotations, CoreLabel}
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.util.CoreMap
import org.apache.log4j.BasicConfigurator
import org.tartarus.snowball.ext.EnglishStemmer
import us.feliscat.text.{StringNone, StringOption, StringSome}
import us.feliscat.util.LibrariesConfig

import scala.collection.mutable.ListBuffer

/**
  * <pre>
  * Created on 2017/02/05.
  * </pre>
  *
  * @author K.Sakamoto
  */
object CoreNLP4English {
  BasicConfigurator.configure()
  private val properties = new Properties()
  properties.put("annotators", "tokenize,ssplit,pos,lemma")
  properties.put("tokenize.language", "English")
  private val pipeline = new StanfordCoreNLP(properties)

  val stopWords: Seq[String] = {
    val path: Path = Paths.get(LibrariesConfig.resourcesDir, "normalizer", "en", "stop_words.txt")
    val reader: BufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)
    val iterator: java.util.Iterator[String] = reader.lines.iterator
    val buffer = ListBuffer.empty[String]
    while (iterator.hasNext) {
      StringOption(iterator.next.trim) match {
        case StringSome(line) =>
          buffer += line
        case StringNone =>
          // Do nothing
      }
    }
    reader.close()
    buffer.result
  }

  def tagPOS(textOpt: StringOption): Seq[(String, String)] = {
    if (textOpt.isEmpty) {
      return Nil
    }
    val text: String = textOpt.get
    val buffer = ListBuffer.empty[(String, String)]
    val document = new Annotation(text)
    pipeline.annotate(document)
    val sentenceIterator: java.util.Iterator[CoreMap] = document.get(classOf[CoreAnnotations.SentencesAnnotation]).iterator
    while (sentenceIterator.hasNext) {
      val sentence: CoreMap = sentenceIterator.next
      val tokenIterator: java.util.Iterator[CoreLabel] = sentence.get(classOf[CoreAnnotations.TokensAnnotation]).iterator
      while (tokenIterator.hasNext) {
        val token: CoreLabel = tokenIterator.next
        val word: String = token.get(classOf[CoreAnnotations.TextAnnotation])
        val pos: String = token.get(classOf[CoreAnnotations.PartOfSpeechAnnotation])
        buffer += ((word, pos))
      }
    }
    buffer.result
  }

  val stopPOSes: Seq[String] = {
    "DT" :: "IN" :: "PDT" :: "SYM" :: "WDT" :: "." :: "," :: "“" :: "“" :: "(" :: ")" :: ":" :: "-LRB-" :: "-RRB-" :: "``" :: "''" :: Nil
  }

  private val stemmer = new EnglishStemmer()

  def stemming(lemmaOpt: StringOption): StringOption = {
    lemmaOpt map {
      lemma: String =>
        stemmer.setCurrent(lemma)
        stemmer.stem()
        stemmer.getCurrent
      }
  }

  def extractContentWords(textOpt: StringOption): Seq[String] = {
    if (textOpt.isEmpty) {
      return Nil
    }
    val buffer = ListBuffer.empty[String]
    val text: String = textOpt.get
    val document = new Annotation(text)
    pipeline.annotate(document)
    val sentenceIterator: java.util.Iterator[CoreMap] = document.get(classOf[SentencesAnnotation]).iterator
    while (sentenceIterator.hasNext) {
      val sentence: CoreMap = sentenceIterator.next
      val tokenIterator: java.util.Iterator[CoreLabel] = sentence.get(classOf[TokensAnnotation]).iterator
      while (tokenIterator.hasNext) {
        val token: CoreLabel = tokenIterator.next
        val word:  String = token.get(classOf[TextAnnotation])
        if (!stopWords.contains(word)) {
          val pos: String = token.get(classOf[PartOfSpeechAnnotation])
          if (!stopPOSes.contains(pos)) {
            val lemma: String = token.get(classOf[LemmaAnnotation])
            stemming(StringOption(lemma)) match {
              case StringSome(stemmedWord) =>
                buffer += stemmedWord
              case StringNone =>
                // Do nothing
            }
          }
        }
      }
    }
    buffer.result
  }

  def ssplit(textOpt: StringOption): Seq[String] = {
    if (textOpt.isEmpty) {
      return Nil
    }
    val text: String = textOpt.get
    val document = new Annotation(text)
    pipeline.annotate(document)
    val sentenceIterator: java.util.Iterator[CoreMap] = document.get(classOf[SentencesAnnotation]).iterator
    val sentenceBuffer = ListBuffer.empty[String]
    while (sentenceIterator.hasNext) {
      val sentence: CoreMap = sentenceIterator.next
      sentenceBuffer += sentence.get(classOf[TextAnnotation])
    }
    sentenceBuffer.result
  }

  def analysisResult(textOpt: StringOption): StringOption = {
    if (textOpt.isEmpty) {
      return StringNone
    }
    val text: String = textOpt.get
    val document = new Annotation(text)
    pipeline.annotate(document)
    val stringWriter = new StringWriter()
    val printWriter = new PrintWriter(stringWriter)
    pipeline.prettyPrint(document, printWriter)
    StringOption(stringWriter.toString)
  }
}
