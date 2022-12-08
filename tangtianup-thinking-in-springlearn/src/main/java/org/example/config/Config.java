package org.example.config;

import org.example.pojo.Animal;
import org.example.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author tangtian
 * @description
 * @date 2022/12/6 8:03
 */
@Configuration
@ComponentScan(basePackages = { " org.example.service" })
public class Config {
//    @Bean
//    public Animal animal(){
//        return new Animal();
//    }
//
//    @Bean
//    public User user(){
//        return new User();
//    }
}
