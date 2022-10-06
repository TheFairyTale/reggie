package asia.dreamdropsakura.reggie.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * SpringBoot 应用程序配置类
 *
 * @author 童话的爱
 * @since 2022-9-18
 */
@Configuration
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

        // 将值序列化为json
        // 先试试看不加行不行

        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
