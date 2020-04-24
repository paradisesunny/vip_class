// ======================================
// Project Name:meddb-starter
// Package Name:cn.meddb.core.starter
// File Name:DefineConfiguration.java
// Create Date:2019年10月16日  16:28
// ======================================
package com.kingyee.prad;

import com.kingyee.prad.common.security.AdminInterceptor;
import com.kingyee.prad.common.security.LogInterceptor;
import com.kingyee.prad.common.security.WechatLoginInterceptor;
import com.kingyee.prad.wx.mp.WeixinInterceptor;
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
    private final WeixinInterceptor weixinInterceptor;
    private final WechatLoginInterceptor wechatLoginInterceptor;

    public DefineConfiguration(LogInterceptor logInterceptor, AdminInterceptor adminInterceptor,
                               WeixinInterceptor weixinInterceptor,
                               WechatLoginInterceptor wechatLoginInterceptor) {
        this.logInterceptor = logInterceptor;
        this.adminInterceptor = adminInterceptor;
        this.weixinInterceptor = weixinInterceptor;
        this.wechatLoginInterceptor = wechatLoginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/", "/**");
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin", "/admin/**")
                .excludePathPatterns("/admin/login", "/admin/logout");
        registry.addInterceptor(weixinInterceptor)
                .addPathPatterns("/wechat", "/wechat/**");
        registry.addInterceptor(wechatLoginInterceptor)
                .addPathPatterns("/wechat/news/addOrRemoveBookRecord", "/wechat/news/addLikeRecord")
                .addPathPatterns("/wechat/news/getCommentById", "/wechat/news/saveComment")
                .addPathPatterns("/wechat/tools/guide")
                .addPathPatterns("/wechat/resources/detail")
				.addPathPatterns("/wechat/userCenter/**")
//                .addPathPatterns("/wechat/score/**")
                .excludePathPatterns("/wechat/userLogin/**");
    }

}