/**
 * created on 2017年11月10日 下午4:26:44
 */
package cn.utstarcom.vmsadapter.md5verify.http.client;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpResponse;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author UTSC0167
 * @date 2017年11月10日
 *
 */
public class HttpClientTest {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientTest.class);

    public static void main(String[] args) {

        doSync();
        doAsync();
    }

    private static void doSync() {
        for (int i = 0; i < 1; i++) {
            HttpClient httpClient = new HttpClient();
            try {

                httpClient.start();

                ContentResponse response = httpClient.POST("http://127.0.0.1:9798/md5verify")
                        .content(new StringContentProvider("application/json",
                                "{\"request\":\"0\",\"file\":\"/2015/06/01/fcd9dd623e1a4f92a79338ede081681b_h2641300000mp296.ts\"}",
                                StandardCharsets.UTF_8))
                        .timeout(30, TimeUnit.SECONDS).send();
                String content = response.getContentAsString();
                logger.info("content: {}", content);

            } catch (Exception e) {
                logger.info("httpClient run generate exception:", e);
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

    private static void doAsync() {
        for (int i = 0; i < 1; i++) {
            HttpClient httpClient = new HttpClient();
            try {

                httpClient.start();

                httpClient.POST("http://127.0.0.1:979/md5verify").content(new StringContentProvider(
                        "application/json",
                        "{\"request\":\"0\",\"file\":\"/2015/06/01/fcd9dd623e1a4f92a79338ede081681b_h2641300000mp296.ts\"}",
                        StandardCharsets.UTF_8)).onResponseSuccess((iResponse) -> {

                            HttpResponse response = (HttpResponse) iResponse;
                            logger.info("99999999999999 response status: {}", response.getStatus());
                        }).onResponseContent((response, buffer) -> {

                            logger.info("8888888888888888 response name: {} content: {}",
                                    response.getClass().getName(), buffer.limit());
                        }).timeout(30, TimeUnit.SECONDS).send((result) -> {
                            logger.info("66666666666666666666 isSucceeded: {}, exception:",
                                    result.isSucceeded(), result.getFailure());
                        });
                logger.info("777777777777777777777777777777777777777777777");

                logger.info("222222222222222 isFailed: {} isStarting: {} isRunning: {}",
                        httpClient.isFailed(), httpClient.isStarting(), httpClient.isRunning());
                Thread.sleep(80000);
                logger.info("333333333333333 isFailed: {} isStarting: {} isRunning: {}",
                        httpClient.isFailed(), httpClient.isStarting(), httpClient.isRunning());

            } catch (Exception e) {
                logger.info("httpClient run generate exception:", e);
            } finally {
                if (httpClient.isStarted())
                    try {
                        httpClient.stop();
                    } catch (Exception e) {
                        logger.info("httpClient stop generate exception:", e);
                    }
            }

            logger.info("44444444444444444 isFailed: {} isStarting: {} isRunning: {}",
                    httpClient.isFailed(), httpClient.isStarting(), httpClient.isRunning());
        }
    }
}
