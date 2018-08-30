#!/usr/bin/python

import urllib2
import os
import sys
import time
import subprocess
import commands
import logging
import json


def parseStatus(content):
    jsonMap = json.loads(content)
    serviceStatus = jsonMap.get('serviceStatus')
    return serviceStatus if serviceStatus != None else -1


def getIpPort(confFile):
    ip = '127.0.0.1'
    port = 9999
    count = 0
    file = open(confFile)
    line = file.readline()
    while line:
        arrs = line.replace("\r\n", "").replace("\n", "").split(":")
        if (arrs[0] == "  address"):
            ip = arrs[1].strip()
            if ip == '0.0.0.0':
                ip = '127.0.0.1'
            count += 1
        if (arrs[0] == "  port"):
            port = arrs[1].strip()
            count += 1
        if count == 2:
            break
        line = file.readline()
    file.close()
    return ip, port


def postHeartBeat(url, headers, data):
    postReq = urllib2.Request(url=url, headers=headers, data=data)
    try:
        response = urllib2.urlopen(postReq, timeout=10)
        status = response.getcode()
        if (status != 200):
            logging.error("postHeartBeat the status: %d" % (status))
            return -1
        else:
            return parseStatus(response.read())
    except Exception, e:
        logging.error("postHeartBeat generate exception: %s" % (e))
        return -1


def restartMd5verify(md5verifyHome):
    os.chdir(md5verifyHome + "/bin")
    logging.info("start to run restartMd5verify. curr dir: %s" % (os.getcwd()))
    subprocess.Popen('nohup ./restart_md5verify.sh &', shell='true')
    logging.info("end to run restartMd5verify. cur dir: %s" % (os.getcwd()))
    sys.exit()


def runTest(postUrl, postHeaders, postCotent):
    exceptCount = 0
    while True:
        if (postHeartBeat(postUrl, postHeaders, postCotent) == 0):
            exceptCount = 0
        else:
            exceptCount += 1;
            logging.info("postHeartBeat exception, exceptCount: %s" % (exceptCount))

        md5verifyCount = commands.getoutput(
            "ps -ef | grep 'cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication' | grep -v grep | wc -l")
        if (exceptCount > 6 or md5verifyCount != "1"):
            logging.info(
                "monitor md5verify run excepted, so restartMd5verify. exceptCount: %s md5verifyCount: %s" % (
                    exceptCount, md5verifyCount))
            restartMd5verify(md5verifyHome)

        time.sleep(10)


# set log format
logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(name)-8s %(levelname)-5s %(message)s')

if __name__ == "__main__":

    logging.info("start to run md5verify_monitor.")
    logging.info("first, sleep 300 seconds...")
    time.sleep(300)
    logging.info("sleep compeleted, continue...")

    md5verifyHome = os.getenv("MD5VERIFY_HOME")
    if (not os.path.exists(md5verifyHome)):
        logging.info(
            "md5verify run dir: don't exists! md5verify_monitor exit!" % (md5verifyHome))
        sys.exit()
    logging.info("md5verify home: %s" % (md5verifyHome))

    confFile = md5verifyHome + "/config/md5verify.yml"
    (ip, port) = getIpPort(confFile)
    postUrl = "http://" + ip + ":" + port + "/NeHeartBeat"
    logging.info("md5verify heartbeat post url: %s" % (postUrl))
    postHeaders = {'Content-Type': 'application/json'}
    postCotent = '{"message": "NeHeartBeat"}'
    runTest(postUrl, postHeaders, postCotent)
