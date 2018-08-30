/**
 * created on 2017年11月22日 上午9:34:49
 */
package cn.utstarcom.vmsadapter.md5verify;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

/**
 * @author UTSC0167
 * @date 2017年11月22日
 *
 */
public class JavaTest {

    private static final Logger logger = LoggerFactory.getLogger(JavaTest.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        
        Properties properties = yaml2Properties("application.yml");
    }

    public static Properties yaml2Properties(String yamlSource) {
        try {
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new ClassPathResource(yamlSource));
            return yaml.getObject();
        } catch (Exception e) {
            logger.error("yaml2Properties read yaml: {} generate exception:", yamlSource,e);
            return null;
        }
    }
}
