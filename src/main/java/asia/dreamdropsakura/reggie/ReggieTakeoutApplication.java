package asia.dreamdropsakura.reggie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SpringBoot 应用程序启动类
 *
 * @author 童话的爱
 * @since 2022-9-17
 */
@SpringBootApplication
@MapperScan("asia.dreamdropsakura.reggie.mapper")
@ComponentScan
// 为应用程序启用javax 中的WebFilter 功能
@ServletComponentScan
// 开启事务管理支持
@EnableTransactionManagement
// 开启缓存注解功能
@EnableCaching
public class ReggieTakeoutApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReggieTakeoutApplication.class, args);
	}

}
