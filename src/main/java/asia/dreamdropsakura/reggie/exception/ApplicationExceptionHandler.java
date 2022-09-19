package asia.dreamdropsakura.reggie.exception;

import asia.dreamdropsakura.reggie.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * SpringBoot 应用程序全局异常控制器
 * 1. 在项目中自定义一个全局异常处理器，在异常处理器上加上注解 @ControllerAdvice,可以通过属
 * 性annotations指定拦截哪一类的Controller方法。 并在异常处理器的方法上加上注解
 * @ExceptionHandler 来指定拦截的是那一类型的异常。
 *
 * @author 童话的爱
 * @since 2022-9-18
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class ApplicationExceptionHandler {
/*
    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return Result.error("系统错误" + e.getMessage());
    }*/

    @ExceptionHandler(SQLException.class)
    public Result sqlExceptionHandler(SQLException e) {
        e.printStackTrace();
        String message = e.getMessage();
        if (message.contains("Duplicate entry")) {
            String[] s = message.split(" ");
            String s1 = s[2] + "已存在";
            return Result.error(s1);
        }
        // exception.getMessage()
        return Result.error("未知错误");
    }

}
