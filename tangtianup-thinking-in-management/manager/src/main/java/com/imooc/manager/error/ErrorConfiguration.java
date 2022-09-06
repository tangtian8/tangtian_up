package com.imooc.manager.error;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 错误处理相关配置
 */
//@Configuration
public class ErrorConfiguration {
    @Bean
    public MyErrorController basicErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
                                                  ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
        return new MyErrorController(errorAttributes, serverProperties.getError(),
                errorViewResolversProvider.getIfAvailable());
    }
}
