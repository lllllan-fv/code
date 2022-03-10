package cn.lllllan.demo.filter;

import cn.lllllan.demo.domain.User;
import cn.lllllan.demo.service.impl.UserServiceImpl;
import cn.lllllan.demo.utils.JsonData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thymeleaf.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//@WebFilter(urlPatterns = "/api/v1/pri/*", filterName = "LoginFilter")
public class LoginFilter implements Filter {

    private static ObjectMapper objectMapper = new ObjectMapper();

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
            JsonData jsonData = JsonData.buildError(-3, "未登录");
            String jsonStr = objectMapper.writeValueAsString(jsonData);
            renderJson(httpServletResponse, jsonStr);
        } else {
            User user = UserServiceImpl.sessionMap.get(token);
            if (user != null) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                JsonData jsonData = JsonData.buildError(-2, "登录失败，token无效");
                String jsonStr = objectMapper.writeValueAsString(jsonData);
                renderJson(httpServletResponse, jsonStr);
            }
        }
    }

    private void renderJson(HttpServletResponse response, String json) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        System.out.println("LoginFilter destroy");
    }
}
