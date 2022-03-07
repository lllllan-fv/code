package cn.lllllan.demo.filter;

import cn.lllllan.demo.domain.User;
import cn.lllllan.demo.service.impl.UserServiceImpl;
import org.thymeleaf.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/api/v1/pri/*", filterName = "LoginFilter")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("LoginFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("LoginFilter doFilter");

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String token = httpServletRequest.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = httpServletRequest.getParameter("token");
        }

        if (StringUtils.isEmpty(token)) {
        } else {
            User user = UserServiceImpl.sessionMap.get(token);
            if (user != null) {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("LoginFilter destroy");
    }
}
