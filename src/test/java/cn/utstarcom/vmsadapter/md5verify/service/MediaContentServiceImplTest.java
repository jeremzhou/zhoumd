/**
 * created on 2017年12月1日 上午9:47:03
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

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
import cn.utstarcom.vmsadapter.md5verify.common.MediaContentStatusType;
import cn.utstarcom.vmsadapter.md5verify.data.MediaContentData;
import cn.utstarcom.vmsadapter.md5verify.data.ProgramData;
import cn.utstarcom.vmsadapter.md5verify.data.ProgramMediaContentData;
import cn.utstarcom.vmsadapter.md5verify.dto.MediaContentStatus;

/**
 * @author UTSC0167
 * @date 2017年12月1日
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Md5verifyApplication.class)
@TestPropertySource(locations="file:src/test/java/test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MediaContentServiceImplTest {

    @Autowired
    private MediaContentService mediaContentService;

    @Autowired
    private ProgramMediaContentService programMediaContentService;

    @Test
    @Rollback
    public void testQueryPendingVerifyList() {

        try {
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

            List<MediaContentStatus> mediaContentStatusList = this.mediaContentService
                    .queryPendingVerifyList(ProgramData.program2.getProgramid());
            assertThat(mediaContentStatusList.size(), equalTo(2));

            mediaContentStatusList = this.mediaContentService
                    .queryPendingVerifyList(ProgramData.program5.getProgramid());
            assertThat(mediaContentStatusList.size(), equalTo(2));
        } finally {

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

    @Test
    public void testUpdateStatus() {

        try {
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

            assertThat(this.mediaContentService.updateStatus(
                    MediaContentData.mediaContent2.getMediaContentid(),
                    MediaContentStatusType.MD5VERIFY_FAILURE), equalTo(1));
            MediaContentStatus mediaContentStatus = this.mediaContentService
                    .queryByPkTest2(MediaContentData.mediaContent2.getMediaContentid());
            assertThat(mediaContentStatus.getStatus(),
                    equalTo(MediaContentStatusType.MD5VERIFY_FAILURE));

            assertThat(this.mediaContentService.updateStatus(
                    MediaContentData.mediaContent5.getMediaContentid(),
                    MediaContentStatusType.ACTIVE), equalTo(1));
            mediaContentStatus = this.mediaContentService
                    .queryByPkTest2(MediaContentData.mediaContent5.getMediaContentid());
            assertThat(mediaContentStatus.getStatus(), equalTo(MediaContentStatusType.ACTIVE));

        } finally {

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

        }
    }

}
