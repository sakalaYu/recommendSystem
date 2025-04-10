package org.example.config;

/*web 项目配置类*/

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    /**
     * 跨域问题处理
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/icon/**").addResourceLocations("C:/Users/Administrator/Desktop/毕设/icon/");
        registry.addResourceHandler("/file/icon/**").addResourceLocations("C:/Users/Administrator/Desktop/毕设/fileIcon/");
//        registry.addResourceHandler("/icon/**").addResourceLocations("E:/image/icon");
    }
}
