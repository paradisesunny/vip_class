// ======================================
// Project Name:meddb-starter
// Package Name:cn.meddb.core.common.log
// File Name:LogInterceptor.java
// Create Date:2019年10月17日  14:35
// ======================================
package com.kingyee.common.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器
 *
 * @author 李旭光
 * @version 2019年10月17日  14:35
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ILogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LogLocalContext.init();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.getMethod().getAnnotation(Log.class) == null || !LogLocalContext.hasLog()) {
            return;
        }
        ILog logBean = LogLocalContext.getLog();
        if (!logBean.isLogFlag() || logService == null) {
            return;
        }
        logService.saveLog(logBean);
        LogLocalContext.remove();
    }
}