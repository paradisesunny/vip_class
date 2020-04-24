package com.kingyee.prad;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.jfinal.template.ext.spring.JFinalViewResolver;
import com.jfinal.template.source.ClassPathSourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * com.kingyee.common.spring.mvc有全局异常处理,默认没有扫描到这里的Controller,所以需要指定ComponentScan位置
 * 或者把ApplicationErrorController移到StarterApplication同级starter目录下,并删除下面两个ComponentScan注解
 */
@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
@ComponentScan("com.kingyee.common.spring")
@ComponentScan("com.kingyee.common.baidu")
@ComponentScan("com.kingyee.prad")
@MapperScan("com.kingyee.prad.mapper")
@EnableAsync
public class StarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarterApplication.class, args);
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 模版引擎
     */
    @Bean(name = "jfinalViewResolver")
    public JFinalViewResolver getJFinalViewResolver() {
        JFinalViewResolver jfr = new JFinalViewResolver();
        // setDevMode 配置放在最前面
        jfr.setDevMode(true);

        // 使用 ClassPathSourceFactory 从 class path 与 jar 包中加载模板文件
        jfr.setSourceFactory(new ClassPathSourceFactory());

        // 在使用 ClassPathSourceFactory 时要使用 setBaseTemplatePath
        // 代替 jfr.setPrefix("/view/")
        JFinalViewResolver.engine.setBaseTemplatePath("/templates/");
        jfr.setSuffix(".html");
        jfr.setContentType("text/html;charset=UTF-8");
        jfr.setOrder(0);
        jfr.addSharedFunction("/_layout.html");
        // jfr.addSharedFunction("/_paginate.html");
        return jfr;
    }

}
