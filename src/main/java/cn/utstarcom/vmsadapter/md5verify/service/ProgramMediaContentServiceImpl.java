/**
 * created on 2017年11月30日 下午5:06:49
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.utstarcom.vmsadapter.md5verify.dao.ProgramMediaContentDao;
import cn.utstarcom.vmsadapter.md5verify.pojo.ProgramMediaContent;

/**
 * @author UTSC0167
 * @date 2017年11月30日
 *
 */
@Service
public class ProgramMediaContentServiceImpl implements ProgramMediaContentService {

    @Autowired
    private ProgramMediaContentDao programMediaContentDao;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public int saveByNativeTest(ProgramMediaContent programMediaContent) {

        return this.programMediaContentDao.saveByNativeTest(programMediaContent);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public int deleteByPkTest(int programMediaContentId) {

        return this.programMediaContentDao.deleteByPkTest(programMediaContentId);
    }

}
