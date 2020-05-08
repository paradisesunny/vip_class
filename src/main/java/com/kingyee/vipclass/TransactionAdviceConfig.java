// ======================================
// Project Name:cdc
// Package Name:cn.meddb.cdc
// File Name:TransactionAdviceConfig.java
// Create Date:2019年05月24日  15:41
// ======================================
package com.kingyee.vipclass;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;

/**
 * 事务整体配置
 *
 * @author fyq
 * @version 2019年05月24日  15:41
 */
@Configuration
public class TransactionAdviceConfig {
    //事务切面
    private static final String AOP_POINTCUT_EXPRESSION = "@within(org.springframework.stereotype.Service)";

    private final PlatformTransactionManager transactionManager;

    public TransactionAdviceConfig(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Bean
    public TransactionInterceptor txAdvice() {
        //事务参数

        //属性 propagation="REQUIRED" rollback-for="Exception"
        RuleBasedTransactionAttribute txAttr_REQUIRED = new RuleBasedTransactionAttribute();
        txAttr_REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txAttr_REQUIRED.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));

        //属性 propagation="SUPPORTS" read-only="true"
        DefaultTransactionAttribute txAttr_SUPPORTS_READONLY = new DefaultTransactionAttribute();
        txAttr_SUPPORTS_READONLY.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        txAttr_SUPPORTS_READONLY.setReadOnly(true);

        //属性 propagation="SUPPORTS" read-only="true"
        DefaultTransactionAttribute txAttr_SUPPORTS = new DefaultTransactionAttribute();
        txAttr_SUPPORTS.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        txAttr_SUPPORTS.setReadOnly(true);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        source.addTransactionalMethod("add*", txAttr_REQUIRED);
        source.addTransactionalMethod("del*", txAttr_REQUIRED);
        source.addTransactionalMethod("remove*", txAttr_REQUIRED);
        source.addTransactionalMethod("update*", txAttr_REQUIRED);
        source.addTransactionalMethod("save*", txAttr_REQUIRED);
        source.addTransactionalMethod("set*", txAttr_REQUIRED);

        source.addTransactionalMethod("get*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("list*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("find*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("search*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("query*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("count*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("is*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("page", txAttr_SUPPORTS_READONLY);

        source.addTransactionalMethod("*", txAttr_SUPPORTS);
        return new TransactionInterceptor(transactionManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}