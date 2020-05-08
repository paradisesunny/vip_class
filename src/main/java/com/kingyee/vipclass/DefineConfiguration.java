// ======================================
// Project Name:meddb-starter
// Package Name:cn.meddb.core.starter
// File Name:DefineConfiguration.java
// Create Date:2019年10月16日  16:28
// ======================================
package com.kingyee.vipclass;

import com.kingyee.vipclass.common.security.AdminInterceptor;
import com.kingyee.vipclass.common.security.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * mvc配置
 *
 * @author 李旭光
 * @version 2019年10月16日  16:28
 */
@Configuration
public class DefineConfiguration implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;
    private final AdminInterceptor adminInterceptor;

    public DefineConfiguration(LogInterceptor logInterceptor, AdminInterceptor adminInterceptor) {
        this.logInterceptor = logInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/", "/**");
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin", "/admin/**")
                .excludePathPatterns("/admin/login", "/admin/logout");
    }

}