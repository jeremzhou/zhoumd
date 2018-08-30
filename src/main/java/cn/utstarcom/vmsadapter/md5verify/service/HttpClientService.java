/**
 * created on 2017年11月24日 下午1:24:10
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import cn.utstarcom.vmsadapter.md5verify.bo.Md5VerifyRequest;
import cn.utstarcom.vmsadapter.md5verify.bo.Md5VerifyResponse;

/**
 * @author UTSC0167
 * @date 2017年11月24日
 *
 */
public interface HttpClientService {

    Md5VerifyResponse doPost(String requestUrl, Md5VerifyRequest md5VerifyRequest)
            throws ConnectException, TimeoutException, Exception;
}
