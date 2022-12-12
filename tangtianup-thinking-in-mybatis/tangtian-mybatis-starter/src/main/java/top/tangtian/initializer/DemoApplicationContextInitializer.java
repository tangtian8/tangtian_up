package top.tangtian.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author tangtian
 * @description
 * @date 2022/12/12 8:01
 */
public class DemoApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        System.out.println(" DemoApplicationContextInitializer 初始化成功 ");
    }
}
