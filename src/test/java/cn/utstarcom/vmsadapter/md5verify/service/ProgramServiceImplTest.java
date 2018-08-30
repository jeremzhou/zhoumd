/**
 * created on 2017年11月23日 下午5:14:31
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication;
import cn.utstarcom.vmsadapter.md5verify.common.Md5VerifyStatusType;
import cn.utstarcom.vmsadapter.md5verify.common.MediaContentStatusType;
import cn.utstarcom.vmsadapter.md5verify.common.ProgramStatusType;
import cn.utstarcom.vmsadapter.md5verify.data.MediaContentData;
import cn.utstarcom.vmsadapter.md5verify.data.ProgramData;
import cn.utstarcom.vmsadapter.md5verify.data.ProgramMediaContentData;
import cn.utstarcom.vmsadapter.md5verify.dto.ProgramStatus;

/**
 * @author UTSC0167
 * @date 2017年11月23日
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Md5verifyApplication.class)
@TestPropertySource(locations="file:src/test/java/test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProgramServiceImplTest {

    @Autowired
    private ProgramService programService;

    @Autowired
    private MediaContentService mediaContentService;

    @Autowired
    private ProgramMediaContentService programMediaContentService;

    @Test
    @Rollback
    public void testQueryPendingVerifyList() {

        List<ProgramStatus> programStatusList = this.programService.queryPendingVerifyList(5, 10);
        int beforCount = programStatusList.size();

        try {
            assertThat(this.programService.saveByNativeTest(ProgramData.program1), equalTo(1));
            assertThat(this.programService.saveByNativeTest(ProgramData.program2), equalTo(1));
            assertThat(this.programService.saveByNativeTest(ProgramData.program3), equalTo(1));
            assertThat(this.programService.saveByNativeTest(ProgramData.program4), equalTo(1));
            assertThat(this.programService.saveByNativeTest(ProgramData.program5), equalTo(1));
            assertThat(this.programService.saveByNativeTest(ProgramData.program6), equalTo(1));

            programStatusList = this.programService.queryPendingVerifyList(5, 10);
            int afterCount = programStatusList.size();

            assertThat(afterCount - beforCount, equalTo(2));
        } finally {
            this.programService.deleteByPkTest(ProgramData.program1.getProgramid());
            this.programService.deleteByPkTest(ProgramData.program2.getProgramid());
            this.programService.deleteByPkTest(ProgramData.program3.getProgramid());
            this.programService.deleteByPkTest(ProgramData.program4.getProgramid());
            this.programService.deleteByPkTest(ProgramData.program5.getProgramid());
            this.programService.deleteByPkTest(ProgramData.program6.getProgramid());
        }
    }

    @Test
    @Rollback
    public void testUpdateStatus() {
        try {
            assertThat(this.programService.saveByNativeTest(ProgramData.program2), equalTo(1));

            ProgramStatus programStatus = new ProgramStatus(ProgramData.program2.getProgramid(),
                    ProgramStatusType.AUDIT_SUCCESS, ProgramData.program2.getMd5chkcnt(),
                    new Date());
            assertThat(this.programService.updateStatus(Md5VerifyStatusType.CONNECT_FAILED, '0',
                    programStatus), equalTo(1));
            ProgramStatus programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program2.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramStatusType.MD5VERIFY_FAILURE));
            assertThat(programStatus1.getMd5chkcnt(), equalTo(ProgramData.program2.getMd5chkcnt()));

            assertThat(this.programService.updateStatus(Md5VerifyStatusType.CONNECT_FAILED, '1',
                    programStatus), equalTo(1));
            programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program2.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramStatusType.MD5VERIFY_FAILURE));
            assertThat(programStatus1.getMd5chkcnt(), equalTo(ProgramData.program2.getMd5chkcnt()));

            assertThat(this.programService.updateStatus(Md5VerifyStatusType.VERIFY_FAILED, '0',
                    programStatus), equalTo(1));
            programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program2.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramStatusType.MD5VERIFY_FAILURE));
            assertThat(programStatus1.getMd5chkcnt(),
                    equalTo(ProgramData.program2.getMd5chkcnt() + 1));

            assertThat(this.programService.updateStatus(Md5VerifyStatusType.VERIFY_FAILED, '1',
                    programStatus), equalTo(1));
            programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program2.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramStatusType.MD5VERIFY_FAILURE));
            assertThat(programStatus1.getMd5chkcnt(),
                    equalTo(ProgramData.program2.getMd5chkcnt() + 2));

            assertThat(this.programService.updateStatus(Md5VerifyStatusType.VERIFY_SUCCESS, '0',
                    programStatus), equalTo(1));
            programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program2.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramStatusType.AUDIT_SUCCESS));
            assertThat(programStatus1.getMd5chkcnt(),
                    equalTo(ProgramData.program2.getMd5chkcnt() + 2));

            assertThat(this.programService.updateStatus(Md5VerifyStatusType.VERIFY_SUCCESS, '1',
                    programStatus), equalTo(1));
            programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program2.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramStatusType.INCOMPLETE));
            assertThat(programStatus1.getMd5chkcnt(),
                    equalTo(ProgramData.program2.getMd5chkcnt() + 2));

        } finally {
            this.programService.deleteByPkTest(ProgramData.program2.getProgramid());
        }
    }

    @Test
    @Rollback
    public void testUpdateStatus2() {

        try {
            assertThat(this.programService.saveByNativeTest(ProgramData.program2), equalTo(1));
            assertThat(this.programService.saveByNativeTest(ProgramData.program5), equalTo(1));

            assertThat(this.mediaContentService.saveByNativeTest(MediaContentData.mediaContent1),
                    equalTo(1));
            assertThat(this.mediaContentService.saveByNativeTest(MediaContentData.mediaContent2),
                    equalTo(1));
            assertThat(this.mediaContentService.saveByNativeTest(MediaContentData.mediaContent3),
                    equalTo(1));
            assertThat(this.mediaContentService.saveByNativeTest(MediaContentData.mediaContent4),
                    equalTo(1));
            assertThat(this.mediaContentService.saveByNativeTest(MediaContentData.mediaContent5),
                    equalTo(1));
            assertThat(this.mediaContentService.saveByNativeTest(MediaContentData.mediaContent6),
                    equalTo(1));

            assertThat(this.programMediaContentService
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent1), equalTo(1));
            assertThat(this.programMediaContentService
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent2), equalTo(1));
            assertThat(this.programMediaContentService
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent3), equalTo(1));
            assertThat(this.programMediaContentService
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent4), equalTo(1));
            assertThat(this.programMediaContentService
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent5), equalTo(1));
            assertThat(this.programMediaContentService
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent6), equalTo(1));

            ProgramStatus programStatus = new ProgramStatus(ProgramData.program2.getProgramid(),
                    ProgramStatusType.AUDIT_SUCCESS, ProgramData.program2.getMd5chkcnt(),
                    new Date());

            assertThat(this.programService.updateStatus(Md5VerifyStatusType.VERIFY_SUCCESS, '0',
                    programStatus), equalTo(0));
            ProgramStatus programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program2.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramData.program2.getStatus()));
            assertThat(programStatus1.getMd5chkcnt(), equalTo(ProgramData.program2.getMd5chkcnt()));

            assertThat(this.mediaContentService.updateStatus(
                    MediaContentData.mediaContent2.getMediaContentid(),
                    MediaContentStatusType.ACTIVE), equalTo(1));
            assertThat(this.programService.updateStatus(Md5VerifyStatusType.VERIFY_SUCCESS, '0',
                    programStatus), equalTo(0));
            programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program2.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramData.program2.getStatus()));
            assertThat(programStatus1.getMd5chkcnt(), equalTo(ProgramData.program2.getMd5chkcnt()));

            assertThat(this.mediaContentService.updateStatus(
                    MediaContentData.mediaContent3.getMediaContentid(),
                    MediaContentStatusType.ACTIVE), equalTo(1));
            assertThat(this.programService.updateStatus(Md5VerifyStatusType.VERIFY_SUCCESS, '0',
                    programStatus), equalTo(1));
            programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program2.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramStatusType.AUDIT_SUCCESS));
            assertThat(programStatus1.getMd5chkcnt(), equalTo(ProgramData.program2.getMd5chkcnt()));

            programStatus = new ProgramStatus(ProgramData.program5.getProgramid(),
                    ProgramStatusType.AUDIT_SUCCESS, ProgramData.program5.getMd5chkcnt(),
                    new Date());

            assertThat(this.programService.updateStatus(Md5VerifyStatusType.VERIFY_SUCCESS, '1',
                    programStatus), equalTo(0));
            programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program5.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramData.program5.getStatus()));
            assertThat(programStatus1.getMd5chkcnt(), equalTo(ProgramData.program5.getMd5chkcnt()));

            assertThat(this.mediaContentService.updateStatus(
                    MediaContentData.mediaContent5.getMediaContentid(),
                    MediaContentStatusType.ACTIVE), equalTo(1));
            assertThat(this.programService.updateStatus(Md5VerifyStatusType.VERIFY_SUCCESS, '1',
                    programStatus), equalTo(0));
            programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program5.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramData.program5.getStatus()));
            assertThat(programStatus1.getMd5chkcnt(), equalTo(ProgramData.program5.getMd5chkcnt()));

            assertThat(this.mediaContentService.updateStatus(
                    MediaContentData.mediaContent6.getMediaContentid(),
                    MediaContentStatusType.ACTIVE), equalTo(1));
            assertThat(this.programService.updateStatus(Md5VerifyStatusType.VERIFY_SUCCESS, '1',
                    programStatus), equalTo(1));
            programStatus1 = this.programService
                    .queryByPkTest2(ProgramData.program5.getProgramid());
            assertThat(programStatus1.getStatus(), equalTo(ProgramStatusType.INCOMPLETE));
            assertThat(programStatus1.getMd5chkcnt(), equalTo(ProgramData.program5.getMd5chkcnt()));

        } finally {
            this.programService.deleteByPkTest(ProgramData.program2.getProgramid());
            this.programService.deleteByPkTest(ProgramData.program5.getProgramid());

            assertThat(this.mediaContentService.deleteByPkTest(
                    MediaContentData.mediaContent1.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentService.deleteByPkTest(
                    MediaContentData.mediaContent2.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentService.deleteByPkTest(
                    MediaContentData.mediaContent3.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentService.deleteByPkTest(
                    MediaContentData.mediaContent4.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentService.deleteByPkTest(
                    MediaContentData.mediaContent5.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentService.deleteByPkTest(
                    MediaContentData.mediaContent6.getMediaContentid()), equalTo(1));

            assertThat(this.programMediaContentService.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent1.getProgrammediaContentId()),
                    equalTo(1));
            assertThat(this.programMediaContentService.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent2.getProgrammediaContentId()),
                    equalTo(1));
            assertThat(this.programMediaContentService.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent3.getProgrammediaContentId()),
                    equalTo(1));
            assertThat(this.programMediaContentService.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent4.getProgrammediaContentId()),
                    equalTo(1));
            assertThat(this.programMediaContentService.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent5.getProgrammediaContentId()),
                    equalTo(1));
            assertThat(this.programMediaContentService.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent6.getProgrammediaContentId()),
                    equalTo(1));
        }
    }

}
