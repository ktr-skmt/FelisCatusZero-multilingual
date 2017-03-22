# FelisCatus Zero-Libraries4UIMA
Scala 2.12.1+

The current version of this library is "0.0.1"
## SBT
SBT 0.13.13+
### Repository
Add the repository of the releases or the snapshots in your build.sbt
#### Releases
```scala
resolvers += "FelisCatusZeroLibraries4UIMA-releases-repository" at "https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries4uima/maven-repo/releases"
```
#### Snapshots
```scala
resolvers += "FelisCatusZeroLibraries4UIMA-snapshots-repository" at "https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries4uima/maven-repo/snapshots"
```
### Dependency
Add the dependency in your build.sbt
```scala
dependences += "us.feliscat" % "feliscatuszerolibraries4uima_2.12" % "{version}"
```
## Gradle
Currently unsupported but probably available
### Repository
Add the repository of the releases or the snapshots in your build.gradle
#### Releases
```json
repositories {
    maven {
        url "https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries4uima/maven-repo/releases"
    }
}
```

#### Snapshots
```json
repositories {
    maven {
        url "https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries4uima/maven-repo/snapshots"
    }
}
```

### Dependency
Add the dependency in your build.gradle
```json
dependencies {
    compile group: 'us.feliscat', name: 'feliscatuszerolibraries4uima_2.12', version: '{version}'
}
```

## Maven
Currently unsupported but probably available
### Repository
Add the repository of the releases or the snapshots in your pom.xml
#### Releases
```xml
<repository>
  <id>FelisCatusZeroLibraries4UIMA-releases-repository</id>
  <url>https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries4uima/maven-repo/releases</url>
</repository>
```
#### Snapshots
```xml
<repository>
  <id>FelisCatusZeroLibraries4UIMA-snapshots-repository</id>
  <url>https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries4uima/maven-repo/snapshots</url>
</repository>
```

### Dependency
Add the dependency in your pom.xml
```xml
<dependency>
  <groupId>us.feliscat</groupId>
  <artifactId>feliscatuszerolibraries4uima_2.12</artifactId>
  <version>{version}</version>
</dependency>
```

## Ivy
Currently unsupported but probably available
### Repository
Add the repository of the releases or the snapshots in your ivysettings.xml
#### Releases
```xml
<ivysettings>
    <settings defaultResolver="chain"/>
    <resolvers>
        <chain name="chain">
            <ibiblio name="FelisCatusZeroLibraries4UIMA-releases-repository" m2compatible="true" root="https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries4uima/maven-repo/releases
"/>
        </chain>
    </resolvers>
</ivysettings>
```
#### Snapshots
```xml
<ivysettings>
    <settings defaultResolver="chain"/>
    <resolvers>
        <chain name="chain">
            <ibiblio name="FelisCatusZeroLibraries4UIMA-snapshots-repository" m2compatible="true" root="https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries4uima/maven-repo/snapshots
"/>
        </chain>
    </resolvers>
</ivysettings>
```
### Dependency
Add the dependency in your ivy.xml
```xml
<dependency org="us.feliscat" name="feliscatuszerolibraries4uima_2.12" rev="{version}"/>
```
