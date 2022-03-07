package cn.lllllan.demo.handler;

import cn.lllllan.demo.utils.JsonData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 标记这是一个异常处理类
 */
@RestControllerAdvice
public class CustomExtHandler {

    @ExceptionHandler(value = Exception.class)
    Object handlerException(Exception e, HttpServletRequest request) {
        return JsonData.buildError(2, "服务端出错");
    }
}
