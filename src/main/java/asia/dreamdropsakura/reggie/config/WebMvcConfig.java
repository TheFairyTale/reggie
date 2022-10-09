package asia.dreamdropsakura.reggie.config;

import asia.dreamdropsakura.reggie.common.JacksonObjectMapper;
import com.alibaba.fastjson.support.spring.messaging.MappingFastJsonMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * SpringMVC 配置类
 * 实现WebMvcConfigurer接口来配置资源映射和消息转换器
 * Spring Boot2.0版本以后推荐使用这种方式来进行web配置，这样不会覆盖掉Spring Boot的一些默认配置.
 *
 * @author 童话的爱
 * @since 2022-9-17
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 添加静态资源映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 添加与Swagger 相关的静态资源映射，以免Swagger 生成的文档无法被访问。
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/static/backend/");
        registry.addResourceHandler("/frontend/**").addResourceLocations("classpath:/static/frontend/");
    }

    /**
     * 扩展消息转换器
     * HttpMessageConverter: 基于HTTP协议请求和响应的数据类型转换
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 转换器正在运作
        log.info("extendMessageConverters are running...");
        // 创建消息转换器
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        // 设置具体的对象映射器，底层使用Jackson 将Java 对象转为json
        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());

        // 将上面的消息转换器对象追加到MVC框架的转换器集合中。
        // 通过设置索引， 让自己的转换器放在最前面，否则默认的jackson 转换器会在前，导致我们设置的转换器不生效
        converters.add(0, mappingJackson2HttpMessageConverter);
    }
}
