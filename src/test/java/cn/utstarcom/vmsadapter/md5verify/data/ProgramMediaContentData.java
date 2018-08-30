/**
 * created on 2017年11月30日 下午2:49:49
 */
package cn.utstarcom.vmsadapter.md5verify.data;

import cn.utstarcom.vmsadapter.md5verify.pojo.ProgramMediaContent;

/**
 * @author UTSC0167
 * @date 2017年11月30日
 *
 */
public class ProgramMediaContentData {

    private ProgramMediaContentData() {

    }

    private static final int maxId = ProgramData.maxId;

    public static final ProgramMediaContent programMediaContent1 = new ProgramMediaContent();
    public static final ProgramMediaContent programMediaContent2 = new ProgramMediaContent();
    public static final ProgramMediaContent programMediaContent3 = new ProgramMediaContent();
    public static final ProgramMediaContent programMediaContent4 = new ProgramMediaContent();
    public static final ProgramMediaContent programMediaContent5 = new ProgramMediaContent();
    public static final ProgramMediaContent programMediaContent6 = new ProgramMediaContent();

    static {

        programMediaContent1.setProgrammediaContentId(maxId - 111);
        programMediaContent1.setObjtype('1');
        programMediaContent1.setObjid(ProgramData.program2.getProgramid());
        programMediaContent1.setMediaContentid(MediaContentData.mediaContent1.getMediaContentid());

        programMediaContent2.setProgrammediaContentId(maxId - 112);
        programMediaContent2.setObjtype('1');
        programMediaContent2.setObjid(ProgramData.program2.getProgramid());
        programMediaContent2.setMediaContentid(MediaContentData.mediaContent2.getMediaContentid());

        programMediaContent3.setProgrammediaContentId(maxId - 113);
        programMediaContent3.setObjtype('1');
        programMediaContent3.setObjid(ProgramData.program2.getProgramid());
        programMediaContent3.setMediaContentid(MediaContentData.mediaContent3.getMediaContentid());

        programMediaContent4.setProgrammediaContentId(maxId - 114);
        programMediaContent4.setObjtype('1');
        programMediaContent4.setObjid(ProgramData.program5.getProgramid());
        programMediaContent4.setMediaContentid(MediaContentData.mediaContent4.getMediaContentid());

        programMediaContent5.setProgrammediaContentId(maxId - 115);
        programMediaContent5.setObjtype('1');
        programMediaContent5.setObjid(ProgramData.program5.getProgramid());
        programMediaContent5.setMediaContentid(MediaContentData.mediaContent5.getMediaContentid());

        programMediaContent6.setProgrammediaContentId(maxId - 116);
        programMediaContent6.setObjtype('1');
        programMediaContent6.setObjid(ProgramData.program5.getProgramid());
        programMediaContent6.setMediaContentid(MediaContentData.mediaContent6.getMediaContentid());
    }
}
