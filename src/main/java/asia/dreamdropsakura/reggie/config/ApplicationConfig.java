package asia.dreamdropsakura.reggie.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * SpringBoot 应用程序配置类
 *
 * @author 童话的爱
 * @since 2022-9-18
 */
@Slf4j
@Configuration
// 开启Swagger 的自动配置
@EnableSwagger2
@EnableKnife4j
public class ApplicationConfig extends CachingConfigurerSupport {
    /**
     * Mybatis-Plus 分页插件
     * 该分页插件会拦截所有查询方法中带有IPage 对象的，改变底层执行的SQL，加上limit 语句
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 自定义Redis 相关行为或周边行为。如如何处理序列与反序列化等
     *
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        // 默认的Key序列化器为：JdkSerializationRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    /**
     * 配置Swagger / knife4j 具体参数
     *
     * @param environment Spring 框架环境参数
     */
    @Bean
    public Docket createRestApi(Environment environment) {
        // 设置要启用/显示Swagger 文档页面的运行环境. 只有当环境为dev 时才启用Swagger
        Profiles profiles = Profiles.of("dev");
        boolean b = environment.acceptsProfiles(profiles);

        // 文档类型
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("瑞吉外卖")
                        .version("1.0")
                        .description("瑞吉外卖接口文档")
                        .build())
                // 配置是否启用Swagger，如果是false，在浏览器将无法访问
                .enable(b)
                .select()
                // RequestHandlerselectors,配置要扫描接口的方式
                // basePackage:指定要扫描的包
                // any():扫描全部
                // none():不扫描
                // withClassAnnotation:扫描类上的注解，多数是一个注解的反射对象
                // withMethodAnnotation:扫描方法上的注解
                .apis(RequestHandlerSelectors.basePackage("asia.dreamdropsakura.reggie.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
