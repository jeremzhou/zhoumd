/**
 * created on 2017年11月30日 下午2:21:50
 */
package cn.utstarcom.vmsadapter.md5verify.dao;

import cn.utstarcom.vmsadapter.md5verify.pojo.ProgramMediaContent;

/**
 * @author UTSC0167
 * @date 2017年11月30日
 *
 */
public interface ProgramMediaContentDao {

    int saveByNativeTest(ProgramMediaContent programMediaContent);

    int deleteByPkTest(int programMediaContentId);
}
