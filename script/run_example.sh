#!/bin/bash

# grep pom.xml to get version
cd "/Users/y.stadnicki/Dev/perso/com-yann-distributedenv"
version="$(cat pom.xml | grep "^   <version>.*</version>" | awk -F'[><]' '{print $3}')"

# run jar
java -cp target/com-yann-distributedenv-$version-jar-with-dependencies.jar com.yann.distributedenv.example.HelloWorld $1 | tee log.txt

