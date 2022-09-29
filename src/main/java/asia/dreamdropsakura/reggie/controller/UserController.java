package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.entity.User;
import asia.dreamdropsakura.reggie.service.UserService;
import asia.dreamdropsakura.reggie.util.LocalThreadVariablePoolUtil;
import asia.dreamdropsakura.reggie.util.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取验证码功能
     *
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/sendMsg")
    public Result<String> getVerifyCode(HttpServletRequest httpServletRequest, @RequestBody User user) {
        if (user != null) {
            HttpSession session = httpServletRequest.getSession();

            String verifyCode = ValidateCodeUtils.generateValidateCode(4).toString();
            session.setAttribute(user.getPhone(), verifyCode);
            log.info("Current thread user " + LocalThreadVariablePoolUtil.getCurrentThreadUserid() + " - " + user.getPhone() + "'s verify code is {}", verifyCode);

            return Result.success(verifyCode);
        }
        return Result.error("电话号码为空。");
    }

    /**
     * 登录验证功能
     *
     * @param mapParams 其中包含验证码与手机号码，用于验证该手机是否已经注册及验证其验证码是否正确
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    // 由于其中包含的值就两个，但又有一个字段不在User 表中，故使用Map 来接收参数
    public Result<String> login(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> mapParams) {
        // todo 代码需要改进，三个if 了都

        if (mapParams.size() >= 2) {
            // todo 复习类型转换知识点
            //  对于登录和注册你还不熟悉
            HttpSession session = httpServletRequest.getSession();
            //获取手机号
            String phone = mapParams.get("phone").toString();
            //获取验证码
            String code = mapParams.get("code").toString();
            //从Session中获取保存的验证码
            Object inSessionVerifyCode = session.getAttribute(phone);

            // 判断验证码是否正确
            if (inSessionVerifyCode != null && inSessionVerifyCode.equals(code)) {
                // 如果验证码正确, 开始根据手机号判断有没有这个用户
                User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));

                if (one == null) {
                    // 如果获取的User 为空，直接注册该手机号为用户
                    one = new User();
                    one.setPhone(phone);
                    // 0 禁用，1 正常
                    one.setStatus(1);
                    boolean save = userService.save(one);
                }
                // 如果获取的User 对象不为空, 则登录成功, 将用户记录表的主键id存入
                session.setAttribute("user", one.getId());
                return Result.success("登录成功");
            } else {
                return Result.error("验证码不正确");
            }
        }

        return Result.error("手机号或验证码为空");
    }

    /**
     * 用户登出
     *
     * @return
     */
    @GetMapping
    public Result<String> logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("user");
        return Result.success("已登出");
    }
}
