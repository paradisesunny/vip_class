package com.kingyee.prad.controller.wechat;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.common.ScoreRuleConst;
import com.kingyee.prad.common.security.UserUtil;
import com.kingyee.prad.entity.PradScoreRule;
import com.kingyee.prad.entity.PradScoreTransaction;
import com.kingyee.prad.entity.PradUser;
import com.kingyee.prad.entity.PradVisitLog;
import com.kingyee.prad.service.IPradScoreRuleService;
import com.kingyee.prad.service.IPradScoreTransactionService;
import com.kingyee.prad.service.IPradUserService;
import com.kingyee.prad.service.IPradVisitLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author nmy
 * @date 2020/3/10
 * @desc 积分操作Controller
 */
@Controller
@RequestMapping(value = "/wechat/score")
public class NewsScoreController {
    private static final Logger logger = LoggerFactory.getLogger(NewsScoreController.class);
    @Autowired
    private IPradScoreRuleService ruleService;
    @Autowired
    private IPradScoreTransactionService transactionService;
    @Autowired
    private IPradVisitLogService visitLogService;
    @Autowired
    private IPradUserService userService;


    private PradScoreRule rule;
    private Long startTime;
    private Long endTime;


    /**
     * 查询今天是否加过该积分项
     *
     */
    @ResponseBody
    @RequestMapping(value = "/updateScoreByRule", method = RequestMethod.GET)
    public JsonNode updateScoreByRule(String ruleType,Long newsId) {
        if(!UserUtil.hasLogin()){
            return JacksonMapper.newSuccessInstance();
        }
        if(StringUtils.isEmpty(ruleType)){
            return JacksonMapper.newErrorInstance("加积分失败，规则为空。");
        }
        try {
            QueryWrapper<PradScoreRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PradScoreRule::getSrType,ruleType);
            rule = ruleService.getOne(queryWrapper);
            if(rule != null){
                //今天的零点 和24:59:59
                startTime = TimeUtil.getTodayTime(0);
                endTime = TimeUtil.getTodayTime(1);
                if(ruleType.equals(ScoreRuleConst.NEWS_READ)){
                    //每天阅读3篇文章
                    QueryWrapper<PradVisitLog> visitLogQueryWrapper = new QueryWrapper<>();
                    visitLogQueryWrapper.lambda().eq(PradVisitLog::getVlUserId,UserUtil.getUserId())
                                        .between(PradVisitLog::getVlStartTime,startTime,endTime)
                                        .groupBy(PradVisitLog::getVlNewsId);
                    List<PradVisitLog> visitLogList = visitLogService.list(visitLogQueryWrapper);
                    if(visitLogList != null && visitLogList.size() == 3){
                        addScore();
                    }
                }else if(ruleType.equals(ScoreRuleConst.NEWS_SHARE)){
                    //分享文章
                    if(newsId != null){
                        addScore();
                    }
                }else{
                    addScore();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = "加分失败。userId="+UserUtil.getUserId()+" 规则Id="+rule.getSrId()+" 规则名称="+rule.getSrName();
            logger.error(errmsg , e);
            return JacksonMapper.newErrorInstance(errmsg);
        }
        return JacksonMapper.newSuccessInstance();
    }


    private void addScore(){
        QueryWrapper<PradScoreTransaction> transactionQueryWrapper = new QueryWrapper<>();
        transactionQueryWrapper.lambda().eq(PradScoreTransaction::getStRuleId,rule.getSrId())
                .eq(PradScoreTransaction::getStUserId,UserUtil.getUserId())
                .between(PradScoreTransaction::getStCreateTime,startTime,endTime);
        PradScoreTransaction transaction = transactionService.getOne(transactionQueryWrapper);
        if(transaction == null || transaction.getStId() == null){
            //当天没加过该积分项
            transaction = new PradScoreTransaction();
            transaction.setStUserId(UserUtil.getUserId());
            transaction.setStUserName(UserUtil.getUserName());
            transaction.setStRuleId(rule.getSrId());
            transaction.setStScore(rule.getSrScore());
            transaction.setStType(rule.getSrName());
            transaction.setStCreateTime(System.currentTimeMillis());
            transactionService.save(transaction);
            //修改用户总积分
            PradUser user = userService.getById(UserUtil.getUserId());
            if(user != null){
                if(user.getPuTotalScore() == null){
                    user.setPuTotalScore(rule.getSrScore());
                }else{
                    user.setPuTotalScore(user.getPuTotalScore() + rule.getSrScore());
                }
                userService.updateById(user);
            }
        }
    }

    public PradScoreRule getRule() {
        return rule;
    }

    public void setRule(PradScoreRule rule) {
        this.rule = rule;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
