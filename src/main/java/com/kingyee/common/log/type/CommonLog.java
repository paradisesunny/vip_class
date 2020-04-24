// ======================================
// Project Name:meddb-starter
// Package Name:cn.meddb.core.common.log.type
// File Name:CommonLog.java
// Create Date:2019年10月17日  14:26
// ======================================
package com.kingyee.common.log.type;

import com.kingyee.common.log.ILog;

/**
 * 通用日志类
 *
 * @author 李旭光
 * @version 2019年10月17日  14:26
 */
public class CommonLog implements ILog {

    private String module;
    private String name;
    private String detail;
    private boolean logFlag = true;

    public static CommonLog newInstance() {
        return new CommonLog();
    }

    public CommonLog module(String module) {
        this.module = module;
        return this;
    }

    public CommonLog name(String name) {
        this.name = name;
        return this;
    }

    public CommonLog detail(String detail) {
        this.detail = detail;
        return this;
    }

    @Override
    public String getModule() {
        return module;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isLogFlag() {
        return logFlag;
    }

    public void setLogFlag(boolean logFlag) {
        this.logFlag = logFlag;
    }
}