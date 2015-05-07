#!/bin/bash

echo "Installing the package into the local repository"
cd "/Users/y.stadnicki/dev/perso/com-yann-distributedenv"
mvn clean install -DskipTests=true -Dmaven.javadoc.skip=true

