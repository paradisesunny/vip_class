// ======================================
// Project Name:meddb-starter
// Package Name:cn.meddb.core.common.log
// File Name:LogLocalContext.java
// Create Date:2019年10月17日  14:32
// ======================================
package com.kingyee.common.log;

import java.util.ArrayList;
import java.util.List;

/**
 * 线程中的日志信息
 *
 * @author 李旭光
 * @version 2019年10月17日  14:32
 */
public final class LogLocalContext {

    private ILog log = null;

    private static ThreadLocal<LogLocalContext> local = new ThreadLocal<>();

    /**
     * 设置日志信息<br>
     * 该方法可以在一次请求的任意一个时刻执行
     *
     * @param log ILog
     */
    protected static void setLog(ILog log) {
        local.get().log = log;
    }


    /**
     * 初始化<br>
     * <span style="color:red;">内部使用</span>
     */
    protected static void init() {
        local.set(new LogLocalContext());
    }

    /**
     * 获取日志信息<br>
     * <span style="color:red;">内部使用</span>
     *
     * @return 一条日志信息
     */
    protected static ILog getLog() {
        return local.get().log;
    }

    /**
     * 清空当前线程中保存的日志信息<br>
     * <span style="color:red;">内部使用</span>
     */
    protected static void remove() {
        local.remove();
    }

    /**
     * 检查当前线程中是否包含日志信息<br>
     * <span style="color:red;">内部使用</span>
     *
     * @return boolean
     */
    protected static boolean hasLog() {
        return local.get().log != null;
    }

}