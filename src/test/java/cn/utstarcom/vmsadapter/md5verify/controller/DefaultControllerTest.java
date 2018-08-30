/**
 * created on 2017年11月14日 下午4:54:47
 */
package cn.utstarcom.vmsadapter.md5verify.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import cn.utstarcom.vmsadapter.md5verify.Md5verifyApplication;

/**
 * @author UTSC0167
 * @date 2017年11月14日
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Md5verifyApplication.class)
@TestPropertySource(locations="file:src/test/java/test.properties")
@SpringBootTest
@AutoConfigureMockMvc
public class DefaultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testNeHeartbeat() throws Exception {
        
        TimeUnit.SECONDS.sleep(5);
        
        this.mockMvc
                .perform(post("/NeHeartBeat").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"NeHeartBeat\"}"))
                .andExpect(status().isOk()).andExpect(content()
                        .string("{\"appType\":999,\"appVersion\":\"1.0.0\",\"serviceStatus\":0}"));
    }

}
