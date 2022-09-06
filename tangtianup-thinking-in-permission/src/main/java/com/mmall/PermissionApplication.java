package com.mmall;

import com.mmall.common.HttpInterceptor;
import com.mmall.filter.AclControlFilter;
import com.mmall.filter.LoginFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan
@MapperScan(basePackages = {"com.mmall.dao"})
public class PermissionApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(PermissionApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean httpFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new AclControlFilter());
		registration.addUrlPatterns("/sys/*","/admin/*");
		registration.setOrder(2);
		return registration;
	}

	@Bean
	public FilterRegistrationBean loginFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new LoginFilter());
		registration.addUrlPatterns("/sys/*","/admin/*");
		registration.setOrder(1);
		return registration;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HttpInterceptor()).addPathPatterns("/**");
	}
}
