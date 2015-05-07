#!/bin/bash

if [ "$#" -ne 1 ]; then
echo "Illegal number of parameters. Usage: pop_server.sh <number_of_clients>"
exit 0
fi

# grep pom.xml to get version
cd "/Users/y.stadnicki/Dev/perso/com-yann-distributedenv"
version="$(cat pom.xml | grep "^   <version>.*</version>" | awk -F'[><]' '{print $3}')"

# run server
java -cp target/com-yann-distributedenv-$version-jar-with-dependencies.jar com.yann.distributedenv.register.Server $1
