/**
 * created on 2017年11月23日 上午10:43:16
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.utstarcom.vmsadapter.md5verify.common.Md5VerifyStatusType;
import cn.utstarcom.vmsadapter.md5verify.common.ProgramStatusType;
import cn.utstarcom.vmsadapter.md5verify.dao.ProgramDao;
import cn.utstarcom.vmsadapter.md5verify.dto.ProgramStatus;
import cn.utstarcom.vmsadapter.md5verify.pojo.Program;

/**
 * @author UTSC0167
 * @date 2017年11月23日
 *
 */
@Service
public class ProgramServiceImpl implements ProgramService {

    private static final Logger logger = LoggerFactory.getLogger(ProgramServiceImpl.class);

    @Autowired
    private ProgramDao programDao;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<ProgramStatus> queryPendingVerifyList(int md5ChkCntThreshold,
            int md5VerifyInterval) {

        return this.programDao.queryPendingVerifyList(md5ChkCntThreshold, md5VerifyInterval);
    }

    @Override
    @Transactional
    public int updateStatus(Md5VerifyStatusType md5VerifyStatus, Character auditFlag,
            ProgramStatus programStatus) {

        if (md5VerifyStatus == Md5VerifyStatusType.VERIFY_SUCCESS) {

            if (auditFlag.equals('1'))
                programStatus.setStatus(ProgramStatusType.INCOMPLETE);
            else if (auditFlag.equals('0'))
                programStatus.setStatus(ProgramStatusType.AUDIT_SUCCESS);
        } else {
            programStatus.setStatus(ProgramStatusType.MD5VERIFY_FAILURE);
            if (md5VerifyStatus == Md5VerifyStatusType.VERIFY_FAILED)
                programStatus.setMd5chkcnt(programStatus.getMd5chkcnt() + 1);
        }

        if (md5VerifyStatus == Md5VerifyStatusType.VERIFY_SUCCESS
                || md5VerifyStatus == Md5VerifyStatusType.VERIFY_FAILED) {
            logger.info(
                    "updateStatus for programId: {} md5VerifyStatus: {} auditFlag: {} update to status: {} md5chkcnt: {}",
                    programStatus.getProgramid(), md5VerifyStatus, auditFlag,
                    programStatus.getStatus(), programStatus.getMd5chkcnt());
            return this.programDao.updateStatus(programStatus);
        } else {
            logger.info(
                    "updateStatus for programId: {} md5VerifyStatus: {} auditFlag: {} update to status: {}",
                    programStatus.getProgramid(), md5VerifyStatus, auditFlag,
                    programStatus.getStatus());
            return this.programDao.updateStatus(programStatus.getProgramid(),
                    programStatus.getStatus());
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public int saveByNativeTest(Program program) {

        return this.programDao.saveByNativeTest(program);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public int deleteByPkTest(int programId) {

        return this.programDao.deleteByPkTest(programId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramStatus queryByPkTest2(int programId) {

        return this.programDao.queryByPkTest2(programId);
    }

}
