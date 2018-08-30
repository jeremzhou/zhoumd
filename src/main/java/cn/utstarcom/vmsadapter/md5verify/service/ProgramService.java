/**
 * created on 2017年11月22日 下午4:09:28
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import java.util.List;

import cn.utstarcom.vmsadapter.md5verify.common.Md5VerifyStatusType;
import cn.utstarcom.vmsadapter.md5verify.dto.ProgramStatus;
import cn.utstarcom.vmsadapter.md5verify.pojo.Program;

/**
 * @author UTSC0167
 * @date 2017年11月22日
 *
 */
public interface ProgramService {

    List<ProgramStatus> queryPendingVerifyList(int md5ChkCntThreshold, int md5VerifyInterval);

    int updateStatus(Md5VerifyStatusType md5VerifyStatus, Character auditFlag,
            ProgramStatus programStatus);

    int saveByNativeTest(Program program);

    int deleteByPkTest(int programId);

    ProgramStatus queryByPkTest2(int programId);
}
