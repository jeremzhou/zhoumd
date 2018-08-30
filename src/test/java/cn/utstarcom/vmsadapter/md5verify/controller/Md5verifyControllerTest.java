/**
 * created on 2017年11月23日 上午9:25:13
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
 * @date 2017年11月23日
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Md5verifyApplication.class)
@TestPropertySource(locations="file:src/test/java/test.properties")
@SpringBootTest
@AutoConfigureMockMvc
public class Md5verifyControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMd5verify() throws Exception {

        TimeUnit.SECONDS.sleep(5);
        
        this.mockMvc.perform(post("/md5verify").contentType(MediaType.APPLICATION_JSON).content(
                "{\"request\":\"0\",\"file\":\"src/test/java/test.properties\"}"))
                .andExpect(status().isOk()).andExpect(content().string(
                        "{\"response\":\"0\",\"file\":\"src/test/java/test.properties\",\"md5\":\"78944d335b9ccc4d6d2b37ece8faf565\",\"success\":\"success\"}"));
    }

}
