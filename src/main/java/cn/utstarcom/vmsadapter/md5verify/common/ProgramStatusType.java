/**
 * created on 2017年11月21日 上午10:36:31
 */
package cn.utstarcom.vmsadapter.md5verify.common;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
public final class ProgramStatusType {

    /**
     * this is tool class, private constructor to prevent create instance.
     */
    private ProgramStatusType() {
        // do nothing.
    }

    // 编目未完成
    public final static Character INCOMPLETE = '1';

    // 等待审核
    public final static Character WAIT_AUDIT = '2';

    // 审核未通过
    public final static Character AUDIT_FAILURE = '3';

    // 审核已通过
    public final static Character AUDIT_SUCCESS = '4';

    // 内容待处理
    public final static Character CONTENT_PENDING_HANDLE = '5';

    // 已删除
    public final static Character DELETED = '9';

    // 等待MD5检验
    public final static Character WAIT_MD5VERIFY = 'F';

    // MD5检验失败
    public final static Character MD5VERIFY_FAILURE = 'G';
}
