/**
 * created on 2017年11月24日 下午2:28:27
 */
package cn.utstarcom.vmsadapter.md5verify.task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.utstarcom.vmsadapter.md5verify.bo.MediaContentVerifyStatus;
import cn.utstarcom.vmsadapter.md5verify.bo.ProgramVerifyStatus;
import cn.utstarcom.vmsadapter.md5verify.common.Md5VerifyStatusType;
import cn.utstarcom.vmsadapter.md5verify.common.Md5verifyCache;
import cn.utstarcom.vmsadapter.md5verify.common.MediaContentStatusType;
import cn.utstarcom.vmsadapter.md5verify.dto.MediaContentStatus;
import cn.utstarcom.vmsadapter.md5verify.dto.ProgramStatus;
import cn.utstarcom.vmsadapter.md5verify.service.MediaContentService;
import cn.utstarcom.vmsadapter.md5verify.service.ProgramService;

/**
 * @author UTSC0167
 * @date 2017年11月24日
 *
 */
@EnableScheduling
@Component
public final class Md5verifyTask {

    private static final Logger logger = LoggerFactory.getLogger(Md5verifyTask.class);

    // being verified MediaContent map
    private static final Map<Integer, MediaContentVerifyStatus> mediaContentVerifyStatusMap = new HashMap<>();

    // being verified Program map
    private static final Map<Integer, ProgramVerifyStatus> programVerifyStatusMap = new HashMap<>();

    @Value("${vmsadapter.md5verify.isTest:false}")
    private boolean isTest;

    @Value("${vmsadapter.md5verify.auditflag}")
    private Character auditFlag;

    @Value("${vmsadapter.md5verify.task.count}")
    private int md5ChkCntThreshold;

    @Value("${vmsadapter.md5verify.task.interval}")
    private int md5VerifyInterval;

    @Autowired
    private MediaContentService mediaContentService;

    @Autowired
    private ProgramService programService;

    @Scheduled(cron = "${vmsadapter.md5verify.task.cronexpression}")
    private void doVerify() {

        if (isTest)
            return;

        logger.info("doVerify begin to run.");

        // handle verified media content
        handleVerifiedMediaConent();

        // handle verified program
        handleVerifiedProgram();

        // load pending verification program and media content list
        loadPendingVerifyProgram();

        // put pending media content verify task to MediaContentVerifyStatusQueue
        addMediaContentVerifyStatusToQueue();

        logger.info("doVerify end to run.");

    }

    private final void handleVerifiedMediaConent() {

        logger.info("handleVerifiedMediaConent begin to run. mediaContentVerifyStatusMap size: {}",
                mediaContentVerifyStatusMap.size());

        Iterator<Map.Entry<Integer, MediaContentVerifyStatus>> iterator = mediaContentVerifyStatusMap
                .entrySet().iterator();
        while (iterator.hasNext()) {

            Map.Entry<Integer, MediaContentVerifyStatus> entry = iterator.next();
            MediaContentVerifyStatus mediaContentVerifyStatus = entry.getValue();
            final Md5VerifyStatusType md5VerifyStatus = mediaContentVerifyStatus
                    .getMd5VerifyStatusType();

            logger.info(
                    "handleVerifiedMediaConent for mediaContentId: {} srcFileurl: {} localFileurl: {} md5VerifyStatus: {}",
                    entry.getKey(),
                    mediaContentVerifyStatus.getMediaContentStatus().getSrcFileurl(),
                    mediaContentVerifyStatus.getMediaContentStatus().getLocalFileUrl(),
                    md5VerifyStatus);

            switch (md5VerifyStatus) {
            case VERIFY_SUCCESS:
                updateProgramVerifyStatus(mediaContentVerifyStatus, true, true, 0);
                discardMediaContentVerifyStatus(mediaContentVerifyStatus, iterator);
                break;

            case VERIFY_FAILED:
                updateProgramVerifyStatus(mediaContentVerifyStatus, false, true, 1);
                handleMediaContentRetransmission(mediaContentVerifyStatus);
                break;

            case LOADED:
            case PENDING:
            case PENDING_RETRANSMISSION:
            case REQUESTING:
            case GETING_LOCAL_FILE_MD5:
                handleMediaContentWaiting(mediaContentVerifyStatus);
                updateProgramVerifyStatus(mediaContentVerifyStatus, false, false, 0);
                break;

            case CONNECT_FAILED:
            case TIMEOUT:
            case RESPONES_NULL:
            case GET_LOCAL_FILE_MD5_FAILUED:
                updateProgramVerifyStatus(mediaContentVerifyStatus, false, true, 0);
                discardMediaContentVerifyStatus(mediaContentVerifyStatus, iterator);
                break;

            default:
                logger.info(
                        "handleVerifiedMediaConent for mediaConentid: {} the status: {} unkown, discard it.",
                        entry.getKey(), md5VerifyStatus);
                updateProgramVerifyStatus(mediaContentVerifyStatus, false, true, 0);
                discardMediaContentVerifyStatus(mediaContentVerifyStatus, iterator);
                break;
            }

        }

        logger.info("handleVerifiedMediaConent end to run. mediaContentVerifyStatusMap size: {}",
                mediaContentVerifyStatusMap.size());

    }

    private final void handleVerifiedProgram() {

        logger.info("handleVerifiedProgram begin to run. programVerifyStatusMap size: {}",
                programVerifyStatusMap.size());

        for (Map.Entry<Integer, ProgramVerifyStatus> entry : programVerifyStatusMap.entrySet()) {

            int programId = entry.getKey();
            ProgramVerifyStatus programVerifyStatus = entry.getValue();
            ProgramStatus programStatus = programVerifyStatus.getProgramStatus();
            logger.info("handleVerifiedProgram for programId: {} loaded programStatus: {}",
                    programId, programStatus);

            Md5VerifyStatusType md5VerifyStatus = Md5VerifyStatusType.VERIFY_SUCCESS;
            if (!programVerifyStatus.isOk()) {
                if (programVerifyStatus.isUpdate()) {
                    if (programVerifyStatus.getFailureCount() > 0)
                        md5VerifyStatus = Md5VerifyStatusType.VERIFY_FAILED;
                    else
                        md5VerifyStatus = Md5VerifyStatusType.CONNECT_FAILED;
                }
            }
            logger.info(
                    "handleVerifiedProgram for programId: {} isOk: {} isUpdate: {} failureCount: {} md5VerifyStatus: {}",
                    programId, programVerifyStatus.isOk(), programVerifyStatus.isUpdate(),
                    programVerifyStatus.getFailureCount(), md5VerifyStatus);

            int result = 0;
            if (programVerifyStatus.isOk() || programVerifyStatus.isUpdate()) {
                result = this.programService.updateStatus(md5VerifyStatus, this.auditFlag,
                        programStatus);
                logger.info("handleVerifiedProgram for programId: {} updateStatus result: {}",
                        programId, result);
            }
        }

        programVerifyStatusMap.clear();

        logger.info("handleVerifiedProgram end to run. programVerifyStatusMap size: {}",
                programVerifyStatusMap.size());
    }

    private final void loadPendingVerifyProgram() {

        logger.info("loadPendingVerifyProgram begin to run. programVerifyStatusMap size: {}",
                programVerifyStatusMap.size());

        List<ProgramStatus> programStatusList = this.programService
                .queryPendingVerifyList(md5ChkCntThreshold, md5VerifyInterval);
        logger.info(
                "loadPendingVerifyProgram use md5ChkCntThreshold: {} md5VerifyInterval: {} queryPendingVerifyList size: {}",
                md5ChkCntThreshold, md5VerifyInterval, programStatusList.size());

        final Set<MediaContentStatus> newMediaContentStatusSet = new HashSet<>();
        for (ProgramStatus programStatus : programStatusList) {
            int programId = programStatus.getProgramid();
            logger.info("loadPendingVerifyProgram loaded programStatus: {}", programStatus);
            ProgramVerifyStatus programVerifyStatus = new ProgramVerifyStatus(programStatus);
            programVerifyStatusMap.put(programId, programVerifyStatus);
            loadPendingVerifyMediaContent(programId, newMediaContentStatusSet);
        }

        cleanMediaContentVerifyStatus(newMediaContentStatusSet);

        logger.info(
                "loadPendingVerifyProgram end to run. programVerifyStatusMap size: {} mediaContentVerifyStatusMap.size: {}",
                programVerifyStatusMap.size(), mediaContentVerifyStatusMap.size());
    }

    private final void loadPendingVerifyMediaContent(int programId,
            Set<MediaContentStatus> newMediaContentStatusSet) {

        List<MediaContentStatus> mediaContentStatusList = this.mediaContentService
                .queryPendingVerifyList(programId);
        logger.info(
                "loadPendingVerifyMediaContent for programId: {} mediaContentStatusList size: {}",
                programId, mediaContentStatusList.size());
        for (MediaContentStatus mediaContentStatus : mediaContentStatusList) {
            int mediaContentId = mediaContentStatus.getMediaContentid();
            logger.info("loadPendingVerifyMediaContent loaded mediaContentStatus: {}",
                    mediaContentStatus);

            MediaContentVerifyStatus mediaContentVerifyStatus = new MediaContentVerifyStatus(
                    mediaContentStatus, Md5VerifyStatusType.LOADED);
            mediaContentVerifyStatus.getProgramIdLIst().add(programId);

            if (mediaContentVerifyStatusMap.containsKey(mediaContentId)) {
                logger.info(
                        "loadPendingVerifyMediaContent for mediaContentId: {} in mediaContentVerifyStatusMap.",
                        mediaContentId);
                MediaContentVerifyStatus oldMediaContentVerifyStatus = mediaContentVerifyStatusMap
                        .get(mediaContentId);
                MediaContentStatus oldMediaContentStatus = oldMediaContentVerifyStatus
                        .getMediaContentStatus();
                if (oldMediaContentStatus.equals(mediaContentStatus)) {
                    logger.info(
                            "loadPendingVerifyMediaContent for mediaContentId: {} the mediaContentStatus equals oldMediaContentStatus. oldMediaContentVerifyStatus Md5VerifyStatus: {}",
                            mediaContentId, oldMediaContentVerifyStatus.getMd5VerifyStatusType());
                    oldMediaContentVerifyStatus.getProgramIdLIst().add(programId);
                } else {
                    logger.info(
                            "loadPendingVerifyMediaContent for mediaContentId: {} the mediaContentStatus don't equals oldMediaContentStatus. discard oldMediaContentVerifyStatus and use new.",
                            mediaContentId);
                    mediaContentVerifyStatus.getProgramIdLIst()
                            .addAll(oldMediaContentVerifyStatus.getProgramIdLIst());
                    mediaContentVerifyStatusMap.put(mediaContentId, mediaContentVerifyStatus);
                }
            } else {
                mediaContentVerifyStatusMap.put(mediaContentId, mediaContentVerifyStatus);
            }

            newMediaContentStatusSet.add(mediaContentStatus);
        }
    }

    private final void addMediaContentVerifyStatusToQueue() {

        logger.info(
                "addMediaContentVerifyStatusToQueue begin to run. MEDIACONTENTVERIFYSTATUSQUEUE size: {}",
                Md5verifyCache.MEDIACONTENTVERIFYSTATUSQUEUE.size());

        for (Map.Entry<Integer, MediaContentVerifyStatus> entry : mediaContentVerifyStatusMap
                .entrySet()) {

            int mediaContentId = entry.getKey();
            MediaContentVerifyStatus mediaContentVerifyStatus = entry.getValue();
            Md5VerifyStatusType md5VerifyStatus = mediaContentVerifyStatus.getMd5VerifyStatusType();
            logger.info(
                    "addMediaContentVerifyStatusToQueue for mediaContentId: {} md5VerifyStatus: {}",
                    mediaContentId, md5VerifyStatus);
            if (md5VerifyStatus.equals(Md5VerifyStatusType.LOADED)
                    || md5VerifyStatus.equals(Md5VerifyStatusType.PENDING_RETRANSMISSION)) {
                if (md5VerifyStatus.equals(Md5VerifyStatusType.LOADED))
                    mediaContentVerifyStatus.setMd5VerifyStatusType(Md5VerifyStatusType.PENDING);
                Md5verifyCache.MEDIACONTENTVERIFYSTATUSQUEUE.add(mediaContentVerifyStatus);
            }
        }

        logger.info(
                "addMediaContentVerifyStatusToQueue end to run. MEDIACONTENTVERIFYSTATUSQUEUE size: {}",
                Md5verifyCache.MEDIACONTENTVERIFYSTATUSQUEUE.size());
    }

    private final void updateProgramVerifyStatus(MediaContentVerifyStatus mediaContentVerifyStatus,
            boolean isOk, boolean isUpdate, int failureCount) {
        MediaContentStatus mediaContentStatus = mediaContentVerifyStatus.getMediaContentStatus();
        final int mediaContentId = mediaContentStatus.getMediaContentid();
        logger.info(
                "updateProgramVerifyStatus for mediaContentId: {} rleated programVerifyStatus isOk: {} isUpdate: {} failureCount: {}",
                mediaContentId, isOk, isUpdate, failureCount);
        for (int programId : mediaContentVerifyStatus.getProgramIdLIst()) {

            ProgramVerifyStatus programVerifyStatus = programVerifyStatusMap.get(programId);
            if (programVerifyStatus == null) {
                logger.info(
                        "updateProgramVerifyStatus for mediaContentId: {} releated programId: {} get programVerifyStatus is null. do nothing and continue.",
                        mediaContentId, programId);
                continue;

            }
            if (!isOk) {
                programVerifyStatus.setOk(isOk);
                logger.info(
                        "updateProgramVerifyStatus for mediaContentId: {} releated programId: {} programVerifyStatus.setOk: {}",
                        mediaContentId, programId, isOk);

                if (isUpdate) {
                    programVerifyStatus.setUpdate(isUpdate);
                    logger.info(
                            "updateProgramVerifyStatus for mediaContentId: {} releated programId: {} programVerifyStatus.setUpdate: {}",
                            mediaContentId, programId, isUpdate);
                }
            }

            programVerifyStatus
                    .setFailureCount(programVerifyStatus.getFailureCount() + failureCount);

        }

    }

    private final void discardMediaContentVerifyStatus(
            MediaContentVerifyStatus mediaContentVerifyStatus,
            Iterator<Map.Entry<Integer, MediaContentVerifyStatus>> iterator) {
        MediaContentStatus mediaContentStatus = mediaContentVerifyStatus.getMediaContentStatus();
        final int mediaContentId = mediaContentStatus.getMediaContentid();
        logger.info(
                "discardMediaContentVerifyStatus for mediaContentId: {} md5VerifyStatus: {} srcFileurl: {} responseMd5: {} localFileurl: {} localFileMd5: {} , discart it.",
                mediaContentId, mediaContentVerifyStatus.getMd5VerifyStatusType(),
                mediaContentStatus.getSrcFileurl(), mediaContentVerifyStatus.getResponseMd5(),
                mediaContentStatus.getLocalFileUrl(), mediaContentVerifyStatus.getLocalFileMd5());

        // 清空MediaContentVerifyStatus的programList
        mediaContentVerifyStatus.getProgramIdLIst().clear();
        logger.info(
                "discardMediaContentVerifyStatus for mediaContentId: {} clear mediaContentVerifyStatus programIdList.",
                mediaContentId);

        // delete mediaContentId from mediaContentVerifyStatusMap
        iterator.remove();
        logger.info(
                "discardMediaContentVerifyStatus rmove mediaContentId: {} from mediaContentVerifyStatusMap and discard completed.",
                mediaContentId);
    }

    private final void handleMediaContentWaiting(
            MediaContentVerifyStatus mediaContentVerifyStatus) {
        MediaContentStatus mediaContentStatus = mediaContentVerifyStatus.getMediaContentStatus();
        logger.info(
                "handleMediaContentWaiting for mediaContentId: {} md5VerifyStatus: {} srcFileurl: {} responseMd5: {} localFileurl: {} localFileMd5: {} , waiting and do nothing.",
                mediaContentStatus.getMediaContentid(),
                mediaContentVerifyStatus.getMd5VerifyStatusType(),
                mediaContentStatus.getSrcFileurl(), mediaContentVerifyStatus.getResponseMd5(),
                mediaContentStatus.getLocalFileUrl(), mediaContentVerifyStatus.getLocalFileMd5());

        // 清空MediaContentVerifyStatus的programList
        mediaContentVerifyStatus.getProgramIdLIst().clear();

    }

    private final void handleMediaContentRetransmission(
            MediaContentVerifyStatus mediaContentVerifyStatus) {

        MediaContentStatus mediaContentStatus = mediaContentVerifyStatus.getMediaContentStatus();
        int mediaContentId = mediaContentStatus.getMediaContentid();
        logger.info(
                "handleMediaContentRetransmission for mediaContentId: {} md5VerifyStatus: {} srcFileurl: {} responseMd5: {} localFileurl: {} localFileMd5: {} retransmissionCount: {}",
                mediaContentId, mediaContentVerifyStatus.getMd5VerifyStatusType(),
                mediaContentStatus.getSrcFileurl(), mediaContentVerifyStatus.getResponseMd5(),
                mediaContentStatus.getLocalFileUrl(), mediaContentVerifyStatus.getLocalFileMd5(),
                mediaContentVerifyStatus.getRetransmissionCount());
        mediaContentStatus.setStatus(MediaContentStatusType.MD5VERIFY_FAILURE);
        mediaContentVerifyStatus.setMd5VerifyStatusType(Md5VerifyStatusType.PENDING_RETRANSMISSION);
        mediaContentVerifyStatus.setResponseMd5(null);
        mediaContentVerifyStatus.setLocalFileMd5(null);
        mediaContentVerifyStatus.getProgramIdLIst().clear();
        mediaContentVerifyStatus
                .setRetransmissionCount(mediaContentVerifyStatus.getRetransmissionCount() + 1);
        logger.info(
                "handleMediaContentRetransmission for mediaContentId: {} modify to mediaContentStatus: {} md5VerifyStatus: {} responseMd5: null LocalFileMd5: null retransmissionCount: {}",
                mediaContentId, mediaContentStatus.getStatus(),
                mediaContentVerifyStatus.getMd5VerifyStatusType(),
                mediaContentVerifyStatus.getRetransmissionCount());
    }

    private final void cleanMediaContentVerifyStatus(
            Set<MediaContentStatus> newMediaContentStatusSet) {

        Iterator<Map.Entry<Integer, MediaContentVerifyStatus>> iterator = mediaContentVerifyStatusMap
                .entrySet().iterator();
        while (iterator.hasNext()) {

            Map.Entry<Integer, MediaContentVerifyStatus> entry = iterator.next();
            MediaContentVerifyStatus mediaContentVerifyStatus = entry.getValue();
            MediaContentStatus mediaContentStatus = mediaContentVerifyStatus
                    .getMediaContentStatus();

            if (newMediaContentStatusSet.contains(mediaContentStatus))
                continue;

            if (mediaContentVerifyStatus.getMd5VerifyStatusType()
                    .equals(Md5VerifyStatusType.PENDING))
                continue;

            if (mediaContentVerifyStatus.getMd5VerifyStatusType()
                    .equals(Md5VerifyStatusType.PENDING_RETRANSMISSION)
                    && mediaContentVerifyStatus
                            .getRetransmissionCount() < this.md5ChkCntThreshold) {
                logger.info(
                        "cleanMediaContentVerifyStatus for mediaContentStatus: {} contiue retransmission count: {} less than md5ChkCntThreshold: {} , keep it.",
                        mediaContentStatus, mediaContentVerifyStatus.getRetransmissionCount(),
                        this.md5ChkCntThreshold);
                continue;
            } else
                logger.info(
                        "cleanMediaContentVerifyStatus for mediaContentStatus: {} contiue retransmission count: {} greate than md5ChkCntThreshold: {} , clear it.",
                        mediaContentStatus, mediaContentVerifyStatus.getRetransmissionCount(),
                        this.md5ChkCntThreshold);

            logger.info("cleanMediaContentVerifyStatus remove mediaContentStatus: {}",
                    mediaContentStatus);
            iterator.remove();
        }
    }
}
