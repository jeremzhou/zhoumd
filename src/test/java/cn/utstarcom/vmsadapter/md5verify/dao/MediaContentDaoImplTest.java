/**
 * created on 2017年11月22日 下午2:17:25
 */
package cn.utstarcom.vmsadapter.md5verify.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication;
import cn.utstarcom.vmsadapter.md5verify.common.MediaContentStatusType;
import cn.utstarcom.vmsadapter.md5verify.data.MediaContentData;
import cn.utstarcom.vmsadapter.md5verify.data.ProgramData;
import cn.utstarcom.vmsadapter.md5verify.data.ProgramMediaContentData;
import cn.utstarcom.vmsadapter.md5verify.dto.MediaContentStatus;

/**
 * @author UTSC0167
 * @date 2017年11月22日
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Md5verifyApplication.class)
@TestPropertySource(locations="file:src/test/java/test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
@Rollback
public class MediaContentDaoImplTest {

    @Autowired
    private ProgramMediaContentDao programMediaContentDao;

    @Autowired
    private MediaContentDao mediaContentDao;

    @Test
    public void testQueryPendingVerifyList() {

        try {
            assertThat(this.mediaContentDao.saveByNativeTest(MediaContentData.mediaContent1),
                    equalTo(1));
            assertThat(this.mediaContentDao.saveByNativeTest(MediaContentData.mediaContent2),
                    equalTo(1));
            assertThat(this.mediaContentDao.saveByNativeTest(MediaContentData.mediaContent3),
                    equalTo(1));
            assertThat(this.mediaContentDao.saveByNativeTest(MediaContentData.mediaContent4),
                    equalTo(1));
            assertThat(this.mediaContentDao.saveByNativeTest(MediaContentData.mediaContent5),
                    equalTo(1));
            assertThat(this.mediaContentDao.saveByNativeTest(MediaContentData.mediaContent6),
                    equalTo(1));

            assertThat(this.programMediaContentDao
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent1), equalTo(1));
            assertThat(this.programMediaContentDao
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent2), equalTo(1));
            assertThat(this.programMediaContentDao
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent3), equalTo(1));
            assertThat(this.programMediaContentDao
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent4), equalTo(1));
            assertThat(this.programMediaContentDao
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent5), equalTo(1));
            assertThat(this.programMediaContentDao
                    .saveByNativeTest(ProgramMediaContentData.programMediaContent6), equalTo(1));

            List<MediaContentStatus> mediaContentStatusList = this.mediaContentDao
                    .queryPendingVerifyList(ProgramData.program2.getProgramid());
            assertThat(mediaContentStatusList.size(), equalTo(2));

            mediaContentStatusList = this.mediaContentDao
                    .queryPendingVerifyList(ProgramData.program5.getProgramid());
            assertThat(mediaContentStatusList.size(), equalTo(2));
        } finally {

            assertThat(this.mediaContentDao.deleteByPkTest(
                    MediaContentData.mediaContent1.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentDao.deleteByPkTest(
                    MediaContentData.mediaContent2.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentDao.deleteByPkTest(
                    MediaContentData.mediaContent3.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentDao.deleteByPkTest(
                    MediaContentData.mediaContent4.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentDao.deleteByPkTest(
                    MediaContentData.mediaContent5.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentDao.deleteByPkTest(
                    MediaContentData.mediaContent6.getMediaContentid()), equalTo(1));

            assertThat(this.programMediaContentDao.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent1.getProgrammediaContentId()),
                    equalTo(1));
            assertThat(this.programMediaContentDao.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent2.getProgrammediaContentId()),
                    equalTo(1));
            assertThat(this.programMediaContentDao.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent3.getProgrammediaContentId()),
                    equalTo(1));
            assertThat(this.programMediaContentDao.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent4.getProgrammediaContentId()),
                    equalTo(1));
            assertThat(this.programMediaContentDao.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent5.getProgrammediaContentId()),
                    equalTo(1));
            assertThat(this.programMediaContentDao.deleteByPkTest(
                    ProgramMediaContentData.programMediaContent6.getProgrammediaContentId()),
                    equalTo(1));
        }
    }

    @Test
    public void testUpdateStatus() {

        try {
            assertThat(this.mediaContentDao.saveByNativeTest2(MediaContentData.mediaContent1),
                    equalTo(1));
            assertThat(this.mediaContentDao.saveByNativeTest2(MediaContentData.mediaContent2),
                    equalTo(1));
            assertThat(this.mediaContentDao.saveByNativeTest2(MediaContentData.mediaContent3),
                    equalTo(1));
            assertThat(this.mediaContentDao.saveByNativeTest2(MediaContentData.mediaContent4),
                    equalTo(1));
            assertThat(this.mediaContentDao.saveByNativeTest2(MediaContentData.mediaContent5),
                    equalTo(1));
            assertThat(this.mediaContentDao.saveByNativeTest2(MediaContentData.mediaContent6),
                    equalTo(1));

            assertThat(this.mediaContentDao.updateStatus(
                    MediaContentData.mediaContent2.getMediaContentid(),
                    MediaContentStatusType.MD5VERIFY_FAILURE), equalTo(1));
            MediaContentStatus mediaContentStatus = this.mediaContentDao
                    .queryByPkTest2(MediaContentData.mediaContent2.getMediaContentid());
            assertThat(mediaContentStatus.getStatus(),
                    equalTo(MediaContentStatusType.MD5VERIFY_FAILURE));

            assertThat(this.mediaContentDao.updateStatus(
                    MediaContentData.mediaContent5.getMediaContentid(),
                    MediaContentStatusType.ACTIVE), equalTo(1));
            mediaContentStatus = this.mediaContentDao
                    .queryByPkTest2(MediaContentData.mediaContent5.getMediaContentid());
            assertThat(mediaContentStatus.getStatus(), equalTo(MediaContentStatusType.ACTIVE));

        } finally {

            assertThat(this.mediaContentDao.deleteByPkTest2(
                    MediaContentData.mediaContent1.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentDao.deleteByPkTest2(
                    MediaContentData.mediaContent2.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentDao.deleteByPkTest2(
                    MediaContentData.mediaContent3.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentDao.deleteByPkTest2(
                    MediaContentData.mediaContent4.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentDao.deleteByPkTest2(
                    MediaContentData.mediaContent5.getMediaContentid()), equalTo(1));
            assertThat(this.mediaContentDao.deleteByPkTest2(
                    MediaContentData.mediaContent6.getMediaContentid()), equalTo(1));

        }
    }

}
