/**
 * created on 2017年11月24日 下午2:34:04
 */
package cn.utstarcom.vmsadapter.md5verify.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.utstarcom.vmsadapter.md5verify.bo.MediaContentVerifyStatus;

/**
 * @author UTSC0167
 * @date 2017年11月24日
 *
 */
public final class Md5verifyCache {

    /**
     * this is tool class, private constructor to prevent create instance.
     */
    private Md5verifyCache() {
        // do nothing.
    }

    // pending verification MediaContent queue
    public static final BlockingQueue<MediaContentVerifyStatus> MEDIACONTENTVERIFYSTATUSQUEUE = new LinkedBlockingQueue<>();
}
