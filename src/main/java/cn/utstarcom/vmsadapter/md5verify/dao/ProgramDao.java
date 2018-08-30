/**
 * created on 2017年11月21日 下午1:58:24
 */
package cn.utstarcom.vmsadapter.md5verify.dao;

import java.util.List;

import cn.utstarcom.vmsadapter.md5verify.dto.ProgramStatus;
import cn.utstarcom.vmsadapter.md5verify.pojo.Program;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public interface ProgramDao {

    List<ProgramStatus> queryPendingVerifyList(int md5ChkCntThreshold, int md5VerifyInterval);

    int updateStatus(ProgramStatus programStatus);

    int updateStatus(int programId, Character status);
    
    int saveByNativeTest(Program program);
    
    int deleteByPkTest(int programId);
    
    int saveByNativeTest2(Program program);
    
    int deleteByPkTest2(int programId);
    
    ProgramStatus queryByPkTest2(int programId);
    
}
