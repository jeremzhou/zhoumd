/**
 * created on 2017年11月21日 上午11:11:42
 */
package cn.utstarcom.vmsadapter.md5verify.bo;

import cn.utstarcom.vmsadapter.md5verify.dto.ProgramStatus;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public class ProgramVerifyStatus {

    private final ProgramStatus programStatus;
    // private final List<Integer> mediaContentIdList = new LinkedList<>();
    private boolean isOk = true;
    private boolean isUpdate = false;
    private int failureCount = 0;

    public ProgramVerifyStatus(ProgramStatus programStatus) {

        this.programStatus = programStatus;
    }

    public ProgramStatus getProgramStatus() {
        return programStatus;
    }

    // public List<Integer> getMediaContentIdList() {
    // return mediaContentIdList;
    // }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean isOk) {
        this.isOk = isOk;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }
}
