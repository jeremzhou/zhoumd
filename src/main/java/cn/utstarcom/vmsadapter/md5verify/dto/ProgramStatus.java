/**
 * created on 2017年11月20日 下午5:27:40
 */
package cn.utstarcom.vmsadapter.md5verify.dto;

import java.util.Date;

/**
 * @author UTSC0167
 * @date 2017年11月20日
 *
 */
public final class ProgramStatus {

    private final int programId;
    private Character status;
    private Integer md5ChkCnt;
    private Date md5ChkLastTime;

    public ProgramStatus(int programId) {

        this.programId = programId;
    }

    public ProgramStatus(int programId, Character status, Integer md5ChkCnt, Date md5ChkLastTime) {
        super();
        this.programId = programId;
        this.status = status;
        this.md5ChkCnt = md5ChkCnt;
        this.md5ChkLastTime = md5ChkLastTime;
    }

    public int getProgramid() {
        return programId;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Integer getMd5chkcnt() {
        return md5ChkCnt;
    }

    public void setMd5chkcnt(Integer md5ChkCnt) {
        this.md5ChkCnt = md5ChkCnt;
    }

    public Date getMd5chklasttime() {
        return md5ChkLastTime;
    }

    public void setMd5chklasttime(Date md5ChkLastTime) {
        this.md5ChkLastTime = md5ChkLastTime;
    }

    @Override
    public String toString() {
        return "ProgramStatus [programId=" + programId + ", status=" + status + ", md5ChkCnt="
                + md5ChkCnt + ", md5ChkLastTime=" + md5ChkLastTime + "]";
    }
}
