#!/bin/bash

JARS_PATH=$TRUNK/etc/javaslicer/jars
echo JARS_PATH = $JARS_PATH
SRC_PATH=$TRUNK/etc/javaslicer/jars

cd $M2_BIN

mvn install:install-file -Dfile=$JARS_PATH/javaslicer-common-1.1.1-SNAPSHOT.jar -Dsources=$SRC_PATH/javaslicer-common-1.1.1-SNAPSHOT-sources.jar -DgroupId=de.unisb.cs.st -DartifactId=javaslicer-common -Dversion=1.1.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=$JARS_PATH/javaslicer-core-1.1.1-SNAPSHOT.jar -Dsources=$SRC_PATH/javaslicer-core-1.1.1-SNAPSHOT-sources.jar -DgroupId=de.unisb.cs.st -DartifactId=javaslicer-core -Dversion=1.1.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=$JARS_PATH/javaslicer-jung-1.1.1-SNAPSHOT.jar -Dsources=$SRC_PATH/javaslicer-jung-1.1.1-SNAPSHOT-sources.jar -DgroupId=de.unisb.cs.st -DartifactId=javaslicer-jung -Dversion=1.1.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=$JARS_PATH/javaslicer-tracer-1.1.1-SNAPSHOT.jar -Dsources=$SRC_PATH/javaslicer-tracer-1.1.1-SNAPSHOT-sources.jar -DgroupId=de.unisb.cs.st -DartifactId=javaslicer-tracer -Dversion=1.1.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=$JARS_PATH/javaslicer-traceReader-1.1.1-SNAPSHOT.jar -Dsources=$SRC_PATH/javaslicer-traceReader-1.1.1-SNAPSHOT-sources.jar -DgroupId=de.unisb.cs.st -DartifactId=javaslicer-traceReader -Dversion=1.1.1-SNAPSHOT -Dpackaging=jar


