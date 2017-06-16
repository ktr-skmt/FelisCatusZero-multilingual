name := "FelisCatusZero"

version := "0.0.1"

lazy val javaVersion = "1.8"

val usfeliscat = "us.feliscat"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.2",
  organization := usfeliscat,
  fork in run := true
) ++ {
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8", // yes, this is 2 args
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xlint",
    "-target:jvm-".concat(javaVersion)
  )
} ++ {
  javaOptions in (Test,run) ++= Seq(
    "-Xms2g",
    "-Xmx4g",
    "-Xss2g",
    "-source", javaVersion,
    "-target", javaVersion
  )
} ++ {
  resolvers ++= Seq(
    "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
    "clojars.org" at "http://clojars.org/repo",
    "spotlight-releases-repository" at "https://github.com/dbpedia-spotlight/maven-repo/raw/master/releases"
  )
} ++ {
  libraryDependencies ++= Seq(
    "args4j" % "args4j" % "2.32",// withSources() withJavadoc(),
    //Add Configuration Framework
    "com.typesafe" % "config" % "1.3.0",// withSources() withJavadoc(),
    "com.iheart" %% "ficus" % "1.4.0"// withSources() withJavadoc()
  )
}

val snapshotsStr = "snapshots"
val releasesStr = "releases"
val mavenRepo = "maven-repo"
val fileProtocol = "file://"

def getSnapshotsOrReleases(isSnapshot: Boolean): String = {
  if (isSnapshot) {
    snapshotsStr
  } else {
    releasesStr
  }
}

def getPublishTo(isSnapshot: Boolean, f: String, n: String): Option[Resolver] = {
  val version = getSnapshotsOrReleases(isSnapshot)
  Option(
    Resolver.file(s"$n-$version-repository",
      file(s"$f/$mavenRepo/$version")
    )
  )
}

val uimaLibraries = {
  val uimaGroupId = "org.apache.uima"
  val uimaVersion = "2.8.1"
  val uimaFitVersion = "2.1.0"
  Seq(
    uimaGroupId % "uimaj-core"                % uimaVersion,// withSources() withJavadoc(),
    uimaGroupId % "uimaj-bootstrap"           % uimaVersion,// withSources() withJavadoc(),
    uimaGroupId % "uimaj-tools"               % uimaVersion,// withSources() withJavadoc(),
    uimaGroupId % "uimaj-examples"            % uimaVersion,// withSources() withJavadoc(),
    uimaGroupId % "uimaj-document-annotation" % uimaVersion,// withSources() withJavadoc(),
    uimaGroupId % "uimaj-cpe"                 % uimaVersion,// withSources() withJavadoc(),
    uimaGroupId % "uimaj-adapter-vinci"       % uimaVersion,// withSources() withJavadoc(),
    uimaGroupId % "uimaj-adapter-soap"        % uimaVersion,// withSources() withJavadoc(),
    uimaGroupId % "jVinci"                    % uimaVersion,// withSources() withJavadoc(),
    uimaGroupId % "uimafit-core"           % uimaFitVersion// withSources() withJavadoc()
  )
}

val licensesTemplate = Seq("Apache License Version 2.0" -> url("https://raw.githubusercontent.com/ktr-skmt/FelisCatusZero-multilingual/master/LICENSE"))

val homepageTemplate = Some(url("https://github.com/ktr-skmt/FelisCatusZero-multilingual"))

val pomExtraTemplate = {
  <scm>
    <url>git@github.com:ktr-skmt/FelisCatusZero-multilingual.git</url>
    <connection>scm:git@github.com:ktr-skmt/FelisCatusZero-multilingual.git</connection>
  </scm>
  <developers>
    <developer>
      <id>ktr-skmt</id>
      <name>Kotaro Sakamoto</name>
      <email>kotarosakamoto@nii.ac.jp</email>
      <url>https://linkedin.com/in/kotaro-sakamoto-19168b4a</url>
    </developer>
  </developers>
}

val libraries4uimaFile = "libraries4uima"
val libraries4uimaName = "FelisCatusZeroLibraries4UIMA"
lazy val libraries4uima = (project in file(libraries4uimaFile)).
  settings(commonSettings: _*).
  settings(
    isSnapshot := true
  ).settings(
    version := "0.0.1",
    name := libraries4uimaName,
    organization := usfeliscat,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    publishTo := getPublishTo(isSnapshot.value, libraries4uimaFile, libraries4uimaName),
    licenses := licensesTemplate,
    homepage := homepageTemplate,
    pomExtra := pomExtraTemplate
  ).settings(
    libraryDependencies ++= uimaLibraries
  )

val librariesFile = "libraries"
val librariesName = "FelisCatusZeroLibraries"
lazy val libraries = (project in file(librariesFile)).
  settings(commonSettings: _*).
  settings(//Add NLP Framework
    libraryDependencies ++= {
      val stanfordCoreNLPVersion = "3.7.0"
      Seq(
        "edu.stanford.nlp" % "stanford-corenlp" % stanfordCoreNLPVersion, // withSources() withJavadoc()
        "edu.stanford.nlp" % "stanford-corenlp" % stanfordCoreNLPVersion classifier "models", // withSources() withJavadoc()
        //"edu.stanford.nlp" % "stanford-parser"  % stanfordCoreNLPVersion // withSources() withJavadoc()
        //lucene-analyzers for snowball stemmer
        "org.apache.lucene" % "lucene-analyzers" % "3.6.2" //withSources() withJavadoc()
      )
    }
  ).settings(
    libraryDependencies += "commons-cli" % "commons-cli" % "1.3.1"// withSources() withJavadoc(),
  ).settings(//Add Unit Test Framework
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.12" % "test",// withSources() withJavadoc(),
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",// withSources() withJavadoc(),
      "org.hamcrest" % "hamcrest-all" % "1.3" % "test"// withSources() withJavadoc()
    )
  ).settings(//Add Template Framework
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-xml" % "1.0.5",// withSources() withJavadoc(),
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",// withSources() withJavadoc(),
      "org.jsoup" % "jsoup" % "1.8.2",// withSources() withJavadoc(),
      "info.bliki.wiki" % "bliki-core" % "3.0.19"// withSources() withJavadoc()
    )
  ).settings(//Add Logging Framework
    libraryDependencies ++= {
      val SLF4JVersion = "1.7.22"
      Seq(
        "log4j" % "log4j" % "1.2.17",
        //"org.slf4j" % "slf4j-api" % SLF4JVersion,// withSources() withJavadoc()
        //"org.slf4j" % "slf4j-log4j12" % SLF4JVersion,// withSources() withJavadoc()
        "org.slf4j" % "slf4j-simple" % SLF4JVersion// withSources() withJavadoc()
      )
    }
  ).settings(
    libraryDependencies += "com.github.pathikrit" %% "better-files" % "2.17.1"
  ).settings(
    isSnapshot := true
  ).settings(
    version := "0.0.1",
    name := librariesName,
    organization := usfeliscat,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    publishTo := getPublishTo(isSnapshot.value, librariesFile, librariesName),
    licenses := licensesTemplate,
    homepage := homepageTemplate,
    pomExtra := pomExtraTemplate
  )

val libraries4jcasFile = "libraries4jcas"
val libraries4jcasName = "FelisCatusZeroLibraries4JCas"
lazy val libraries4jcas = (project in file(libraries4jcasFile)).
  settings(commonSettings: _*).
  settings(
    isSnapshot := true
  ).settings(
    version := "0.0.1",
    name := libraries4jcasName,
    organization := usfeliscat,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    publishTo := getPublishTo(isSnapshot.value, libraries4jcasFile, libraries4jcasName),
    licenses := licensesTemplate,
    homepage := homepageTemplate,
    pomExtra := pomExtraTemplate

  ).settings(
    resolvers ++= Seq(
      s"$librariesName-$snapshotsStr-repository" at fileProtocol.concat(Path.absolute(file(s"./$librariesFile/$mavenRepo/$snapshotsStr")).absolutePath),
      s"$libraries4uimaName-$snapshotsStr-repository" at fileProtocol.concat(Path.absolute(file(s"./$libraries4uimaFile/$mavenRepo/$snapshotsStr")).absolutePath)
    )
  ).settings(
    libraryDependencies ++= Seq(
      usfeliscat % s"feliscatuszero${librariesFile}_2.12" % "0.0.1",// from fileProtocol.concat(Path.absolute(file(s"./$librariesFile/$mavenRepo/$snapshotsStr")).absolutePath),
      usfeliscat % s"feliscatuszero${libraries4uimaFile}_2.12" % "0.0.1"// from fileProtocol.concat(Path.absolute(file(s"./$libraries4uimaFile/$mavenRepo/$snapshotsStr")).absolutePath)
    )
  )

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    version := "0.0.1",
    name := "FelisCatusZero-Multilingual"
  ).settings(
    resolvers ++= Seq(
      s"$libraries4jcasName-$snapshotsStr-repository" at fileProtocol.concat(Path.absolute(file(s"./$libraries4jcasFile/$mavenRepo/$snapshotsStr")).absolutePath),
      s"$libraries4uimaName-$snapshotsStr-repository" at fileProtocol.concat(Path.absolute(file(s"./$libraries4uimaFile/$mavenRepo/$snapshotsStr")).absolutePath)
    )
  ).settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.4.17",
      "com.typesafe.akka" %% "akka-stream" % "2.4.17",
      usfeliscat % s"feliscatuszero${libraries4jcasFile}_2.12" % "0.0.1",// from fileProtocol.concat(Path.absolute(file(s"./$libraries4jcasFile/$mavenRepo/$snapshotsStr")).absolutePath),
      usfeliscat % s"feliscatuszero${libraries4uimaFile}_2.12" % "0.0.1"// from fileProtocol.concat(Path.absolute(file(s"./$libraries4uimaFile/$mavenRepo/$snapshotsStr")).absolutePath)
    )
  ).settings(
    libraryDependencies ++= uimaLibraries
  )

val jcasgenFile = "jcasgen"
lazy val jcasgen = (project in file(jcasgenFile)).
  settings(commonSettings: _*).
  settings(
    version := "0.0.1",
    name := "FelisCatusZero-JCasGen"
  ).settings(
    libraryDependencies ++= {
      val uimaGroupId = "org.apache.uima"
      val uimaVersion = "2.8.1"
      Seq(
        uimaGroupId % "uimaj-core"  % uimaVersion,// withSources() withJavadoc(),
        uimaGroupId % "uimaj-tools" % uimaVersion// withSources() withJavadoc(),
      )
    }
  )

commands ++= {
  val t = "t"
  val componentList = Seq[String]("qa", "ir", "ag", "aw", "ae")

  def cpeCommand(from: String, to: String): String = {
    s"run-main uima.cpe.CPERunner -from $from -to $to"
  }

  val commandBuffer = scala.collection.mutable.ListBuffer.empty[Command]

  commandBuffer += {
    Command.command("publishLib4UIMA") {
      state =>
        s"project $libraries4uimaFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          "project root" :: state
    }
  }

  commandBuffer += {
    Command.command("publishLib") {
      state =>
        s"project $librariesFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          "project root" :: state
    }
  }

  commandBuffer += {
    Command.command("publishLib4JCas") {
      state =>
        s"project $libraries4jcasFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          "project root" :: state
    }
  }

  commandBuffer += {
    Command.command("publishLibs") {
      state =>
        s"project $libraries4uimaFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          s"project $librariesFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          s"project $libraries4jcasFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          "project root" :: state
    }
  }

  commandBuffer += {
    Command.command(jcasgenFile) {
      state =>
        s"project $jcasgenFile" ::
          "clean" ::
          "clean-files" ::
          "compile" ::
          s"run-main $usfeliscat.util.JCasGen" ::
          "project root" :: state
    }
  }

  commandBuffer += {
    Command.command("init") {
      state =>
        s"project $jcasgenFile" ::
          "clean" ::
          "clean-files" ::
          "compile" ::
          s"run-main $usfeliscat.util.JCasGen" ::
          s"project $libraries4uimaFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          s"project $librariesFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          s"project $libraries4jcasFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          "project root" ::
          "clean" ::
          "clean-files" ::
          s"run-main $usfeliscat.util.HistoryCleaner" ::
          "run-main uima.cpe.CPERunner -doCharacterLevelIndriIndexInJapanese -doContentWordLevelIndriIndexInJapanese -doTokenLevelIndriIndexInEnglish -doContentWordLevelIndriIndexInEnglish" :: state
    }
  }

  commandBuffer += {
    Command.command("initWithoutIndriBuildIndex") {
      state =>
        s"project $jcasgenFile" ::
          "clean" ::
          "clean-files" ::
          "compile" ::
          s"run-main $usfeliscat.util.JCasGen" ::
          s"project $libraries4uimaFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          s"project $librariesFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          s"project $libraries4jcasFile" ::
          "clean" ::
          "clean-files" ::
          "publish-local" ::
          "project root" ::
          "clean" ::
          "clean-files" ::
          s"run-main $usfeliscat.util.HistoryCleaner" ::
          "run-main uima.cpe.CPERunner" :: state
    }
  }

  commandBuffer += {
    Command.command("ci") {
      state =>
        "clean" ::
          "jcasgen" ::
          "reload" ::
          "publishLibs" ::
          "reload" ::
          "project jcasgen" ::
          "test:compile" ::
          "coverage" ::
          "test" ::
          "coverageReport" ::
          "project libraries4uima" ::
          "test:compile" ::
          "coverage" ::
          "test" ::
          "coverageReport" ::
          "project libraries" ::
          "test:compile" ::
          "coverage" ::
          "test" ::
          "coverageReport" ::
          "project libraries4jcas" ::
          "test:compile" ::
          "coverage" ::
          "test" ::
          "coverageReport" ::
          "project root" ::
          "compile" ::
          "test:compile" ::
          "coverage" ::
          "test" ::
          "coverageReport" ::
          "coverageAggregate" :: state
    }
  }

  commandBuffer += {
    Command.command("clearHistory") {
      state =>
        s"run-main $usfeliscat.util.HistoryCleaner" :: state
    }
  }

  commandBuffer += {
    Command.command(s"e${t}e") {
      state =>
        "run-main uima.cpe.CPERunner" :: state
    }
  }

  for (component <- componentList) {
    commandBuffer += {
      Command.command(component) {
        state =>
          cpeCommand(component, component) :: state
      }
    }
    commandBuffer += {
      Command.command(component.concat(t)) {
        state =>
          cpeCommand(component, componentList.last) :: state
      }
    }
    commandBuffer += {
      Command.command(t.concat(component)) {
        state =>
          cpeCommand(componentList.head, component) :: state
      }
    }
  }
  commandBuffer ++= {
    for (i <- componentList.indices;
         j <- i until componentList.size) yield {
      val componentA = componentList(i)
      val componentB = componentList(j)
      Command.command(s"$componentA$t$componentB") {
        state =>
          cpeCommand(componentA, componentB) :: state
      }
    }
  }

  commandBuffer.result
}
