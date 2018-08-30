/**
 * created on 2017年11月21日 下午2:15:48
 */
package cn.utstarcom.vmsadapter.md5verify.dao;

import java.util.List;

import cn.utstarcom.vmsadapter.md5verify.dto.MediaContentStatus;
import cn.utstarcom.vmsadapter.md5verify.pojo.MediaContent;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public interface MediaContentDao {

    List<MediaContentStatus> queryPendingVerifyList(int programId);

    int updateStatus(int mediaContentId, Character status);

    int saveByNativeTest(MediaContent mediaContent);

    int deleteByPkTest(int mediaContentId);

    int saveByNativeTest2(MediaContent mediaContent);

    int deleteByPkTest2(int mediaContentId);

    MediaContentStatus queryByPkTest2(int mediaContentId);
}
