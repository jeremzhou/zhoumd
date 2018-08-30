/**
 * created on 2017年11月21日 上午10:44:18
 */
package cn.utstarcom.vmsadapter.md5verify.common;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public enum Md5VerifyStatusType {

                                 LOADED,
                                 PENDING,
                                 PENDING_RETRANSMISSION,
                                 REQUESTING,
                                 CONNECT_FAILED,
                                 TIMEOUT,
                                 RESPONES_NULL,
                                 GETING_LOCAL_FILE_MD5,
                                 GET_LOCAL_FILE_MD5_FAILUED,
                                 VERIFY_FAILED,
                                 VERIFY_SUCCESS
}
