package cn.lllllan.demo.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 标记这是一个异常处理类
 */
//@RestControllerAdvice
@ControllerAdvice
public class CustomExtHandler {

//    @ExceptionHandler(value = Exception.class)
//    Object handlerException(Exception e, HttpServletRequest request) {
//        return JsonData.buildError(2, "服务端出错");
//    }

    @ExceptionHandler(value = Exception.class)
    Object handlerException(Exception e, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error.html");
        modelAndView.addObject("msg", e.getMessage());
        return modelAndView;
    }
}
