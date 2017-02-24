#!/bin/sh
cd ~
mkdir wh
mkdir whe
cd ~/wh
git clone git@bitbucket.org:kotaro_sakamoto/worldhistoryindriindex.git
mv worldhistoryindriindex/index .
mv worldhistoryindriindex/indexW .
mv worldhistoryindriindex/res .
mv worldhistoryindriindex/seg .
mv worldhistoryindriindex/segW .
rm -rf worldhistoryindriindex/
rm -rf .git/
cd ~/whe
git clone git@bitbucket.org:kotaro_sakamoto/worldhistoryindriindex-englishversion.git
mv worldhistoryindriindex-englishversion/index .
mv worldhistoryindriindex-englishversion/indexW .
mv worldhistoryindriindex-englishversion/res .
mv worldhistoryindriindex-englishversion/seg .
mv worldhistoryindriindex-englishversion/segW .
rm -rf worldhistoryindriindex-englishversion/
rm -rf .git/