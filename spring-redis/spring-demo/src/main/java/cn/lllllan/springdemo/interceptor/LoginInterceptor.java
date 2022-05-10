package cn.lllllan.springdemo.interceptor;

import cn.lllllan.springdemo.utils.JWT;
import cn.lllllan.springdemo.utils.JsonData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 进入到 Controller 之前的方法
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String token = request.getHeader("token");
            if (token == null) {
                token = request.getParameter("token");
            }

            if (StringUtils.isNotBlank(token)) {
                Claims claims = JWT.checkJWT(token);

                if (claims == null) {
                    sendJsonMessage(response, JsonData.error("未登录"));
                    return false;
                }

                Integer id = (Integer) claims.get("id");
                String name = (String) claims.get("name");

                request.setAttribute("user_id", id);
                request.setAttribute("user_name", name);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendJsonMessage(response, JsonData.error("未登录"));
        return false;
    }

    /**
     * 响应 json 给前端
     *
     * @param response
     * @param object
     */
    public static void sendJsonMessage(HttpServletResponse response, Object object) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();

            response.setContentType("application/json; charset=utf-8");

            PrintWriter writer = response.getWriter();

            writer.print(objectMapper.writeValueAsString(object));

            response.flushBuffer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
