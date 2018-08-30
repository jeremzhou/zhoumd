/**
 * created on 2017年12月1日 上午10:05:19
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication;

/**
 * @author UTSC0167
 * @date 2017年12月1日
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Md5verifyApplication.class)
@TestPropertySource(locations="file:src/test/java/test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LocalFileServiceImplTest {

    private static final File okFile = new File("./src/test/java/test.properties");
    private static final File failedFile = new File("./src/test/java/test2.properties");

    @Autowired
    private LocalFileService localFileService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test() throws IOException {

        assertThat(this.localFileService.getMd5(okFile),
                equalTo("78944d335b9ccc4d6d2b37ece8faf565"));

        expectedException.expect(IOException.class);
        assertThat(this.localFileService.getMd5(failedFile),
                equalTo("78944d335b9ccc4d6d2b37ece8faf565"));
    }

}
