package asia.dreamdropsakura.reggie.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * SpringMVC 配置类
 *
 * @author 童话的爱
 * @since 2022-9-17
 */
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 添加静态资源映射
     *
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/static/backend/");
        registry.addResourceHandler("/frontend/**").addResourceLocations("classpath:/static/frontend/");
    }
}
