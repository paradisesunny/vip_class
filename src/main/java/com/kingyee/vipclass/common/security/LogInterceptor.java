// ======================================
// Project Name:meddb-starter
// Package Name:cn.meddb.core.starter.common.security
// File Name:LogInterceptor.java
// Create Date:2019年10月16日  16:30
// ======================================
package com.kingyee.vipclass.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器
 *
 * @author 李旭光
 * @version 2019年10月16日  16:30
 */
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        logger.info(request.getRequestURI());
        return super.preHandle(request, response, handler);
    }

}