package cn.lllllan.springdemo.exception;

import cn.lllllan.springdemo.utils.JsonData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class VideoExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(VideoExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonData handle(Exception e) {

        logger.error("[ 系统异常 ]{}", e);

        if (e instanceof LanException) {
            LanException lanException = (LanException) e;
            return JsonData.error(lanException.getCode(), lanException.getMsg());
        } else {
            return JsonData.error("全局异常，未知错误");
        }
    }
}
