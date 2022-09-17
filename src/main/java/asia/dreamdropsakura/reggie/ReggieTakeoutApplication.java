package asia.dreamdropsakura.reggie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * SpringBoot 应用程序启动类
 *
 * @author 童话的爱
 * @since 2022-9-17
 */
@SpringBootApplication
@MapperScan("asia.dreamdropsakura.reggie.mapper")
@ComponentScan
// 为应用程序启用Filter 功能
@ServletComponentScan
public class ReggieTakeoutApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReggieTakeoutApplication.class, args);
	}

}
