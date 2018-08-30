/**
 * created on 2017年11月23日 上午9:14:58
 */
package cn.utstarcom.vmsadapter.md5verify.controller;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.utstarcom.vmsadapter.md5verify.bo.Md5VerifyRequest;
import cn.utstarcom.vmsadapter.md5verify.bo.Md5VerifyResponse;
import cn.utstarcom.vmsadapter.md5verify.service.LocalFileService;

/**
 * @author UTSC0167
 * @date 2017年11月23日
 *
 */
@RestController
public class Md5verifyController {

    private static final Logger logger = LoggerFactory.getLogger(Md5verifyController.class);

    @Value("${vmsadapter.md5verify.request.timeout}")
    private int requestTimeout;

    @Autowired
    LocalFileService localFileService;

    @PostMapping("/md5verify")
    public Md5VerifyResponse md5verify(@RequestBody Md5VerifyRequest md5VerifyRequest) {

        logger.debug("md5verify begin for md5VerifyRequest: {}", md5VerifyRequest);

        String requestFile = md5VerifyRequest.getFile();
        logger.info("md5verify for requestFile: {}", requestFile);
        Md5VerifyResponse md5VerifyResponse = new Md5VerifyResponse();
        md5VerifyResponse.setResponse(md5VerifyRequest.getRequest());
        md5VerifyResponse.setFile(md5VerifyRequest.getFile());

        md5VerifyResponse.setSuccess("success");
        if (requestFile.equals("/test/requestnull")) {
            md5VerifyResponse.setMd5("null");
        } else if (requestFile.equals("/test/requestempty")) {
            md5VerifyResponse.setMd5("");
        } else if (requestFile.equals("/test/requestmd5")
                || requestFile.equals("/test/requesttimeout")) {
            md5VerifyResponse.setMd5("iAmMd5!");
        } else {
            try {
                String md5 = this.localFileService.getMd5(new File(requestFile));
                md5VerifyResponse.setMd5(md5);
            } catch (IOException e) {
                logger.info(
                        "md5verify run localFileService.getMd5 for file: {} generate exception:",
                        requestFile, e);
                md5VerifyResponse.setMd5("null");
                md5VerifyResponse.setSuccess("error");
            }
        }

        try {
            if (requestFile.equals("/test/requesttimeout")) {
                TimeUnit.SECONDS.sleep(this.requestTimeout + 10);
            } else {
                TimeUnit.SECONDS.sleep(3);
            }
        } catch (InterruptedException e) {
            logger.debug("md5verify sleep 60 seconds generate exception: {}", e);
        }
        logger.debug("md5verify end for md5VerifyResponse: {}", md5VerifyResponse);
        return md5VerifyResponse;
    }
}
