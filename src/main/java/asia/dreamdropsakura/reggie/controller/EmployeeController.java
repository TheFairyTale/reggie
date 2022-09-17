package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.entity.Employee;
import asia.dreamdropsakura.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 员工相关控制器类
 *
 * @author 童话的爱
 * @since 2022-9-17
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * 员工登录的处理逻辑如下
     * 1. 将前端页面提交的密码password 进行md5 加密
     * 2-3. 根据页面提交的用户名username 密码password 查询数据库
     * 4. 如果没有查询到则返回登录失败结果
     * 5. 查看员工账号状态，如果为禁用状态，则返回账号已被禁用结果
     * 6. 登录成功，将员工id 存入session 并返回登录成功结果
     *
     * @param request  用于获取session， 将登录成功的用户放入session 中
     * @param employee
     * @return 统一结果返回
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 1
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 2 3
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getPassword, password).eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(lambdaQueryWrapper);
        // 4
        if (one == null) {
            return Result.error("登录失败，用户名或密码错误");
        }
        // 5 0账号被禁用 1账号正常，故此处写如果有status为1的则代表账号是正常的，故下面的if 不生效
        lambdaQueryWrapper.eq(Employee::getStatus, 1);
        one = employeeService.getOne(lambdaQueryWrapper);
        if (one == null) {
            return Result.error("登陆失败，该用户已被禁用。");
        }
        // 6
        System.out.println("让我看看你的pwd: " + one.getPassword());
        HttpSession session = request.getSession();
        //session.setAttribute(one.getUsername(), one.getId());
        session.setAttribute("employee", one.getId());
        return Result.success(one);
    }

    /**
     * 员工登出系统
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        // todo 前端不传值怎么知道要登出哪个用户？
        request.getSession().getAttribute("employee");
        return Result.success("登出成功.");
    }
}
