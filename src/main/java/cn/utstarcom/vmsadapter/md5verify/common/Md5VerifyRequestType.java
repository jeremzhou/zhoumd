/**
 * created on 2017年11月21日 上午10:47:03
 */
package cn.utstarcom.vmsadapter.md5verify.common;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public final class Md5VerifyRequestType {

    /**
     * this is tool class, private constructor to prevent create instance.
     */
    private Md5VerifyRequestType() {
        // do nothing.
    }

    // 正常校验
    public final static Character VERIFY = '0';

    // 校验失败，重传
    public final static Character RETRANSMISSION = '1';
}
