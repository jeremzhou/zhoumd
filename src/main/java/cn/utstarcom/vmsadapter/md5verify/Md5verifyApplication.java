package cn.utstarcom.vmsadapter.md5verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.utstarcom.vmsadapter.md5verify.common.LogToConsole;

/**
 * Hello world!
 *
 */
@EnableTransactionManagement
@SpringBootApplication
public class Md5verifyApplication {

    private static final Logger logger = LoggerFactory.getLogger(Md5verifyApplication.class);

    static {

        System.getProperties().put("spring.config.location",
                "file:${spring.property.path}/config/md5verify.yml");
    }

    public static void main(String[] args) {

        logger.info("the md5verify begin to start ....");
        String userDir = System.getProperty("user.dir");
        SpringApplication.run(Md5verifyApplication.class, args);
        logger.info("the md5verify start completed. the user dir: {}", userDir);
        LogToConsole.out("Md5verifyApplication",
                "the md5verify start completed. the user dir: " + userDir);
    }
}
