/**
 * created on 2017年11月14日 下午4:48:51
 */
package cn.utstarcom.vmsadapter.md5verify.common;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author UTSC0167
 * @date 2017年11月14日
 *
 */
@Configuration
public class SpringConfiguration {

    @Bean
    public SessionFactory sessionFactory(
            @Qualifier("entityManagerFactory") EntityManagerFactory emf) {
        return emf.unwrap(SessionFactory.class);
    }
}
