/**
 * created on 2017年11月30日 下午2:34:48
 */
package cn.utstarcom.vmsadapter.md5verify.data;

import java.util.Date;

import cn.utstarcom.vmsadapter.md5verify.common.ProgramStatusType;
import cn.utstarcom.vmsadapter.md5verify.pojo.Program;

/**
 * @author UTSC0167
 * @date 2017年11月30日
 *
 */
public final class ProgramData {

    private ProgramData() {

    }

    public static final int maxId = 99999999;

    public static final Program program1 = new Program();
    public static final Program program2 = new Program();
    public static final Program program3 = new Program();
    public static final Program program4 = new Program();
    public static final Program program5 = new Program();
    public static final Program program6 = new Program();

    static {
        program1.setProgramid(maxId - 1);
        program1.setStatus(ProgramStatusType.INCOMPLETE);
        program1.setMd5chkcnt(1);
        program1.setMd5chklasttime(new Date(new Date().getTime() - 2 * 24 * 60 * 60 * 1000));

        program2.setProgramid(maxId - 2);
        program2.setStatus(ProgramStatusType.WAIT_MD5VERIFY);
        program2.setMd5chkcnt(6);
        program2.setMd5chklasttime(new Date(new Date().getTime() - 2 * 24 * 60 * 60 * 1000));

        program3.setProgramid(maxId - 3);
        program3.setStatus(ProgramStatusType.WAIT_MD5VERIFY);
        program3.setMd5chkcnt(6);
        program3.setMd5chklasttime(new Date());

        program4.setProgramid(maxId - 4);
        program4.setStatus(ProgramStatusType.AUDIT_SUCCESS);
        program4.setMd5chkcnt(1);
        program4.setMd5chklasttime(new Date(new Date().getTime() - 2 * 24 * 60 * 60 * 1000));

        program5.setProgramid(maxId - 5);
        program5.setStatus(ProgramStatusType.MD5VERIFY_FAILURE);
        program5.setMd5chkcnt(6);
        program5.setMd5chklasttime(new Date(new Date().getTime() - 2 * 24 * 60 * 60 * 1000));

        program6.setProgramid(maxId - 6);
        program6.setStatus(ProgramStatusType.MD5VERIFY_FAILURE);
        program6.setMd5chkcnt(6);
        program6.setMd5chklasttime(new Date());
    }
}
