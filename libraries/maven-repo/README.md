# FelisCatus Zero-Libraries
Scala 2.12.1+

The current version of this library is "0.0.1"
## SBT
SBT 0.13.13+
### Repository
Add the repository of the releases or the snapshots in your build.sbt
#### Releases
```scala
resolvers += "FelisCatusZeroLibraries-releases-repository" at "https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries/maven-repo/releases"
```
#### Snapshots
```scala
resolvers += "FelisCatusZeroLibraries-snapshots-repository" at "https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries/maven-repo/snapshots"
```
### Dependency
Add the dependency in your build.sbt
```scala
dependences += "us.feliscat" % "feliscatuszerolibraries_2.12" % "{version}"
```
## Gradle
Currently unsupported but probably available
### Repository
Add the repository of the releases or the snapshots in your build.gradle
#### Releases
```javascript
repositories {
    maven {
        url "https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries/maven-repo/releases"
    }
}
```

#### Snapshots
```javascript
repositories {
    maven {
        url "https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries/maven-repo/snapshots"
    }
}
```

### Dependency
Add the dependency in your build.gradle
```javascript
dependencies {
    compile group: 'us.feliscat', name: 'feliscatuszerolibraries_2.12', version: '{version}'
}
```

## Maven
Currently unsupported but probably available
### Repository
Add the repository of the releases or the snapshots in your pom.xml
#### Releases
```xml
<repository>
  <id>FelisCatusZeroLibraries-releases-repository</id>
  <url>https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries/maven-repo/releases</url>
</repository>
```
#### Snapshots
```xml
<repository>
  <id>FelisCatusZeroLibraries-snapshots-repository</id>
  <url>https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries/maven-repo/snapshots</url>
</repository>
```

### Dependency
Add the dependency in your pom.xml
```xml
<dependency>
  <groupId>us.feliscat</groupId>
  <artifactId>feliscatuszerolibraries_2.12</artifactId>
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
            <ibiblio name="FelisCatusZeroLibraries-releases-repository" m2compatible="true" root="https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries/maven-repo/releases
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
            <ibiblio name="FelisCatusZeroLibraries-snapshots-repository" m2compatible="true" root="https://github.com/ktr-skmt/FelisCatusZero-multilingual/raw/master/libraries/maven-repo/snapshots
"/>
        </chain>
    </resolvers>
</ivysettings>
```
### Dependency
Add the dependency in your ivy.xml
```xml
<dependency org="us.feliscat" name="feliscatuszerolibraries_2.12" rev="{version}"/>
```
