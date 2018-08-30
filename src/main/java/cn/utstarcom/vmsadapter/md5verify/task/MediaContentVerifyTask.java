/**
 * created on 2017年11月28日 下午2:10:35
 */
package cn.utstarcom.vmsadapter.md5verify.task;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.utstarcom.vmsadapter.md5verify.bo.Md5VerifyRequest;
import cn.utstarcom.vmsadapter.md5verify.bo.Md5VerifyResponse;
import cn.utstarcom.vmsadapter.md5verify.bo.MediaContentVerifyStatus;
import cn.utstarcom.vmsadapter.md5verify.common.Md5VerifyRequestType;
import cn.utstarcom.vmsadapter.md5verify.common.Md5VerifyStatusType;
import cn.utstarcom.vmsadapter.md5verify.common.Md5verifyCache;
import cn.utstarcom.vmsadapter.md5verify.common.MediaContentStatusType;
import cn.utstarcom.vmsadapter.md5verify.dto.MediaContentStatus;
import cn.utstarcom.vmsadapter.md5verify.service.HttpClientService;
import cn.utstarcom.vmsadapter.md5verify.service.LocalFileService;
import cn.utstarcom.vmsadapter.md5verify.service.MediaContentService;

/**
 * @author UTSC0167
 * @date 2017年11月28日
 *
 */
@Component
public class MediaContentVerifyTask {

    private static final Logger logger = LoggerFactory.getLogger(MediaContentVerifyTask.class);

    @Value("${vmsadapter.md5verify.request.url}")
    private String requestUrl;

    @Value("${vmsadapter.md5verify.request.threads}")
    private int threadNums;

    @Autowired
    private HttpClientService httpClientService;

    @Autowired
    private LocalFileService localFileService;

    @Autowired
    private MediaContentService mediaContentService;

    @PostConstruct
    public void doVerify() {

        logger.info("doVerify threadNums: {} beging to start MediaContentVerifyThread ...",
                threadNums);
        for (int i = 0; i < threadNums; i++) {
            new Thread(new MediaContentVerifyThread(), "MediaContentVerifyThread-" + i).start();
        }
        logger.info("doVerify start MediaContentVerifyThread completed.");

    }

    private final class MediaContentVerifyThread implements Runnable {

        @Override
        public void run() {

            logger.info("MediaContentVerifyThread begin to run.");
            while (true) {

                MediaContentVerifyStatus mediaContentVerifyStatus = null;
                try {
                    mediaContentVerifyStatus = Md5verifyCache.MEDIACONTENTVERIFYSTATUSQUEUE.take();
                } catch (InterruptedException e) {
                    logger.info("run MEDIACONTENTVERIFYSTATUSQUEUE.take generate exception: {}", e);
                    doSleep();
                    continue;
                }

                if (mediaContentVerifyStatus == null) {
                    logger.info("run get mediaContentVerifyStatus is null.");
                    doSleep();
                    continue;
                }

                MediaContentStatus mediaContentStatus = mediaContentVerifyStatus
                        .getMediaContentStatus();
                final int mediaContentId = mediaContentStatus.getMediaContentid();
                final Md5VerifyStatusType md5VerifyStatus = mediaContentVerifyStatus
                        .getMd5VerifyStatusType();
                logger.info("run for mediaContentId: {} init md5VerifyStatus: {}", mediaContentId,
                        md5VerifyStatus);

                Md5VerifyRequest md5VerifyRequest = new Md5VerifyRequest();
                if (md5VerifyStatus.equals(Md5VerifyStatusType.PENDING_RETRANSMISSION)) {
                    md5VerifyRequest.setRequest(Md5VerifyRequestType.RETRANSMISSION);
                } else {
                    md5VerifyRequest.setRequest(Md5VerifyRequestType.VERIFY);
                }
                mediaContentVerifyStatus.setMd5VerifyStatusType(Md5VerifyStatusType.REQUESTING);
                logger.info(
                        "run for mediaContentId: {} setMd5VerifyStatusType: {} Md5VerifyRequestType: {}",
                        mediaContentId, mediaContentVerifyStatus.getMd5VerifyStatusType(),
                        md5VerifyRequest.getRequest());

                String srcFilreUrl = mediaContentStatus.getSrcFileurl();
                String localFileUrl = mediaContentStatus.getLocalFileUrl();

                Md5VerifyResponse md5VerifyResponse = null;
                md5VerifyRequest.setFile(srcFilreUrl);
                try {
                    md5VerifyResponse = httpClientService.doPost(requestUrl, md5VerifyRequest);
                } catch (ConnectException e) {
                    mediaContentVerifyStatus
                            .setMd5VerifyStatusType(Md5VerifyStatusType.CONNECT_FAILED);
                    logger.info(
                            "run for mediaContentId: {} exec doPost requestUrl: {} srcFilreUrl: {} generate ConnectException, setMd5VerifyStatusType: {}. the exception:",
                            mediaContentId, requestUrl, srcFilreUrl,
                            mediaContentVerifyStatus.getMd5VerifyStatusType(), e);
                    continue;
                } catch (TimeoutException e) {
                    mediaContentVerifyStatus.setMd5VerifyStatusType(Md5VerifyStatusType.TIMEOUT);
                    logger.info(
                            "run for mediaContentId: {} exec doPost requestUrl: {} srcFilreUrl: {} generate TimeoutException, setMd5VerifyStatusType: {}. the exception:",
                            mediaContentId, requestUrl, srcFilreUrl,
                            mediaContentVerifyStatus.getMd5VerifyStatusType(), e);
                    continue;
                } catch (Exception e) {
                    mediaContentVerifyStatus
                            .setMd5VerifyStatusType(Md5VerifyStatusType.RESPONES_NULL);
                    logger.info(
                            "run for mediaContentId: {} exec doPost requestUrl: {} srcFilreUrl: {} generate unkown exception, setMd5VerifyStatusType: {}. the exception:",
                            mediaContentId, requestUrl, srcFilreUrl,
                            mediaContentVerifyStatus.getMd5VerifyStatusType(), e);
                    continue;
                }

                if (md5VerifyResponse == null) {
                    mediaContentVerifyStatus
                            .setMd5VerifyStatusType(Md5VerifyStatusType.RESPONES_NULL);
                    logger.info(
                            "run for mediaContentId: {} post get md5VerifyResponse is null. continue.",
                            mediaContentId);
                    continue;
                }

                String responseMd5 = md5VerifyResponse.getMd5();
                logger.info("run for mediaContentId: {} get responseMd5: {}", mediaContentId,
                        responseMd5);
                if (responseMd5 != null) {

                    responseMd5 = responseMd5.replaceAll(" ", "");
                    if (responseMd5.equals("") || responseMd5.toUpperCase().equals("NULL")) {
                        mediaContentVerifyStatus
                                .setMd5VerifyStatusType(Md5VerifyStatusType.RESPONES_NULL);
                        logger.info(
                                "run for mediaContentId: {} get responseMd5: {}, setMd5VerifyStatusType: {} and continue.",
                                mediaContentId, mediaContentVerifyStatus.getMd5VerifyStatusType());
                        continue;
                    }
                } else {
                    mediaContentVerifyStatus
                            .setMd5VerifyStatusType(Md5VerifyStatusType.RESPONES_NULL);
                    logger.info(
                            "run for mediaContentId: {} get responseMd5 is null, setMd5VerifyStatusType: {} and continue.",
                            mediaContentId, mediaContentVerifyStatus.getMd5VerifyStatusType());
                    continue;
                }

                mediaContentVerifyStatus.setResponseMd5(responseMd5);
                mediaContentVerifyStatus
                        .setMd5VerifyStatusType(Md5VerifyStatusType.GETING_LOCAL_FILE_MD5);
                logger.info("run for mediaContentId: {} setMd5VerifyStatusType: {} responseMd5: {}",
                        mediaContentId, mediaContentVerifyStatus.getMd5VerifyStatusType(),
                        responseMd5);

                String localFileMd5 = null;
                try {
                    localFileMd5 = localFileService.getMd5(new File(localFileUrl));
                } catch (IOException e) {
                    mediaContentVerifyStatus
                            .setMd5VerifyStatusType(Md5VerifyStatusType.GET_LOCAL_FILE_MD5_FAILUED);
                    logger.info(
                            "run for mediaContentId: {} localFileService getMd5 use localFileUrl: {} generate IOException, setMd5VerifyStatusType: {}. the exception:",
                            mediaContentId, localFileUrl,
                            mediaContentVerifyStatus.getMd5VerifyStatusType(), e);
                    continue;
                }

                logger.info("run for mediaContentId: {} getMd5 localFileUrl: {} localFileMd5: {}",
                        mediaContentId, localFileUrl, localFileMd5);
                mediaContentVerifyStatus.setLocalFileMd5(localFileMd5);
                int result = -1;
                if (responseMd5.equals(localFileMd5)) {
                    mediaContentVerifyStatus
                            .setMd5VerifyStatusType(Md5VerifyStatusType.VERIFY_SUCCESS);
                    logger.info(
                            "run for mediaContentId: {} verify success, setMd5VerifyStatusType: {}",
                            mediaContentId, mediaContentVerifyStatus.getMd5VerifyStatusType());
                    result = updateMediaContentStatus(mediaContentVerifyStatus,
                            MediaContentStatusType.ACTIVE);
                } else {
                    mediaContentVerifyStatus
                            .setMd5VerifyStatusType(Md5VerifyStatusType.VERIFY_FAILED);
                    logger.info(
                            "run for mediaContentId: {} verify failed, setMd5VerifyStatusType: {}",
                            mediaContentId, mediaContentVerifyStatus.getMd5VerifyStatusType());
                    result = updateMediaContentStatus(mediaContentVerifyStatus,
                            MediaContentStatusType.MD5VERIFY_FAILURE);
                }
                logger.info(
                        "run for mediaContentId: {} md5 veirify completed. update status result: {}",
                        mediaContentId, result);
            }
        }

        private final void doSleep() {

            logger.info("doSleep to sleep 10 seconds.");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                logger.info("doSleep to sleep 10 seconds generate exception:", e);
            }
        }

        private final int updateMediaContentStatus(
                MediaContentVerifyStatus mediaContentVerifyStatus, Character newStatus) {

            MediaContentStatus mediaContentStatus = mediaContentVerifyStatus
                    .getMediaContentStatus();
            int mediaContentId = mediaContentStatus.getMediaContentid();
            int result = mediaContentService.updateStatus(mediaContentId, newStatus);
            logger.info(
                    "updateMediaContentStatus for mediaContentId: {} md5VerifyStatus: {} srcFileurl: {} responseMd5: {} localFileurl: {} localFileMd5: {} update status from: {} to: {} result: {}",
                    mediaContentId, mediaContentVerifyStatus.getMd5VerifyStatusType(),
                    mediaContentStatus.getSrcFileurl(), mediaContentVerifyStatus.getResponseMd5(),
                    mediaContentStatus.getLocalFileUrl(),
                    mediaContentVerifyStatus.getLocalFileMd5(), mediaContentStatus.getStatus(),
                    newStatus, result);
            return result;
        }
    }
}
