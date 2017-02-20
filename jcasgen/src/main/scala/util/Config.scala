package util

import java.io.File

import com.typesafe.config.{Config => TypeSafeConfig, ConfigFactory}
import net.ceedubs.ficus.Ficus._

/**
  * <pre>
  * Created on 2017/02/20.
  * </pre>
  *
  * @author K.Sakamoto
  */
object Config {
  final private[this] var config: TypeSafeConfig = ConfigFactory.load()

  def set(configFile: File): Unit = {
    config = ConfigFactory.load(ConfigFactory.parseFile(configFile))
  }

  final lazy val jCasGenTypeSystemDir: String = config.as[Option[String]]("jCasGen.typeSystemDir").getOrElse("../../src/main/resources/desc/ts")

  final lazy val jCasGenOutputDir: String = config.as[Option[String]]("jCasGen.outputDir").getOrElse("../../src/main/java")
}
