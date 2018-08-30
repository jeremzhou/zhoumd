/**
 * created on 2017年11月24日 上午9:58:07
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import java.io.File;
import java.io.IOException;

/**
 * @author UTSC0167
 * @date 2017年11月24日
 *
 */
public interface LocalFileService {

    String getMd5(File file)  throws IOException;
}
