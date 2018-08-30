/**
 * created on 2017年11月24日 下午1:25:12
 */
package cn.utstarcom.vmsadapter.md5verify.service;

import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.utstarcom.vmsadapter.md5verify.bo.Md5VerifyRequest;
import cn.utstarcom.vmsadapter.md5verify.bo.Md5VerifyResponse;

/**
 * @author UTSC0167
 * @date 2017年11月24日
 *
 */
@Service
public class HttpClientServiceImpl implements HttpClientService {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientServiceImpl.class);

    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    @Value("${vmsadapter.md5verify.request.timeout}")
    private int requestTimeout;

    @Override
    public Md5VerifyResponse doPost(String requestUrl, Md5VerifyRequest md5VerifyRequest)
            throws ConnectException, TimeoutException, Exception {

        logger.info("doPost start for requestUri: {} md5VerifyRequest: {}", requestUrl,
                md5VerifyRequest);
        HttpClient httpClient = new HttpClient();
        try {
            httpClient.start();
            ContentResponse response = httpClient.POST(requestUrl)
                    .content(new StringContentProvider(MediaType.APPLICATION_JSON_VALUE,
                            gson.toJson(md5VerifyRequest), StandardCharsets.UTF_8))
                    .timeout(this.requestTimeout, TimeUnit.SECONDS).send();
            int status = response.getStatus();
            if (status != HttpStatus.OK_200) {
                logger.info(
                        "doPost for requestUri: {} md5VerifyRequest: {} return status: {} isn't OK_200. thow exception.",
                        requestUrl, md5VerifyRequest, status);
                throw new Exception("doPost return status: " + status + " isn't OK_200!");
            }
            String content = response.getContentAsString();
            logger.info("doPost response conent: {}", content);
            return gson.fromJson(content, Md5VerifyResponse.class);
        } finally {
            if (httpClient.isStarted())
                try {
                    httpClient.stop();
                } catch (Exception e) {
                    logger.info("httpClient stop generate exception:", e);
                }
        }
    }

}
