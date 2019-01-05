package com.cosmos.config;

import com.cosmos.auth.intercepter.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * The type Intercepter config.
 *
 * @author ambujmehra
 */
@Configuration
public class IntercepterConfig extends WebMvcConfigurerAdapter {

    /**
     * Authentication interceptor authentication interceptor.
     *
     * @return the authentication interceptor
     */
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor()).excludePathPatterns("/payment/paytm/callback");
    }


}
