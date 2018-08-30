/**
 * created on 2017年11月21日 上午10:13:37
 */
package cn.utstarcom.vmsadapter.md5verify.common;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public final class MediaContentStatusType {

    /**
     * this is tool class, private constructor to prevent create instance.
     */
    private MediaContentStatusType() {
        // do nothing.
    }

    // loading
    public final static Character LOADING = '0';

    // initialize
    public final static Character INITIALIZE = '1';

    // delete successful
    public final static Character DELETE_SUCCESSFUL = '2';

    // delete failed
    public final static Character DELETE_FAILED = '3';

    // active
    public final static Character ACTIVE = '4';

    // 待处理
    public final static Character PENGDING_HANDLE = '5';

    // 待删除
    public final static Character PENGDING_DELETE = '6';

    // 技审失败
    public final static Character AUDIT_FAILURE = '7';

    // 拷贝失败
    public final static Character COPY_FAILURE = '8';

    // 转码失败
    public final static Character TRANSCODE_FAILURE = '9';

    // 拷贝中
    public final static Character COPYING = 'A';

    // 转码中
    public final static Character TRANSCODING = 'B';

    // 技审中
    public final static Character AUDITING = 'C';

    // 视频分析中
    public final static Character PARSING = 'D';

    // 视频分析失败
    public final static Character PARSE_FAILURE = 'E';

    // 等待MD5检验
    public final static Character WAIT_MD5VERIFY = 'F';

    // MD5检验失败
    public final static Character MD5VERIFY_FAILURE = 'G';
}
