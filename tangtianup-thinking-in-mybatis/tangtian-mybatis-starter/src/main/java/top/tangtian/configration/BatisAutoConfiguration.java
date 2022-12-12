package top.tangtian.configration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.tangtian.core.factory.SqlSessionFactoryBuilder;

/**
 * @author tangtian
 * @description
 * @date 2022/12/12 7:51
 */
@Configuration
@EnableConfigurationProperties(value = DataSourceProperties.class)
public class BatisAutoConfiguration {
    private final DataSourceProperties demoProperties;

    public BatisAutoConfiguration(DataSourceProperties demoProperties) {
        this.demoProperties = demoProperties;
    }

    @Bean
    @ConditionalOnMissingBean(SqlSessionFactoryBuilder.class)
    public SqlSessionFactoryBuilder factoryBuilder() {
        return new SqlSessionFactoryBuilder(demoProperties);
    }
}
