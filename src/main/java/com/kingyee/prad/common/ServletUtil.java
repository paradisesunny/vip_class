// ======================================
// Project Name:gwicc
// Package Name:com.kingyee.isa.common
// File Name:ServletUtil.java
// Create Date:2018年03月26日  10:08
// ======================================
package com.kingyee.prad.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

/**
 * Servlet工具
 *
 * @author 李旭光
 * @version 2018年03月26日  10:08
 */
public class ServletUtil {

    private static final int BUFFER_SIZE = 1024;

    private static final String AJAX_PARAM = "__ajax__";

    private static final String AJAX_REQUEST_HEADER_KEY = "X-Requested-With";

    private static final String AJAX_REQUEST_HEADER_VALUE = "XMLHttpRequest";

    /**
     * 判断一个请求是否是一个异步请求
     */
    public static boolean isAjax(ServletRequest request) {
        boolean flag;
        if (request instanceof HttpServletRequest) {
            flag = AJAX_REQUEST_HEADER_VALUE.equals(((HttpServletRequest) request)
                    .getHeader(AJAX_REQUEST_HEADER_KEY));
            if (flag) {
                return true;
            }
        }
        flag = request.getAttribute(AJAX_PARAM) != null && "true"
                .equals(request.getAttribute(AJAX_PARAM).toString());
        return flag || request.getParameter(AJAX_PARAM) != null && "true".equals(request.getParameter(AJAX_PARAM));
    }

    /**
     * 为一个http请求返回json数据
     */
    public static void writeMsg(ServletResponse response, String errMsg) throws IOException {
        response.setContentType("text/plain;");
        response.setCharacterEncoding("UTF-8");
        if (response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).setHeader("Content-Disposition", "inline");
        }

        JsonNode json = JacksonMapper.newErrorInstance(errMsg);
        PrintWriter writer = response.getWriter();
        StringReader reader = null;
        try {
            reader = new StringReader(json.toString());
            char[] buffer = new char[BUFFER_SIZE];
            int charRead;
            while ((charRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, charRead);
            }
        } finally {
            if (reader != null)
                reader.close();
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    /**
     * 获取一个http请求的ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) { // "***.***.***.***".length() = 15
            if (ip.contains(",")) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}