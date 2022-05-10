package cn.lllllan.springdemo.config;

import cn.lllllan.springdemo.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor())
                // 拦截全部
                .addPathPatterns("/api/v1/pri/*/*/**")
                // 不拦截
                .excludePathPatterns("/api/v1/pri/user/register", "/api/v1/pri/user/login");

        WebMvcConfigurer.super.addInterceptors(registry);

    }
}
