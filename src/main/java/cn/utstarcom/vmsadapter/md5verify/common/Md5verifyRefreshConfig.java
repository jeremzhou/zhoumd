/**
 * created on 2018年1月9日 下午4:09:14
 */
package cn.utstarcom.vmsadapter.md5verify.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;

/**
 * @author UTSC0167
 * @date 2018年1月9日
 *
 */
@Component
public class Md5verifyRefreshConfig {

    private static final Logger logger = LoggerFactory.getLogger(Md5verifyRefreshConfig.class);

    @Autowired
    private RefreshScope refreshScope;

    @Autowired
    Md5verifyConfig md5verifyConfig;

    @ApolloConfigChangeListener
    private void onChange(ConfigChangeEvent changeEvent) {
        
        logger.info("onChange changeEvent nameSpace: {}",changeEvent.getNamespace());
        if (changeEvent.isChanged("vmsadapter.md5verify.task.count")
                || changeEvent.isChanged("vmsadapter.md5verify.request.threads")) {
            logger.info("before md5verifyConfig refresh {}", md5verifyConfig.toString());
            refreshScope.refresh("md5verifyConfig");
            logger.info("after md5verifyConfig refresh {}", md5verifyConfig.toString());
        }
    }
}
