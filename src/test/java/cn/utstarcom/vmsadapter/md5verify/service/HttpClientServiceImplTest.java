/**
 * created on 2017年12月1日 上午10:36:22
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.ConnectException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
import cn.utstarcom.vmsadapter.md5verify.bo.Md5VerifyRequest;
import cn.utstarcom.vmsadapter.md5verify.bo.Md5VerifyResponse;
import cn.utstarcom.vmsadapter.md5verify.common.Md5VerifyRequestType;

/**
 * @author UTSC0167
 * @date 2017年12月1日
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Md5verifyApplication.class)
@TestPropertySource(locations="file:src/test/java/test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HttpClientServiceImplTest {

    private static final String requestnull = "/test/requestnull";
    private static final String requestempty = "/test/requestempty";
    private static final String requesttimeout = "/test/requesttimeout";
    private static final String requestok = "src/test/java/test.properties";
    private static final String requesterror = "src/test/java/test2.properties";

    private static final String testVerifyUrl = "http://127.0.0.1:9797/md5verify";
    private static final String errorVerifyUrl = "http://127.0.0.1:65532/md5verify";
    private static final String error2VerifyUrl = "http://2.2.2.2:65532/md5verify";

    @Autowired
    HttpClientService httpClientService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testDoPostNull() throws ConnectException, TimeoutException, Exception {

        Md5VerifyRequest md5VerifyRequest = new Md5VerifyRequest(Md5VerifyRequestType.VERIFY,
                requestnull);
        Md5VerifyResponse md5VerifyResponse = this.httpClientService.doPost(testVerifyUrl,
                md5VerifyRequest);
        assertThat(md5VerifyResponse.getMd5(), equalTo("null"));
        assertThat(md5VerifyResponse.getResponse(), equalTo(Md5VerifyRequestType.VERIFY));
        assertThat(md5VerifyResponse.getSuccess(), equalTo("success"));

        md5VerifyRequest = new Md5VerifyRequest(Md5VerifyRequestType.RETRANSMISSION, requestnull);
        md5VerifyResponse = this.httpClientService.doPost(testVerifyUrl, md5VerifyRequest);
        assertThat(md5VerifyResponse.getMd5(), equalTo("null"));
        assertThat(md5VerifyResponse.getResponse(), equalTo(Md5VerifyRequestType.RETRANSMISSION));
        assertThat(md5VerifyResponse.getSuccess(), equalTo("success"));

    }

    @Test
    public void testDoPostEmpty() throws ConnectException, TimeoutException, Exception {

        Md5VerifyRequest md5VerifyRequest = new Md5VerifyRequest(Md5VerifyRequestType.VERIFY,
                requestempty);
        Md5VerifyResponse md5VerifyResponse = this.httpClientService.doPost(testVerifyUrl,
                md5VerifyRequest);
        assertThat(md5VerifyResponse.getMd5(), equalTo(""));
        assertThat(md5VerifyResponse.getResponse(), equalTo(Md5VerifyRequestType.VERIFY));
        assertThat(md5VerifyResponse.getSuccess(), equalTo("success"));

        md5VerifyRequest = new Md5VerifyRequest(Md5VerifyRequestType.RETRANSMISSION, requestempty);
        md5VerifyResponse = this.httpClientService.doPost(testVerifyUrl, md5VerifyRequest);
        assertThat(md5VerifyResponse.getMd5(), equalTo(""));
        assertThat(md5VerifyResponse.getResponse(), equalTo(Md5VerifyRequestType.RETRANSMISSION));
        assertThat(md5VerifyResponse.getSuccess(), equalTo("success"));

    }

    @Test
    public void testDoPostOk() throws ConnectException, TimeoutException, Exception {

        Md5VerifyRequest md5VerifyRequest = new Md5VerifyRequest(Md5VerifyRequestType.VERIFY,
                requestok);
        Md5VerifyResponse md5VerifyResponse = this.httpClientService.doPost(testVerifyUrl,
                md5VerifyRequest);
        assertThat(md5VerifyResponse.getMd5(), equalTo("78944d335b9ccc4d6d2b37ece8faf565"));
        assertThat(md5VerifyResponse.getResponse(), equalTo(Md5VerifyRequestType.VERIFY));
        assertThat(md5VerifyResponse.getSuccess(), equalTo("success"));

        md5VerifyRequest = new Md5VerifyRequest(Md5VerifyRequestType.RETRANSMISSION, requestok);
        md5VerifyResponse = this.httpClientService.doPost(testVerifyUrl, md5VerifyRequest);
        assertThat(md5VerifyResponse.getMd5(), equalTo("78944d335b9ccc4d6d2b37ece8faf565"));
        assertThat(md5VerifyResponse.getResponse(), equalTo(Md5VerifyRequestType.RETRANSMISSION));
        assertThat(md5VerifyResponse.getSuccess(), equalTo("success"));

    }

    @Test
    public void testDoPostError() throws ConnectException, TimeoutException, Exception {

        Md5VerifyRequest md5VerifyRequest = new Md5VerifyRequest(Md5VerifyRequestType.VERIFY,
                requesterror);
        Md5VerifyResponse md5VerifyResponse = this.httpClientService.doPost(testVerifyUrl,
                md5VerifyRequest);
        assertThat(md5VerifyResponse.getMd5(), equalTo("null"));
        assertThat(md5VerifyResponse.getResponse(), equalTo(Md5VerifyRequestType.VERIFY));
        assertThat(md5VerifyResponse.getSuccess(), equalTo("error"));

        md5VerifyRequest = new Md5VerifyRequest(Md5VerifyRequestType.RETRANSMISSION, requesterror);
        md5VerifyResponse = this.httpClientService.doPost(testVerifyUrl, md5VerifyRequest);
        assertThat(md5VerifyResponse.getMd5(), equalTo("null"));
        assertThat(md5VerifyResponse.getResponse(), equalTo(Md5VerifyRequestType.RETRANSMISSION));
        assertThat(md5VerifyResponse.getSuccess(), equalTo("error"));

    }

    @SuppressWarnings("unused")
    @Test
    public void testDoPostTimeout() throws ConnectException, TimeoutException, Exception {

        Md5VerifyRequest md5VerifyRequest = new Md5VerifyRequest(Md5VerifyRequestType.VERIFY,
                requesttimeout);

        expectedException.expect(TimeoutException.class);
        Md5VerifyResponse md5VerifyResponse = this.httpClientService.doPost(testVerifyUrl,
                md5VerifyRequest);
    }
    
    @SuppressWarnings("unused")
    @Test
    public void testDoPostConnFailed() throws ConnectException, TimeoutException, Exception {

        Md5VerifyRequest md5VerifyRequest = new Md5VerifyRequest(Md5VerifyRequestType.VERIFY,
                requesttimeout);

        expectedException.expect(ExecutionException.class);
        expectedException.expectMessage("java.net.ConnectException: Connection refused: no further information");
        Md5VerifyResponse md5VerifyResponse = this.httpClientService.doPost(errorVerifyUrl,
                md5VerifyRequest);
    }

    @SuppressWarnings("unused")
    @Test
    public void testDoPostException() throws ConnectException, TimeoutException, Exception {

        Md5VerifyRequest md5VerifyRequest = new Md5VerifyRequest(Md5VerifyRequestType.VERIFY,
                requesttimeout);

        expectedException.expect(Exception.class);
        Md5VerifyResponse md5VerifyResponse = this.httpClientService.doPost(error2VerifyUrl,
                md5VerifyRequest);
    }
}
