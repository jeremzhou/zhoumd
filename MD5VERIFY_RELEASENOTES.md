<!-- TOC -->

- [v1.0.0_20171201_001](#v10020171201001)
    - [v1.0.0_20171201_001下载](#v10020171201001%E4%B8%8B%E8%BD%BD)
    - [v1.0.0_20171201_001信息](#v10020171201001%E4%BF%A1%E6%81%AF)
    - [References](#references)
    - [概述](#%E6%A6%82%E8%BF%B0)
    - [主要更新](#%E4%B8%BB%E8%A6%81%E6%9B%B4%E6%96%B0)
        - [新功能](#%E6%96%B0%E5%8A%9F%E8%83%BD)
        - [改变](#%E6%94%B9%E5%8F%98)
    - [MR](#mr)
        - [修复的MR](#%E4%BF%AE%E5%A4%8D%E7%9A%84mr)
        - [未修复的MR](#%E6%9C%AA%E4%BF%AE%E5%A4%8D%E7%9A%84mr)
    - [Source Code](#source-code)
        - [Code结构](#code%E7%BB%93%E6%9E%84)
        - [编译环境](#%E7%BC%96%E8%AF%91%E7%8E%AF%E5%A2%83)
    - [安装](#%E5%AE%89%E8%A3%85)
        - [安装JDK](#%E5%AE%89%E8%A3%85jdk)
        - [安装md5verify](#%E5%AE%89%E8%A3%85md5verify)
        - [配置md5verify](#%E9%85%8D%E7%BD%AEmd5verify)
            - [md5verify.yml](#md5verifyyml)
            - [logback.xml](#logbackxml)
        - [启动](#%E5%90%AF%E5%8A%A8)
    - [PROTOCOL CHART](#protocol-chart)

<!-- /TOC -->

# v1.0.0_20171201_001
---
## v1.0.0_20171201_001下载

| filename | sha256 hash |
| -------- | ----------- |
|[md5verify.LINUX64.MES_1.0.0_20171201_001.tar.gz](http://172.19.64.100:9999/release/md5verify/packages/md5verify.LINUX64.MES_1.0.0_20171201_001.tar.gz)|d1cd99ccc9bee3d8cf337c15d60534a9f3b1f227415f301ea90e4cd13554e897|
|[md5verify.LINUX64.MES_1.0.0_20171201_001_Internal_ReleaseNotes.html](http://172.19.64.100:9999/release/doc/releaseNotes/md5verify.LINUX64.MES_1.0.0_20171201_001_Internal_ReleaseNotes.html)||
|[jdk-8u74-linux-x64.tar.gz](http://172.19.64.100:9999/release/common/JDK/jdk-8u74-linux-x64.tar.gz)|0bfd5d79f776d448efc64cb47075a52618ef76aabb31fde21c5c1018683cdddd|

---

## v1.0.0_20171201_001信息

>* Author:[Tommy Zhao](tommy.zhao@utstarcom.cn)
>* Release Date：2017年12月01日
>* Reviewed By: [Tommy Zhao](tommy.zhao@utstarcom.cn), [King Ye](king.ye@utstarcom.cn)
>* Description Of Change： First release

---

## References

>* MRD & SRS for 央视OMS三期扩容（运营平台扩容项目）phase1需求_V1.0(20161130) .doc
>* HLD for 央视三期phase1_1201.doc
>* HLD for VMSMD5校验模块  v0.1.doc

---

## 概述

现在接收视频，并没有进行检验，无锡的生产系统有MD5的检验接口。
1、收到无锡生产基地生产的节目之后，视频状态处于正在分析（末激活）状态
2、根据节目对应的视频，通过校验接口校验视频MD5值，如果一样，将视频状态置为已激活，否则还是正在分析状态，并向校验接口请求重发视频
3、每过10分钟再去校验无锡生产基地传过来且视频处于（末激活）状态的节目的视频接口规范

```sequence
title: MD5校验流程图
participant 无锡生产系统 as A
participant VMSAdapter as B
participant Oracle DB AS C
participant md5verify as D
participant Filesystem as E
A->B: MI消息
B->C: 保存节目、Movie，\nMovie未激活
D->C: 每十分钟\n获取待校验的节目、\nMovie
D->A: 根据Movie的fileurl请求MD5校验
A-->D: 返回MD5值
D->E: 根据Movie的fileurl\n获取本地文件MD5值
E-->D: 返回MD5值
D->D: 无锡MD和本地MD5一致
D->C: 更新Movie状态激活
D-->D: 无锡MD和本地MD5不一致
D->C: 更新Movie状态校验失败
D->A: 根据Movie的fileurl重新请求MD5校验
A-->D: 返回重传结果和MD5值
D-->D: ...
D->D: 每十分钟\n根据Movie的校验状态\n计算节目状态
D->C: 根据条件更新节目状态
D->D: 清理缓存的Movie和节目状态
D->C: 获取待校验的节目、\nMovie
D->A: ...
```

---

## 主要更新

### 新功能

>* MD5校验功能从VMSAdapter中独立出来，作为一个新的模块重写。
>* 实现原MD5校验功能。

### 改变

>* 首次出包，不涉及。

---

## MR

### 修复的MR
| MR Num | 描述| 发现的版本 | 修复的版本 |
-------- | ----------- |------ | ------- |
|None

### 未修复的MR
| MR Num | 描述 | 发现的版本 |
-------- | ----------- |------ |
|None

---

## Source Code

### Code结构
```
md5verify                       主目录
│  build.bat                    windows编译脚本
│  build.sh                     linux编译脚本
│  MD5VERIFY_RELEASENOTES.md    releasenotes
│  pom.xml                      maven配置文件
├─src                           source目录
│  ├─dev                        source开发目录，开发时使用的一些代码，不会被打包。
│  ├─main                       source主目录
│  │  ├─assembly                maven 插件目录
│  │  ├─bin                     启动、停止等脚本目录
│  │  ├─config                  配置文件目录
│  │  ├─db2pojo                 db表转换为hibernate entity类配置文件
│  │  ├─java                    java源代码目录
│  │  └─resources               spring配置文件目录
│  └─test                       junit测试目录
```
---
### 编译环境

* 编译环境要求：
    >* Windows 7或Centos6.8-x86_64
    >* JDK-8u74及以上
    >* Apache Maven 3.3.9及以上
    >* Oracle 10g以上
    >
    > 备注：因为编译前需要运行junit单元测试，要连VMSAdapter的DB，因此需要运行Oracle DB.

* 安装ojdbc8.jar

    &emsp;因为版权的原因，[maven repository](http://mvnrepository.com/)不提供ojdbc8.jar的下载，需要手工到oracle官网[jdbc](http://www.oracle.com/technetwork/database/features/jdbc/index-091264.html)下载[ojdbc8.jar](http://download.oracle.com/otn/utilities_drivers/jdbc/122010/ojdbc8.jar)，然后手工安装到本地maven repository。

```bash
#下载ojdbc8.jar
wget http://download.oracle.com/otn/utilities_drivers/jdbc/122010/ojdbc8.jar
#安装
mvn install:install-file "-DgroupId=com.oracle" "-DartifactId=ojdbc8" "-Dversion=12.2.0.1" "-Dpackaging=jar" "-Dfile=C:\temp\ojdbc8.jar"
#deploy to nexus3
mvn deploy:deploy-file "-DgroupId=com.oracle" "-DartifactId=ojdbc8" "-Dversion=12.2.0.1" "-Dpackaging=jar" "-Dfile=C:\temp\ojdbc8.jar" "-Durl=http://10.50.13.130:9527/repository/third-party/" "-DrepositoryId=third-party"
```

* 编译
```bash
#windows
cd md5verify.git\md5verify
build.bat
#linux
cd md5verify.git/md5verify
chmod u+x build.sh
./build.sh
```

* 安装包

    &emsp;编译完成后，安装包位于md5verify.git/md5verify/target目录。

---

## 安装

### 安装JDK

```bash
#copy jdk-8u74-linux-x64.tar.gz to the server, unpack it
tar xf jdk-8u74-linux-x64.tar.gz -C /usr/local/
```

### 安装md5verify

```bash
#copy tar xf md5verify.LINUX64.MES_1.0.0_20171201_001.tar.gz to the server, unpack it
mkdir -p /opt/wacos/server/
tar xf md5verify.LINUX64.MES_1.0.0_20171201_001.tar.gz -C /opt/wacos/server/
```

### 配置md5verify

&emsp;&emsp;md5verify主要有两个配置文件：
>* md5verify.yml    md5verify的基本配置
>* logback.xml      日志配置文件

#### md5verify.yml

&emsp;&emsp;配置md5verify.yml主要是两个部分。
>* oracle db连接
>   * spring.datasource.url         数据库连接串
>   * spring.datasource.username    数据库用户名
>   * spring.datasource.password    数据库用户密码
>* 无锡MD5校验接口url
>   * vmsadapter.md5verify.request.url  MD5校验url

```yaml
# vi md5verify.yml

#spring boot properties
#http://docs.spring.io/spring-boot/docs/1.5.8.RELEASE/reference/htmlsingle/#common-application-properties

# EMBEDDED SERVER CONFIGURATION (ServerProperties)
server:
  # Network address to which the server should bind to.
  address: 127.0.0.1
  # Server HTTP port.
  port: 9797

# SPRING BOOT CONFIG CONFIGURATION
spring:
  datasource:
    url: jdbc:oracle:thin:@10.50.13.101:1521:orcl
    username: wacos
    password: oss

# md5verify配置
vmsadapter:
  md5verify:
    #0：无需审核， 1：编目未完成，2：根据VSP
    auditflag: 1
    task:    
      #MD5验证时间间隔。单位：分钟
      interval: 10
      #定时检查频率
      cronexpression: 0 0/5 * * * ?
      #MD5 program 校验次数
      count: 3
    request:
      #MD5校验url
      url: http://127.0.0.1:9797/md5verify
      #等待服务器返回结果超时时间。单位：秒
      timeout: 180
      #并发请求数
      threads: 8
```

#### logback.xml

&emsp;&emsp;日志配置文件logback.xml一般不用修改。

```xml
<!-- logback.xml -->
<?xml version="1.0" encoding="UTF-8"?>

<configuration scan="true" scanPeriod="600 seconds" debug="false">

	<property name="log_dir" value="logs" />
	<property name="maxHistory" value="720" /><!-- unit: hour -->

	<!-- LEVEL: ERROR -->
	<appender name="logError"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<file>${log_dir}/md5verify_error.log</file>
		<filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>ERROR</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<fileNamePattern>${log_dir}/md5verify_error.log.%d{yyyy-MM-dd-HH}.%i
			</fileNamePattern>
			<maxHistory>${maxHistory}</maxHistory>
			<cleanHistoryOnStart>true</cleanHistoryOnStart>
			<timeBasedFileNamingAndTriggeringPolicy
				class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>10MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
		</rollingPolicy>
		<encoder>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p [%t] [%c] - %m%n</pattern>
		</encoder>
	</appender>

	<!-- LEVEL: WARN -->
	<appender name="logWarn"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<file>${log_dir}/md5verify_warn.log</file>
		<filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>WARN</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<fileNamePattern>${log_dir}/md5verify_warn.log.%d{yyyy-MM-dd-HH}.%i
			</fileNamePattern>
			<maxHistory>${maxHistory}</maxHistory>
			<cleanHistoryOnStart>true</cleanHistoryOnStart>
			<timeBasedFileNamingAndTriggeringPolicy
				class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>10MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
		</rollingPolicy>
		<encoder>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p [%t] [%c] - %m%n</pattern>
		</encoder>
	</appender>

	<!-- LEVEL: INFO -->
	<appender name="logInfo"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<file>${log_dir}/md5verify.log</file>
		<filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>INFO</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<fileNamePattern>${log_dir}/md5verify.log.%d{yyyy-MM-dd-HH}.%i
			</fileNamePattern>
			<maxHistory>${maxHistory}</maxHistory>
			<cleanHistoryOnStart>true</cleanHistoryOnStart>
			<timeBasedFileNamingAndTriggeringPolicy
				class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>10MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
		</rollingPolicy>
		<encoder>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] - %m%n</pattern>
		</encoder>
	</appender>

	<!-- LEVEL: DEBUG -->
	<appender name="logDebug"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<file>${log_dir}/md5verify_debug.log</file>
		<filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>DEBUG</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
		<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<fileNamePattern>${log_dir}/md5verify_debug.log.%d{yyyy-MM-dd-HH}.%i
			</fileNamePattern>
			<maxHistory>${maxHistory}</maxHistory>
			<cleanHistoryOnStart>true</cleanHistoryOnStart>
			<timeBasedFileNamingAndTriggeringPolicy
				class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>10MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
		</rollingPolicy>
		<encoder>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p [%t] [%c] - %m%n</pattern>
		</encoder>
	</appender>

	<!-- package: org.eclipse.jetty -->
	<logger name="org.eclipse.jetty" level="WARN" />

	<!-- package: cn.utstarcom.vmsadapter.md5verify -->
	<logger name="cn.utstarcom.vmsadapter.md5verify" level="INFO" />

	<!-- package: org.springframework -->
	<logger name="org.springframework" level="INFO" />
	
	<!-- package: org.jboss -->
    <logger name="org.jboss" level="INFO" />
    
    <!-- package: org.hibernate -->
    <logger name="org.hibernate" level="INFO" />
    
    <!-- package: org.eclipse -->
    <logger name="org.eclipse" level="INFO" />

	<!-- root -->
	<root level="DEBUG">
		<appender-ref ref="logError" />
		<appender-ref ref="logWarn" />
		<appender-ref ref="logInfo" />
		<appender-ref ref="logDebug" />
	</root>

</configuration> 
```

### 启动

```bash
#启动
cd /opt/wacos/server/md5verify/bin
nohup ./start_md5verify.sh &

#检查
# tail -f nohup.out 

20171201-165101 - [start_md5verify.sh]: start to run md5verify.
20171201-165101 - [start_md5verify.sh]: run md5verify_monitor.py script.
20171201-165101 - [start_md5verify.sh]: run md5verify main program.
20171201-165101 - [start_md5verify.sh]: md5verify used memory size: 1024M
2017-12-01 16:51:01,946 root     INFO  start to run md5verify_monitor.
2017-12-01 16:51:01,946 root     INFO  first, sleep 300 seconds...
16:51:02.222 [main] INFO cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication - the md5verify begin to start ....

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.8.RELEASE)

#启动成功
2017-12-01 16:51:19,755 - [Md5verifyApplication]: the md5verify start completed. the user dir: /opt/wacos/server/md5verify
```

---

## PROTOCOL CHART
| Tcp/Udp | Protocol Type | Port# TO: | Description
| -------- | ----------- | ------ | ------- |
| TCP | HTTP | 127.0.0.1:9797 | 心跳接口使用 |