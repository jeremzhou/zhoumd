/**
 * created on 2017年11月23日 下午5:06:05
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.utstarcom.vmsadapter.md5verify.dao.MediaContentDao;
import cn.utstarcom.vmsadapter.md5verify.dto.MediaContentStatus;
import cn.utstarcom.vmsadapter.md5verify.pojo.MediaContent;

/**
 * @author UTSC0167
 * @date 2017年11月23日
 *
 */
@Service
public class MediaContentServiceImpl implements MediaContentService {

    @Autowired
    private MediaContentDao mediaContentDao;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public List<MediaContentStatus> queryPendingVerifyList(int programId) {

        return this.mediaContentDao.queryPendingVerifyList(programId);
    }

    @Override
    @Transactional
    public int updateStatus(int mediaContentId, Character status) {

        return this.mediaContentDao.updateStatus(mediaContentId, status);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public int saveByNativeTest(MediaContent mediaContent) {

        return mediaContentDao.saveByNativeTest(mediaContent);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public int deleteByPkTest(int mediaContentId) {

        return mediaContentDao.deleteByPkTest(mediaContentId);
    }

    @Override
    @Transactional(readOnly = true)
    public MediaContentStatus queryByPkTest2(int mediaContentId) {

        return mediaContentDao.queryByPkTest2(mediaContentId);
    }
}
