#!/bin/bash

source ./setEnv.sh
SHELLNAME=`basename $0`

printLog "$SHELLNAME" "start to stop md5verify."
if [ `ps -ef | grep "md5verify" | grep "cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication" | grep -v grep | wc -l` -gt "0" ] || [ `ps -ef | grep "md5verify_monitor.py" | grep -v grep | wc -l` -gt "0" ]; then
    #dump md5verify threads
    cTime=`date +%Y%m%d%H%M%S`
    jccPid=`ps -ef | grep "md5verify" | grep "cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication" | grep -v grep | awk '{ print $2 }'`
    printLog "$SHELLNAME" "dump md5verify threads. md5verify pid: "$jccPid""
    if [ ! -z "$jccPid" ]; then
        $JAVA_HOME/bin/jstack -l "$jccPid" > ./jstack_"$jccPid"_"$cTime".txt 2>&1
        pstack "$jccPid" > ./pstack_"$jccPid"_"$cTime".txt 2>&1
    fi
    #delete expired file
    expiredDays=30
    printLog "$SHELLNAME" "delete expired md5verify dumped threads file. expired days: "$expiredDays""
    find . -type f -name "jstack_*" -mtime +"$expiredDays" | xargs rm -f
    find . -type f -name "pstack_*" -mtime +"$expiredDays" | xargs rm -f
    
    #stop md5verify
    ccPid=`ps -ef | grep "md5verify" | grep "cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication" | grep -v grep | awk '{ print $2,$3 }'`
    ccPid=$ccPid" "`ps -ef | grep "md5verify_monitor.py" | grep -v grep | awk '{ print $2 }' 2>/dev/null`
    if [ ! -z "$ccPid" ]; then
        printLog "$SHELLNAME" "the md5verify pid is "$ccPid""
        printLog "$SHELLNAME" "run kill -9 "$ccPid""
        kill -9 $ccPid
        printLog "$SHELLNAME" "sleep 3 secconds...."
        sleep 3
        while [ `ps -ef | grep "md5verify" | grep "cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication" | grep -v grep | wc -l` -gt "0" ] || [ `ps -ef | grep "md5verify_monitor.py" | grep -v grep | wc -l` -gt "0" ]
        do
            printLog "$SHELLNAME" "kill md5verify again!!!"
            printLog "$SHELLNAME" "kill -9 "$ccPid""
            kill -9 $ccPid
            printLog "$SHELLNAME" "sleep 1 secconds...."
            sleep 1
            if  [ `ps -ef | grep "md5verify" | grep "cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication" | grep -v grep | wc -l` -eq "0" ] && [ `ps -ef | grep "md5verify_monitor.py" | grep -v grep | wc -l` -eq "0" ]; then
                break
            fi
        done
        printLog "$SHELLNAME" "the md5verify stop completly." 
    fi
else
    printLog "$SHELLNAME" "the md5verify not run!"
fi
