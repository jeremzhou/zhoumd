/**
 * created on 2017年11月24日 上午10:03:59
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;

/**
 * @author UTSC0167
 * @date 2017年11月24日
 *
 */
@Service
public class LocalFileServiceImpl implements LocalFileService {

    @SuppressWarnings("deprecation")
    @Override
    public String getMd5(File file) throws IOException {

        return Files.hash(file, Hashing.md5()).toString();
    }

}
