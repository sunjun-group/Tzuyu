#!/bin/bash

WALA_JARS_PATH=$TRUNK/etc/wala
echo WALA_JARS_PATH = $WALA_JARS_PATH

cd $M2_BIN

mvn install:install-file -Dfile=$WALA_JARS_PATH/com.ibm.wala.core-1.3.4-SNAPSHOT.jar -Dsources=$WALA_JARS_PATH/com.ibm.wala.core-1.3.4-SNAPSHOT-src.zip -DgroupId=com.ibm.wala -DartifactId=com.ibm.wala.core -Dversion=1.3.4-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=$WALA_JARS_PATH/com.ibm.wala.shrike-1.3.4-SNAPSHOT.jar -Dsources=$WALA_JARS_PATH/com.ibm.wala.shrike-1.3.4-SNAPSHOT-src.zip -DgroupId=com.ibm.wala -DartifactId=com.ibm.wala.shrike -Dversion=1.3.4-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=$WALA_JARS_PATH/com.ibm.wala.util-1.3.4-SNAPSHOT.jar -Dsources=$WALA_JARS_PATH/com.ibm.wala.util-1.3.4-SNAPSHOT-src.zip -DgroupId=com.ibm.wala -DartifactId=com.ibm.wala.util -Dversion=1.3.4-SNAPSHOT -Dpackaging=jar
