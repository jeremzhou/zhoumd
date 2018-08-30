/**
 * created on 2018年1月9日 下午4:01:56
 */
package cn.utstarcom.vmsadapter.md5verify.common;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author UTSC0167
 * @date 2018年1月9日
 *
 */
@RefreshScope
@Component("md5verifyConfig")
public class Md5verifyConfig {

    private static final Logger logger = LoggerFactory.getLogger(Md5verifyConfig.class);

    @Value("${vmsadapter.md5verify.isTest:false}")
    private boolean isTest;

    @Value("${vmsadapter.md5verify.auditflag}")
    private Character auditFlag;

    @Value("${vmsadapter.md5verify.task.count}")
    private int md5ChkCntThreshold;

    @Value("${vmsadapter.md5verify.task.interval}")
    private int md5VerifyInterval;

    @Value("${vmsadapter.md5verify.request.timeout}")
    private int requestTimeout;

    @Value("${vmsadapter.md5verify.request.url}")
    private String requestUrl;

    @Value("${vmsadapter.md5verify.request.threads}")
    private int threadNums;

    @PostConstruct
    public void init() {

        logger.info(
                "init Md5verifyConfigBean isTest: {} auditFlag: {} md5ChkCntThreshold: {} md5VerifyInterval: {} requestTimeout: {} requestUrl: {} threadNums: {}",
                this.isTest, this.auditFlag, this.md5ChkCntThreshold, this.md5VerifyInterval,
                this.requestTimeout, this.requestUrl, this.threadNums);

    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean isTest) {
        this.isTest = isTest;
    }

    public Character getAuditFlag() {
        return auditFlag;
    }

    public void setAuditFlag(Character auditFlag) {
        this.auditFlag = auditFlag;
    }

    public int getMd5ChkCntThreshold() {
        return md5ChkCntThreshold;
    }

    public void setMd5ChkCntThreshold(int md5ChkCntThreshold) {
        this.md5ChkCntThreshold = md5ChkCntThreshold;
    }

    public int getMd5VerifyInterval() {
        return md5VerifyInterval;
    }

    public void setMd5VerifyInterval(int md5VerifyInterval) {
        this.md5VerifyInterval = md5VerifyInterval;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public int getThreadNums() {
        return threadNums;
    }

    public void setThreadNums(int threadNums) {
        this.threadNums = threadNums;
    }

    @Override
    public String toString() {
        return "Md5verifyConfigBean [isTest=" + isTest + ", auditFlag=" + auditFlag
                + ", md5ChkCntThreshold=" + md5ChkCntThreshold + ", md5VerifyInterval="
                + md5VerifyInterval + ", requestTimeout=" + requestTimeout + ", requestUrl="
                + requestUrl + ", threadNums=" + threadNums + "]";
    }
}
