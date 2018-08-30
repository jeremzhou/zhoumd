/**
 * created on 2017年11月14日 下午4:47:56
 */
package cn.utstarcom.vmsadapter.md5verify.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.utstarcom.vmsadapter.md5verify.bo.NeHeartbeatRequest;
import cn.utstarcom.vmsadapter.md5verify.bo.NeHeartbeatResponse;

/**
 * @author UTSC0167
 * @date 2017年11月14日
 *
 */
@RestController
public class DefaultController {

    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @PostMapping("/NeHeartBeat")
    public NeHeartbeatResponse neHeartbeat(@RequestBody NeHeartbeatRequest heartbeatRequest,
            HttpServletRequest request) {
        logger.debug("neHeartbeat begin for clientIp: {} clientPort: {} requestBody: {}",
                request.getRemoteAddr(), request.getRemotePort(), heartbeatRequest);

        NeHeartbeatResponse heartbeatResponse = new NeHeartbeatResponse();
        heartbeatResponse.setAppType(999);
        heartbeatResponse.setAppVersion("1.0.0");
        heartbeatResponse.setServiceStatus(0);
        logger.debug("neHeartbeat end for clientIp: {} clientPort: {} heartbeatResponse: {}",
                request.getRemoteAddr(), request.getRemotePort(), heartbeatResponse);

        return heartbeatResponse;
    }
}
