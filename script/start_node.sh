#!/bin/bash

if [ "$#" -ne 2 ]; then
echo "Illegal number of parameters"
echo "Usage: start_node.sh <node_id> <node_value>"
exit 0
fi

# grep pom.xml to get version
cd "/Users/y.stadnicki/Dev/perso/com-yann-distributedenv"
version="$(cat pom.xml | grep "^   <version>.*</version>" | awk -F'[><]' '{print $3}')"

# run client
java -cp target/com-yann-distributedenv-$version-jar-with-dependencies.jar com.yann.distributedenv.example.StartNode $1 $2
