#!/bin/bash

cd `dirname $0`
TIMER_HOME=`pwd`
cd ..

DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf
STDOUT_FILE=$DEPLOY_DIR/stdout.log
LIB_DIR=$DEPLOY_DIR/lib
MAINJAR=$TIMER_HOME/myApp.jar


MODEL=-Dspring.profiles.active=production

JAVA_OPTS="-Xmn1024m -Xms2048m -Xmx2048m -XX:PermSize=125M -XX:+UseCompressedOops -XX:+AggressiveOpts -Dport=8080 -Dapplication.name="$DEPLOY_DIR
LIB_JARS=$MAINJAR:`ls $LIB_DIR | grep .jar | awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

HADOOP_OPTS="-Dhdp.version=2.3.4.0-3485"
CONF_HADOOP=/usr/hdp/2.3.4.0-3485/hadoop/conf

COMMAND=$1
if  [ "$COMMAND" = "startApp" ];then
	
	for f in $SPARK_HOME/lib/*.jar; do
	  LIB_JARS=${LIB_JARS}:$f;
	done
	 	
    MAINCLASS=com.app.MyApp

else
  MAINCLASS=$COMMAND
fi

params=$@
java -classpath $CONF_HADOOP:$CONF_DIR:$LIB_JARS $JAVA_OPTS $HADOOP_OPTS $MODEL $MAINCLASS $params
