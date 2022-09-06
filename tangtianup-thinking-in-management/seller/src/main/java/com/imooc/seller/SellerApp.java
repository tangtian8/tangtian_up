package com.imooc.seller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 销售端启动类
 */
@SpringBootApplication
@EnableCaching
@EntityScan("com.imooc.entity")
@EnableScheduling
public class SellerApp {
    public static void main(String[] args) {
        SpringApplication.run(SellerApp.class);
    }
}
