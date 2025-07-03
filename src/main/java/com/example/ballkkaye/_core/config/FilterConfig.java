package com.example.ballkkaye._core.config;

import com.example.ballkkaye._core.filter.AuthorizationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter() {
        System.out.println("authorizationFilter 동작");
        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthorizationFilter());
        registrationBean.addUrlPatterns("/s/*"); // 모든 요청에 적용
        registrationBean.setOrder(1); // 필터 순서 설정
        return registrationBean;
    }
}
