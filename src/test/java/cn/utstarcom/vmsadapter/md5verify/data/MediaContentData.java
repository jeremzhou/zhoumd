/**
 * created on 2017年11月30日 下午2:39:24
 */
package cn.utstarcom.vmsadapter.md5verify.data;

import cn.utstarcom.vmsadapter.md5verify.common.MediaContentStatusType;
import cn.utstarcom.vmsadapter.md5verify.pojo.MediaContent;

/**
 * @author UTSC0167
 * @date 2017年11月30日
 *
 */
public final class MediaContentData {
    
    private MediaContentData() {
        
    }

    private static final int maxId = ProgramData.maxId;

    public static final MediaContent mediaContent1 = new MediaContent();
    public static final MediaContent mediaContent2 = new MediaContent();
    public static final MediaContent mediaContent3 = new MediaContent();
    public static final MediaContent mediaContent4 = new MediaContent();
    public static final MediaContent mediaContent5 = new MediaContent();
    public static final MediaContent mediaContent6 = new MediaContent();

    static {

        mediaContent1.setMediaContentid(maxId - 11);
        mediaContent1
                .setFileurl("ftp://wacos:wacos@10.50.13.102:21//opt/wacos/smpnas/2017/07/27/11.ts");
        mediaContent1.setStatus(MediaContentStatusType.ACTIVE);

        mediaContent2.setMediaContentid(maxId - 12);
        mediaContent2
                .setFileurl("ftp://wacos:wacos@10.50.13.102:21//opt/wacos/smpnas/2017/07/27/12.ts");
        mediaContent2.setStatus(MediaContentStatusType.WAIT_MD5VERIFY);

        mediaContent3.setMediaContentid(maxId - 13);
        mediaContent3
                .setFileurl("ftp://wacos:wacos@10.50.13.102:21//opt/wacos/smpnas/2017/07/27/13.ts");
        mediaContent3.setStatus(MediaContentStatusType.MD5VERIFY_FAILURE);

        mediaContent4.setMediaContentid(maxId - 14);
        mediaContent4
                .setFileurl("ftp://wacos:wacos@10.50.13.102:21//opt/wacos/smpnas/2017/07/27/14.ts");
        mediaContent4.setStatus(MediaContentStatusType.ACTIVE);

        mediaContent5.setMediaContentid(maxId - 15);
        mediaContent5
                .setFileurl("ftp://wacos:wacos@10.50.13.102:21//opt/wacos/smpnas/2017/07/27/15.ts");
        mediaContent5.setStatus(MediaContentStatusType.WAIT_MD5VERIFY);

        mediaContent6.setMediaContentid(maxId - 16);
        mediaContent6
                .setFileurl("ftp://wacos:wacos@10.50.13.102:21//opt/wacos/smpnas/2017/07/27/16.ts");
        mediaContent6.setStatus(MediaContentStatusType.MD5VERIFY_FAILURE);
    }

}
