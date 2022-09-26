package asia.dreamdropsakura.reggie.filter;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.entity.Employee;
import asia.dreamdropsakura.reggie.util.LocalThreadVariablePoolUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 登录请求判断过滤器
 * 1. 自定义一个过滤器 LoginCheckFilter 并实现 Filter 接口, 在doFilter方法中完成登录校验的逻
 * 辑。
 *
 * @author 童话的爱
 * @since 2022-9-17
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    /**
     * 路径匹配器，支持通配符
     */
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * backend 登录检查，仅检查请求是否已登录
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        ObjectMapper objectMapper = new ObjectMapper();

        // 1. 获取本次请求的URI
        //log.info("[LoginCheckFilter]RequestURI: " + httpServletRequest.getRequestURI());

        // 2. 定义不需要拦截的请求地址
        // 注意前端点菜界面的登录请求也需要添加上
        String[] paths = new String[]{
                "/employee/login",
                "/employee/logout",
                "/user/login",
                "/user/sendMsg",
                "/backend/**",
                "/front/**",
        };

        // 3. 进行路径匹配，判断本次请求是否需要处理
        for (String path : paths) {
            boolean match = PATH_MATCHER.match(path, httpServletRequest.getRequestURI());
            // 判断请求路径是否符合paths 中所定义的路径
            if (match) {
                log.info("[doFilter]Matched path " + path + " at URI " + httpServletRequest.getRequestURI() + "");
                // 如果不需要处理
                // 注意如果提前在方法体开头对ServletResponse 获取Writer 则会有可能抛出： filterChain.doFilter() throw exception:
                //  java.lang.IllegalStateException: getWriter() has already been called for this response
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        // 判断请求是否携带有指定的session 对象
        if (httpServletRequest.getSession().getAttribute("employee") != null) {
            // 如果有登录后台

            // 设置线程变量. 这行代码不可以写在if 外面，只有在确认请求确实携带有employee session 时才继续
            long employee = (Long) httpServletRequest.getSession().getAttribute("employee");
            log.info("[doFilter]有后台用户登录：" + employee);
            LocalThreadVariablePoolUtil.setCurrentThreadUserid(employee);

            //System.out.println("用户已经登录" + httpServletRequest.getSession().getAttribute("employee"));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        } else if (httpServletRequest.getSession().getAttribute("user") != null) {
            // 如果有登录前端

            String user = String.valueOf(httpServletRequest.getSession().getAttribute("user"));
            log.info("[doFilter]有前台用户登录：" + user);
            LocalThreadVariablePoolUtil.setCurrentThreadUserid(Long.parseLong(user));

            filterChain.doFilter(servletRequest, servletResponse);
        }

        // 如果没有登录
        // 有时会报错 java.lang.IllegalStateException: getOutputStream() has already been called for this response
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(Result.error("NOTLOGIN")));
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
