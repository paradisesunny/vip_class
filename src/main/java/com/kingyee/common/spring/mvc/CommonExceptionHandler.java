package com.kingyee.common.spring.mvc;

import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 * Created by ph on 2017/5/23.
 */
@ControllerAdvice
public class CommonExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        try {
            String url = HttpUtil.getFullUrl(request, false);
            String errMsg = "前台-未登陆用户，在请求["+url+"]时候发生了异常";
            //写入日志
            logger.error(errMsg, ex);

            // 前台错误处理
            String requestType = request.getHeader("X-Requested-With");
            if(requestType != null){
                JsonNode errorInstance = JacksonMapper.newErrorInstance("运行时有错误。");
                // ajax请求
                response.setContentType("text/plain;");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition", "inline");
                PrintWriter writer = response.getWriter();
                try {
                    writer.write(errorInstance.toString());
                } catch (Exception e1) {
                    logger.error("", e1);
                } finally {
                    try {
                        writer.close();
                    } catch (Exception e2) {
                        logger.error("", e2);
                    }
                }
                return new ModelAndView();
            }else{
                //转发
                Map<String, Object> model = new HashMap<>();
                model.put("errMsg", "抱歉，出现错误了，请稍后再试。");
                return new ModelAndView("error/error", model);
            }
        }catch (Exception e){
            logger.error("记录出错日志失败。", e);
        }
        return new ModelAndView("error/error");
    }

}
