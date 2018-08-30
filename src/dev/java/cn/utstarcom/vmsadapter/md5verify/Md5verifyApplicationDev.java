package cn.utstarcom.vmsadapter.md5verify;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.ctrip.framework.apollo.util.ConfigConstants;

import cn.utstarcom.vmsadapter.md5verify.common.LogToConsole;

/**
 * Hello world!
 *
 */
@EnableApolloConfig
@EnableTransactionManagement
@SpringBootApplication
public class Md5verifyApplicationDev {

    private static final Logger logger = LoggerFactory.getLogger(Md5verifyApplicationDev.class);
    
    static {
        System.getProperties().put("uapollo.env", "dev");
        System.getProperties().put("app.id", "md5verify");
        System.getProperties().put("uapollo.server-port", "10.48.113.11:8080");
        System.getProperties().put("uapollo.client.ip", "10.48.114.12");
        String userDir = System.getProperty("user.dir");
        System.getProperties().put("spring.property.path", userDir + "/src/main");
        System.getProperties().put("spring.config.location", "file:${spring.property.path}/config/md5verify.yml");
        System.getProperties().put("logging.level.root", "info");
    }
    
    public static void main(String[] args) {

        logger.info("the md5verify begin to start ....");
        String userDir = System.getProperty("user.dir");
        SpringApplication.run(Md5verifyApplicationDev.class, args);
        logger.info("the md5verify start completed. the user dir: {}", userDir);
        LogToConsole.out("Md5verifyApplication",
                "the md5verify start completed. the user dir: " + userDir);
    }
}
