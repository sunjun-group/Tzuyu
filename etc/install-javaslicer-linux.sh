#!/bin/bash

JARS_PATH=$TRUNK/etc/javaslicer/assembly
echo JARS_PATH = $JARS_PATH
SRC_PATH=$TRUNK/etc/javaslicer/src

cd $BASE_PATH/apache-maven-3.2.2/bin

mvn install:install-file -Dfile=$JARS_PATH/slicer.jar -Dsources=$SRC_PATH/java-slicer.zip -DgroupId=de.javaslicer -DartifactId=slicer -Dversion=20101004 -Dpackaging=jar

mvn install:install-file -Dfile=$JARS_PATH/tracer.jar -Dsources=$SRC_PATH/javaslicer_src_20101004.tar -DgroupId=de.javaslicer -DartifactId=tracer -Dversion=20101004 -Dpackaging=jar

mvn install:install-file -Dfile=$JARS_PATH/traceReader.jar -Dsources=$SRC_PATH/javaslicer_src_20101004.tar -DgroupId=de.javaslicer -DartifactId=traceReader -Dversion=20101004 -Dpackaging=jar

mvn install:install-file -Dfile=$JARS_PATH/visualize.jar -Dsources=$SRC_PATH/javaslicer_src_20101004.tar -DgroupId=de.javaslicer -DartifactId=visualize -Dversion=20101004 -Dpackaging=jar
