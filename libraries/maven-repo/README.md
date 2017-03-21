# FelisCatus Zero-libraries
Scala 2.12.1+
## SBT
SBT 0.13.13+
### Repository
Add the releases or the snapshots in your build.sbt
#### Releases
```scala
resolvers += "FelisCatusZeroLibraries-releases-repository" at ""
```
#### Snapshots
```scala
resolvers += "FelisCatusZeroLibraries-snapshots-repository" at ""
```
### Dependency
Add the dependency in your build.sbt
```scala
dependences += "" % "" % ""
```
## Gradle
Currently unsupported but probably available
### Repository
Add the releases or the snapshots in your build.gradle
#### Releases
```json
repositories {
    maven {
        url ""
    }
}
```

#### Snapshots
```json
repositories {
    maven {
        url ""
    }
}
```

### Dependency
Add the dependency in your build.gradle
```json
dependencies {
    compile group: '', name: '', version: ''
}
```

## Maven
Currently unsupported but probably available
### Repository
Add the releases or the snapshots in your pom.xml
#### Releases
```xml
<repository>
  <id>FelisCatusZeroLibraries-releases-repository</id>
  <url></url>
</repository>
```
#### Snapshots
```xml
<repository>
  <id>FelisCatusZeroLibraries-snapshots-repository</id>
  <url></url>
</repository>
```

### Dependency
Add the dependency in your pom.xml
```xml
<dependency>
  <groupId></groupId>
  <artifactId></artifactId>
  <version></version>
</dependency>
```