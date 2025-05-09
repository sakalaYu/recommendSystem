package org.example.config;

import org.example.util.LoginInterceptor;
import org.example.util.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    StringRedisTemplate stringRedisTemplate;
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 登录拦截器
//        registry.addInterceptor(new LoginInterceptor())
//                .excludePathPatterns(
//                        "/user/login",
//                        "/file/**",
//                        "/user/**",
//                        "/blogs/**"
//                ).order(1);
//        // token刷新的拦截器
//        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**")
//                .excludePathPatterns(
//                "/file/**",
//                "/blogs/**"
//        ).order(0);
//    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // token刷新的拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
                .addPathPatterns("/**")
                .excludePathPatterns(
//                        "/file/**"
                ).order(0);

        // 登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/login",
                        "/file/**",
                        "/user/**"
                ).order(1);
    }
}
