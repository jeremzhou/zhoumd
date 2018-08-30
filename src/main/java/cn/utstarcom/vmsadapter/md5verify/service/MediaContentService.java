/**
 * created on 2017年11月22日 下午4:09:46
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import java.util.List;

import cn.utstarcom.vmsadapter.md5verify.dto.MediaContentStatus;
import cn.utstarcom.vmsadapter.md5verify.pojo.MediaContent;

/**
 * @author UTSC0167
 * @date 2017年11月22日
 *
 */
public interface MediaContentService {

    List<MediaContentStatus> queryPendingVerifyList(int programId);

    int updateStatus(int mediaContentId, Character status);
    
    int saveByNativeTest(MediaContent mediaContent);

    int deleteByPkTest(int mediaContentId);

    MediaContentStatus queryByPkTest2(int mediaContentId);
}
