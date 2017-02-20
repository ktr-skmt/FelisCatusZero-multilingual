# FelisCatus Zero-multilingual 
## Abstract
This system answers world history essay questions in Japanese and/or English and evaluate the answers.  
Also, this is Japanese-English bilingual version of <a href="https://github.com/ktr-skmt/FelisCatusZero">FelisCatus Zero</a>.

## Trial

1. Install Scala and sbt.
1. Git clone or download this repository.
1. Run <a href="https://github.com/ktr-skmt/FelisCatusZero-multilingual/blob/master/trial.sh">trial.sh</a>
```bash
$ bash trial.sh
```
## Quick Start Guide
To install datasets, do the following 1. and/or 2.

1. Put QA Lab-3 essay subtask datasets into <a href="https://github.com/ktr-skmt/FelisCatusZero-multilingual/tree/master/src/main/resources/qalab_dataset">src/main/resources/qalab_dataset</a>
1. Put QA Corpus into <a href="https://github.com/ktr-skmt/FelisCatusZero-multilingual/tree/master/src/main/resources/qa_corpus">src/main/resources/qa_courpus</a>

Install Indri search engine and Indri indexes of Japanese and English textbooks and glossary. See <a href="https://github.com/ktr-skmt/FelisCatusZero/wiki/Install-Guide">here</a>  

Install MeCab with UTF-8 and UniDic system dictionary and user dictionary with UTF-8. See <a href="https://github.com/ktr-skmt/FelisCatusZero/wiki/Install-Guide">here</a>  

To run this system, see <a href="https://github.com/ktr-skmt/FelisCatusZero-multilingual/blob/master/tutorial/Run.ipynb">Run.ipynb</a>
