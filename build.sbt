name := "FelisCatusZero"

version := "0.0.1"

lazy val javaVersion = "1.8"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.1",
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
    "spotlight-releases-repository" at "https://github.com/dbpedia-spotlight/maven-repo/raw/master/releases")
} ++ {
  libraryDependencies ++= {
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
      uimaGroupId % "uimafit-core"           % uimaFitVersion,// withSources() withJavadoc()
      "args4j" % "args4j" % "2.32",// withSources() withJavadoc(),
      //Add Configuration Framework
      "com.typesafe" % "config" % "1.3.0",// withSources() withJavadoc(),
      "com.iheart" %% "ficus" % "1.4.0"// withSources() withJavadoc()
    )
  }
}

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    version := "0.1.0",
    name := "FelisCatusZero-multilingual"
  ).settings(//Add NLP Framework
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
  )).settings(//Add Template Framework
  libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-xml" % "1.0.5",// withSources() withJavadoc(),
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",// withSources() withJavadoc(),
    "org.jsoup" % "jsoup" % "1.8.2",// withSources() withJavadoc(),
    "info.bliki.wiki" % "bliki-core" % "3.0.19"// withSources() withJavadoc()
  )).settings(//Add Logging Framework
  libraryDependencies ++= {
    val SLF4JVersion = "1.7.22"
    Seq(
      "log4j" % "log4j" % "1.2.17",
      //"org.slf4j" % "slf4j-api" % SLF4JVersion,// withSources() withJavadoc()
      //"org.slf4j" % "slf4j-log4j12" % SLF4JVersion,// withSources() withJavadoc()
      "org.slf4j" % "slf4j-simple" % SLF4JVersion// withSources() withJavadoc()
    )
  }).settings(
  libraryDependencies += "com.github.pathikrit" %% "better-files" % "2.17.1"
  )

lazy val jcasgen = (project in file("jcasgen")).
  settings(commonSettings: _*).
  settings(
    version := "0.0.1",
    name := "FelisCatusZero-JCasGen"
  )

commands ++= Seq(
  Command.command("jcasgen") {
    state =>
      "project jcasgen" ::
      "run-main util.JCasGen" ::
      "project root" :: state
  },
  Command.command("init") {
    state =>
      "project jcasgen" ::
      "run-main util.JCasGen" ::
      "project root" ::
      "run-main uima.cpe.CPERunner -doCharacterLevelIndriIndexInJapanese -doContentWordLevelIndriIndexInJapanese -doTokenLevelIndriIndexInEnglish -doContentWordLevelIndriIndexInEnglish" :: state
  },
  Command.command("clearHistory") {
    state =>
      "run-main util.HistoryCleaner" :: state
  },
  Command.command("e2e") {
    state =>
      "run-main uima.cpe.CPERunner" :: state
  },
  Command.command("qa") {
    state =>
      "run-main uima.cpe.CPERunner -from qa -to qa" :: state
  },
  Command.command("ir") {
    state =>
      "run-main uima.cpe.CPERunner -from ir -to ir" :: state
  },
  Command.command("eg") {
    state =>
      "run-main uima.cpe.CPERunner -from eg -to eg" :: state
  },
  Command.command("w") {
    state =>
      "run-main uima.cpe.CPERunner -from w -to w" :: state
  },
  Command.command("e") {
    state =>
      "run-main uima.cpe.CPERunner -from e -to e" :: state
  },
  Command.command("qa-") {
    state =>
      "run-main uima.cpe.CPERunner -from qa" :: state
  },
  Command.command("ir-") {
    state =>
      "run-main uima.cpe.CPERunner -from ir" :: state
  },
  Command.command("eg-") {
    state =>
      "run-main uima.cpe.CPERunner -from eg" :: state
  },
  Command.command("w-") {
    state =>
      "run-main uima.cpe.CPERunner -from w" :: state
  },
  Command.command("e-") {
    state =>
      "run-main uima.cpe.CPERunner -from e" :: state
  },
  Command.command("qa-qa") {
    state =>
      "run-main uima.cpe.CPERunner -from qa -to qa" :: state
  },
  Command.command("qa-ir") {
    state =>
      "run-main uima.cpe.CPERunner -from qa -to ir" :: state
  },
  Command.command("qa-eg") {
    state =>
      "run-main uima.cpe.CPERunner -from qa -to eg" :: state
  },
  Command.command("qa-w") {
    state =>
      "run-main uima.cpe.CPERunner -from qa -to w" :: state
  },
  Command.command("qa-e") {
    state =>
      "run-main uima.cpe.CPERunner -from qa -to e" :: state
  },
  Command.command("ir-ir") {
    state =>
      "run-main uima.cpe.CPERunner -from ir -to ir" :: state
  },
  Command.command("ir-eg") {
    state =>
      "run-main uima.cpe.CPERunner -from ir -to eg" :: state
  },
  Command.command("ir-w") {
    state =>
      "run-main uima.cpe.CPERunner -from ir -to w" :: state
  },
  Command.command("ir-e") {
    state =>
      "run-main uima.cpe.CPERunner -from ir -to e" :: state
  },
  Command.command("eg-eg") {
    state =>
      "run-main uima.cpe.CPERunner -from eg -to eg" :: state
  },
  Command.command("eg-w") {
    state =>
      "run-main uima.cpe.CPERunner -from eg -to w" :: state
  },
  Command.command("eg-e") {
    state =>
      "run-main uima.cpe.CPERunner -from eg -to e" :: state
  },
  Command.command("w-w") {
    state =>
      "run-main uima.cpe.CPERunner -from w -to w" :: state
  },
  Command.command("w-e") {
    state =>
      "run-main uima.cpe.CPERunner -from w -to e" :: state
  },
  Command.command("e-e") {
    state =>
      "run-main uima.cpe.CPERunner -from e -to e" :: state
  }
)