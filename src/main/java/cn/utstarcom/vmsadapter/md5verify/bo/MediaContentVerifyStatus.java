/**
 * created on 2017年11月21日 上午11:17:15
 */
package cn.utstarcom.vmsadapter.md5verify.bo;

import java.util.LinkedList;
import java.util.List;

import cn.utstarcom.vmsadapter.md5verify.common.Md5VerifyStatusType;
import cn.utstarcom.vmsadapter.md5verify.dto.MediaContentStatus;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public class MediaContentVerifyStatus {

    private final MediaContentStatus mediaContentStatus;
    private final List<Integer> programIdLIst = new LinkedList<>();
    private String responseMd5;
    private String localFileMd5;
    private volatile Md5VerifyStatusType md5VerifyStatusType;
    private int retransmissionCount = 0;

    public MediaContentVerifyStatus(MediaContentStatus mediaContentStatus,
            Md5VerifyStatusType md5VerifyStatusType) {
        super();
        this.mediaContentStatus = mediaContentStatus;
        this.md5VerifyStatusType = md5VerifyStatusType;
    }

    public String getResponseMd5() {
        return responseMd5;
    }

    public void setResponseMd5(String responseMd5) {
        this.responseMd5 = responseMd5;
    }

    public String getLocalFileMd5() {
        return localFileMd5;
    }

    public void setLocalFileMd5(String localFileMd5) {
        this.localFileMd5 = localFileMd5;
    }

    public Md5VerifyStatusType getMd5VerifyStatusType() {
        return md5VerifyStatusType;
    }

    public void setMd5VerifyStatusType(Md5VerifyStatusType md5VerifyStatusType) {
        this.md5VerifyStatusType = md5VerifyStatusType;
    }

    public MediaContentStatus getMediaContentStatus() {
        return mediaContentStatus;
    }

    public List<Integer> getProgramIdLIst() {
        return programIdLIst;
    }

    public int getRetransmissionCount() {
        return retransmissionCount;
    }

    public void setRetransmissionCount(int retransmissionCount) {
        this.retransmissionCount = retransmissionCount;
    }
}
