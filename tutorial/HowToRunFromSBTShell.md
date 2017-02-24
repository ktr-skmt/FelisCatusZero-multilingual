# How to run through SBT Shell
I recommend to run FelisCatus Zero through SBT Shell because the build through SBT Shell runs faster than the build from SBT Batch Mode.
> Note: Running in batch mode requires JVM spinup and JIT each time, so your build will run much slower. For day-to-day coding, we recommend using the sbt shell or Continuous build and test feature described below.  

quoted from "sbt Reference Manual — Running" (http://www.scala-sbt.org/0.13/docs/Running.html)

So I explain how to run it through SBT Shell.
## Build
### SBT Shell
#### Start SBT Shell
```bash
$ sbt
Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=384m; support was removed in 8.0
[info] Loading project definition from /Users/.../FelisCatusZero/project
[info] Set current project to FelisCatusZero (in build file:/Users/.../FelisCatusZero/)
> 
```
#### Exit SBT Shell
```bash
> exit
$ 
```
#### Reload project after you changed build.sbt, project/build.properties and/or project/plugins.sbt
```bash
> reload
[info] Loading project definition from /Users/.../FelisCatusZero/project
[info] Set current project to FelisCatusZero (in build file:/Users/.../FelisCatusZero/)
> 
```
### System Initialization
#### Initialize
```bash
> init
[info] Set current project to FelisCatusZero-JCasGen (in build file:/Users/.../FelisCatusZero/)
[info] Running util.JCasGen 
...
[info] Set current project to FelisCatusZero (in build file:/Users/.../FelisCatusZero/)
```
#### Do JCasGen after you changes the type system files under src/main/resources/desc/ts/
```bash
> jcasgen
[info] Set current project to FelisCatusZero-JCasGen (in build file:/Users/.../FelisCatusZero/)
[info] Running util.JCasGen 
...
[info] Set current project to FelisCatusZero (in build file:/Users/.../FelisCatusZero/)
```
#### Clear the run history
```bash
> clearHistory
[info] Running util.HistoryCleaner 
...
```
## Run
Once you initialized FelisCatus Zero you can do the following runs.
### End-to-end run
```bash
> e2e
[info] Running uima.cpe.CPERunner 
...
```
### Partial run
#### Run from Information Retriever
```bash
> ir-
[info] Running uima.cpe.CPERunner 
...
```
#### Run from Information Retriever to Essay Generator
```bash
> ir-eg
[info] Running uima.cpe.CPERunner 
...
```
#### Run only Information Retriever
```bash
> ir
[info] Running uima.cpe.CPERunner 
...
```
or
```bash
> ir-ir
[info] Running uima.cpe.CPERunner 
...
```

If you want to do other partial run, see below.

Pipeline component name|The name in SBT Shell
---|---
Question Analyzer|qa
Information Retriever|ir
Essay Generator|eg
Essay Writer|w
Essay Evaluator|e

#### Run though the main method of CPERunner with arguments
The following example is exact same as the above "Run from Information Retriever"
```bash
> run-main uima.cpe.CPERunner -from ir
[info] Running uima.cpe.CPERunner 
...
```
