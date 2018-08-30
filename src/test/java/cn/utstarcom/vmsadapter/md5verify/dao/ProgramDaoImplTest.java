/**
 * created on 2017年11月21日 下午4:11:12
 */
package cn.utstarcom.vmsadapter.md5verify.dao;

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
import org.springframework.transaction.annotation.Transactional;

import cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication;
import cn.utstarcom.vmsadapter.md5verify.common.ProgramStatusType;
import cn.utstarcom.vmsadapter.md5verify.data.ProgramData;
import cn.utstarcom.vmsadapter.md5verify.dto.ProgramStatus;

/**
 * @author UTSC0167
 * @date 2017年11月21日
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Md5verifyApplication.class)
@TestPropertySource(locations="file:src/test/java/test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
@Rollback
public class ProgramDaoImplTest {

    @Autowired
    private ProgramDao programDao;

    @Test
    public void testQueryPendingVerifyList() {

        List<ProgramStatus> programStatusList = this.programDao.queryPendingVerifyList(5, 10);
        int beforCount = programStatusList.size();

        try {
            assertThat(this.programDao.saveByNativeTest(ProgramData.program1), equalTo(1));
            assertThat(this.programDao.saveByNativeTest(ProgramData.program2), equalTo(1));
            assertThat(this.programDao.saveByNativeTest(ProgramData.program3), equalTo(1));
            assertThat(this.programDao.saveByNativeTest(ProgramData.program4), equalTo(1));
            assertThat(this.programDao.saveByNativeTest(ProgramData.program5), equalTo(1));
            assertThat(this.programDao.saveByNativeTest(ProgramData.program6), equalTo(1));

            programStatusList = this.programDao.queryPendingVerifyList(5, 10);
            int afterCount = programStatusList.size();

            assertThat(afterCount - beforCount, equalTo(2));
        } finally {
            this.programDao.deleteByPkTest(ProgramData.program1.getProgramid());
            this.programDao.deleteByPkTest(ProgramData.program2.getProgramid());
            this.programDao.deleteByPkTest(ProgramData.program3.getProgramid());
            this.programDao.deleteByPkTest(ProgramData.program4.getProgramid());
            this.programDao.deleteByPkTest(ProgramData.program5.getProgramid());
            this.programDao.deleteByPkTest(ProgramData.program6.getProgramid());
        }
    }

    @Test
    public void testUpdateStatus() {

        try {
            assertThat(this.programDao.saveByNativeTest2(ProgramData.program2), equalTo(1));

            ProgramStatus programStatus = new ProgramStatus(ProgramData.program2.getProgramid(),
                    ProgramStatusType.AUDIT_SUCCESS, ProgramData.program2.getMd5chkcnt() + 1,
                    new Date());
            assertThat(this.programDao.updateStatus(programStatus), equalTo(1));
            programStatus = this.programDao.queryByPkTest2(ProgramData.program2.getProgramid());
            assertThat(programStatus.getStatus(), equalTo(ProgramStatusType.AUDIT_SUCCESS));
            assertThat(programStatus.getMd5chkcnt(),
                    equalTo(ProgramData.program2.getMd5chkcnt() + 1));

        } finally {
            this.programDao.deleteByPkTest2(ProgramData.program2.getProgramid());
        }
    }

    @Test
    public void testUpdateStatus2() {

        try {
            assertThat(this.programDao.saveByNativeTest2(ProgramData.program5), equalTo(1));

            assertThat(this.programDao.updateStatus(ProgramData.program5.getProgramid(),
                    ProgramStatusType.INCOMPLETE), equalTo(1));
            ProgramStatus programStatus = this.programDao
                    .queryByPkTest2(ProgramData.program5.getProgramid());
            assertThat(programStatus.getStatus(), equalTo(ProgramStatusType.INCOMPLETE));

        } finally {
            this.programDao.deleteByPkTest2(ProgramData.program5.getProgramid());
        }
    }
}
