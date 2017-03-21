#!/bin/sh
cp -r tutorial/out_example .
mv out_example out
sbt "tag"
