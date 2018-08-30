#!/bin/bash

source ./setEnv.sh
SHELLNAME=`basename $0`

printLog "$SHELLNAME" "stop md5verify ..."
./stop_md5verify.sh
printLog "$SHELLNAME" "stop md5verify success! start the md5verify ..."
nohup ./start_md5verify.sh &
printLog "$SHELLNAME" "start md5verify completed, please use 'tail -f nohup.out' to check start result."
