#!/bin/bash

source ./setEnv.sh
SHELLNAME=`basename $0`

printLog "$SHELLNAME" "start to run md5verify."

if [ `ps -ef | grep "md5verify" | grep "cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication" | grep -v grep | wc -l` -eq "1" ]; then
    printLog "$SHELLNAME" "the md5verify has been running. exit!"
    exit 0
fi

cd ..
MD5VERIFY_HOME=`pwd`
export MD5VERIFY_HOME

if [ `ps -ef | grep "md5verify_monitor.py" | grep -v grep | wc -l` -eq "0" ]; then
    printLog "$SHELLNAME" "run md5verify_monitor.py script."
    cd ./bin
    nohup ./md5verify_monitor.py &
    cd ..
fi

CLASSPATH="$MD5VERIFY_HOME"/lib/*

[ -d ${MD5VERIFY_HOME}/logs/gc ] || mkdir -p ${MD5VERIFY_HOME}/logs/gc
if [ $? -ne "0" ]; then
    printLog "$SHELLNAME" "create gc log dir : ${MD5VERIFY_HOME}/logs/gc failed! exit!"
    exit 2
fi

# uapollo opts
UAPOLLO_OPTS="-Duapollo.server-port=${UAPOLLO_SEVER_PORT}"
if [ "${UAPOLLO_CLIENT_IP}" != "" ]; then
    UAPOLLO_OPTS="${UAPOLLO_OPTS} -Duapollo.client.ip=${UAPOLLO_CLIENT_IP}"
fi

printLog "$SHELLNAME" "run md5verify main program."
printLog "$SHELLNAME" "md5verify used memory size: ${JAVAMEM}"
CURRENT_DATE=`date '+%Y%m%d-%H.%M.%S'`
GC_OPTS="-Xms"${JAVAMEM}" -Xmx"${JAVAMEM}" -d64 -server -XX:+AggressiveOpts -XX:MaxDirectMemorySize=128M -XX:+UseG1GC -XX:MaxGCPauseMillis=400 -XX:G1ReservePercent=15 -XX:InitiatingHeapOccupancyPercent=30 -XX:ParallelGCThreads=16 -XX:ConcGCThreads=4 -XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions -XX:G1NewSizePercent=20 -XX:+G1SummarizeConcMark -XX:G1LogLevel=finest -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintAdaptiveSizePolicy -Xloggc:${MD5VERIFY_HOME}/logs/gc/gc-$CURRENT_DATE.txt -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+HeapDumpOnOutOfMemoryError"
#performance stable, startup slowly for -XX:+AlwaysPreTouch
#GC_OPTS="-Xms"${JAVAMEM}" -Xmx"${JAVAMEM}" -d64 -server -XX:+AggressiveOpts -XX:+AlwaysPreTouch -XX:MaxDirectMemorySize=1024M -XX:+UseG1GC -XX:MaxGCPauseMillis=400 -XX:G1ReservePercent=15 -XX:InitiatingHeapOccupancyPercent=30 -XX:ParallelGCThreads=16 -XX:ConcGCThreads=4 -XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions -XX:G1NewSizePercent=20 -XX:+G1SummarizeConcMark -XX:G1LogLevel=finest -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintAdaptiveSizePolicy -Xloggc:${MD5VERIFY_HOME}/logs/gc/gc-$CURRENT_DATE.txt -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+HeapDumpOnOutOfMemoryError"

$JAVA -server $GC_OPTS -classpath "$CLASSPATH" -Djava.net.preferIPv4Stack=true -Dspring.property.path="${MD5VERIFY_HOME}" -Dlogging.config=file:"${MD5VERIFY_HOME}"/config/logback.xml ${UAPOLLO_OPTS} cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication
