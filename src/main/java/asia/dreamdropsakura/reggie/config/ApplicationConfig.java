package asia.dreamdropsakura.reggie.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringBoot 应用程序配置类
 *
 * @author 童话的爱
 * @since 2022-9-18
 */
@Configuration
public class ApplicationConfig {
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
}
