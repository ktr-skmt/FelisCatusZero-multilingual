# FelisCatus Zero-multilingual 
[![Build Status](https://travis-ci.org/ktr-skmt/FelisCatusZero-multilingual.svg?branch=master)](https://travis-ci.org/ktr-skmt/FelisCatusZero-multilingual)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/FelisCatusZero-multilingual/Lobby?utm_source=share-link&utm_medium=link&utm_campaign=share-link)
## Abstract
This system answers world history essay questions in Japanese and/or English and evaluate the answers.  
Also, this is the Japanese-English bilingual version of <a href="https://github.com/ktr-skmt/FelisCatusZero">FelisCatus Zero</a>.

## Trial

1. Install Scala and Simple Build Tool (SBT).
1. Git-clone or download this repository.
1. Run <a href="https://github.com/ktr-skmt/FelisCatusZero-multilingual/blob/master/trial.sh">trial.sh</a>
```bash
$ bash trial.sh
```

## Quick Start Guide
### (1) Software Installation for FelisCatus Zero Running
Install the following softwares before running (non-trial) FelisCatus Zero
* Scala
* Simple Build Tool (SBT)
* Indri search engine. See <a href="https://github.com/ktr-skmt/FelisCatusZero/wiki/Install-Guide">here</a>  
* Japanese language analyzer (MeCab). See <a href="https://github.com/ktr-skmt/FelisCatusZero/wiki/Install-Guide">here</a>  

Note: I am planning to make remote services to enable to run without local Indri and MeCab.

### (2) Knowledge Source Installation
See [Knowledge Source Installation](tutorial/KnowledgeSourceInstallation.md)

Note: Knowledge source accessible people are currently limited because of a license issue. I am planning to make remote services to enable to run without local knowledge sources and avoid the license issue.

### (3) Download FelisCatus Zero
Git-clone or download this repository

Git-clone via HTTPS
```bash
cd (directory for downloading this repository)
git clone https://github.com/ktr-skmt/FelisCatusZero-multilingual.git
```
Git-clone via SSH
```bash
cd (directory for downloading this repository)
git clone git@github.com:ktr-skmt/FelisCatusZero-multilingual.git
```
Download
```bash
cd (directory for downloading this repository)
wget https://github.com/ktr-skmt/FelisCatusZero-multilingual/archive/master.zip
```

### (4) Question-and-gold-standard Pair Installation
Sample questions have already been installed in this repository.

If you want to Install other pairs of question and gold standard, do the following 1. and/or 2.

1. Put QA Lab-3 essay subtask datasets into <a href="https://github.com/ktr-skmt/FelisCatusZero-multilingual/tree/master/src/main/resources/qalab_dataset">src/main/resources/qalab_dataset</a>
1. Put QA Corpus into <a href="https://github.com/ktr-skmt/FelisCatusZero-multilingual/tree/master/src/main/resources/qa_corpus">src/main/resources/qa_courpus</a>

Note: I am planning to make remote services to enable to download training data and test data without installing them locally.

### (5) How to Run
#### Run through SBT Shell (Recommended)
See [How to Run through SBT Shell](tutorial/HowToRunThroughSBTShell.md)

#### Run through SBT Batch Mode
See <a href="https://github.com/ktr-skmt/FelisCatusZero-multilingual/blob/master/tutorial/Run.ipynb">Run.ipynb</a>

## Libraries
FelisCatusZero-multilingual has three libraries as sub-projects:
* [FelisCatus Zero-Libraries](libraries/maven-repo)
* [FelisCatus Zero-Libraries4UIMA](libraries4uima/maven-repo)
* [FelisCatus Zero-Libraries4JCas](libraries4jcas/maven-repo)

FelisCatusZero-multilingual is dependent on FelisCatus Zero-Libraries4JCas, FelisCatus Zero-Libraries4UIMA and JCas generated from [FelisCatusZero's type system](src/main/resources/desc/ts) by [JCasGen](jcasgen).
FelisCatus Zero-Libraries4JCas is dependent on FelisCatus Zero-Libraries and FelisCatus Zero-Libraries4UIMA.
The three libraries' dependencies are shown in the following table.

LIBRARY | DEPENDENT on UIMA | DEPENDENT on JCas generated from FelisCatusZero's type system
---|---|---
FelisCatus Zero-Libraries | NO | NO
FelisCatus Zero-Libraries4UIMA | YES | NO
FelisCatus Zero-Libraries4JCas | YES | YES

See [build.sbt](build.sbt) if you want to look in more detail.

