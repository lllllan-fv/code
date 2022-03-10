package cn.lllllan.demo.intercepter;

import cn.lllllan.demo.domain.User;
import cn.lllllan.demo.service.impl.UserServiceImpl;
import cn.lllllan.demo.utils.JsonData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class LoginIntercepter implements HandlerInterceptor {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("LoginInterceptor preHandle");

        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }

        if (StringUtils.isEmpty(token)) {
            JsonData jsonData = JsonData.buildError(-3, "未登录");
            String jsonStr = objectMapper.writeValueAsString(jsonData);
            renderJson(response, jsonStr);

            return false;
        } else {
            User user = UserServiceImpl.sessionMap.get(token);
            if (user != null) {
                return true;
            } else {
                JsonData jsonData = JsonData.buildError(-2, "登录失败，token无效");
                String jsonStr = objectMapper.writeValueAsString(jsonData);
                renderJson(response, jsonStr);
                return false;
            }
        }

//        return HandlerInterceptor.super.preHandle(request, response, handler);
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
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("LoginInterceptor postHandle");

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("LoginInterceptor afterCompletion");

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
