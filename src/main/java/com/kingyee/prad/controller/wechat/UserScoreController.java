package com.kingyee.prad.controller.wechat;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.common.Const;
import com.kingyee.prad.common.ScoreRuleConst;
import com.kingyee.prad.common.security.UserUtil;
import com.kingyee.prad.entity.PradScoreRule;
import com.kingyee.prad.entity.PradScoreTransaction;
import com.kingyee.prad.mapper.PradScoreTransactionMapper;
import com.kingyee.prad.model.UserScoreModel;
import com.kingyee.prad.service.IPradScoreRuleService;
import com.kingyee.prad.service.IPradScoreTransactionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;


/**
 * @author nmy
 * @date 2020/3/10
 * @desc 个人中心-成长值      视频播放了30s之后算加5成长值；
 *
 */
@Controller
@RequestMapping("/wechat/userCenter")
public class UserScoreController {
    private final static Logger logger = LoggerFactory.getLogger(UserScoreController.class);

    @Autowired
    private IPradScoreTransactionService scoreTransactionService;
    @Resource
    private PradScoreTransactionMapper transactionMapper;
    @Autowired
    private IPradScoreRuleService ruleService;


    /**
     * 成长值明细
     * 以月份为单位，按时间倒序展示，可单击收起/展开整个月份的明细记录。
     * 查询月份，并只查当前月的积分交易明细
     */
    @RequestMapping("scoreList")
    public String scoreList() {
		return "wechat/user/scoreList";
    }

    /**
     * 获取该月的积分交易明细
     * @return
     */
    @ResponseBody
    @RequestMapping("listScoreAjax")
    public JsonNode listScoreAjax(String month) {
        List<UserScoreModel> userScoreModelList = new ArrayList<>();
        try {
            if(StringUtils.isEmpty(month)){
                List<String> monthList = transactionMapper.getScoreByMonth(UserUtil.getUserId());
                Optional.ofNullable(monthList).orElse(new ArrayList<>()).forEach(mon -> {
                    UserScoreModel userScoreModel = new UserScoreModel();
                    userScoreModel.setMonth(mon);
                    //获取这个月的第一天和最后一天
                    Long firstTimeLong = TimeUtil.getMonthBegin(mon);
                    Long lastTimeLong = TimeUtil.getMonthEnd(mon);
                    //获取这个月的积分交易明细
                    QueryWrapper<PradScoreTransaction> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(PradScoreTransaction::getStUserId,UserUtil.getUserId())
                            .between(PradScoreTransaction::getStCreateTime,firstTimeLong,lastTimeLong)
                            .orderByDesc(PradScoreTransaction::getStCreateTime);
                    List<PradScoreTransaction> scoreList = scoreTransactionService.list(queryWrapper);
                    userScoreModel.setScoreTransactionList(scoreList);
                    userScoreModelList.add(userScoreModel);
                });
            }
            return JacksonMapper.newDataInstance(userScoreModelList);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取该月的积分交易明细出错，userId="+UserUtil.getUserId()+"  month="+month;
            logger.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }


    /**
     * 成长值任务
     * 每天的任务
     */
    @RequestMapping("scoreTaskList")
    public String scoreTaskList(ModelMap mm) {
        try {
            //今天的零点 和24:59:59
            Long startTime = TimeUtil.getTodayTime(0);
            Long endTime = TimeUtil.getTodayTime(1);
            QueryWrapper<PradScoreTransaction> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PradScoreTransaction::getStUserId,UserUtil.getUserId())
                    .between(PradScoreTransaction::getStCreateTime,startTime,endTime)
                    .groupBy(PradScoreTransaction::getStRuleId);
            List<PradScoreTransaction> transactionList = scoreTransactionService.list(queryWrapper);
            //获取规则列表
            List<PradScoreRule> ruleList = ruleService.list();
            Optional.ofNullable(ruleList).orElse(new ArrayList<>()).forEach(rule -> {
                Optional.ofNullable(transactionList).orElse(new ArrayList<>()).forEach(transaction -> {
                    if(rule.getSrId().equals(transaction.getStRuleId())){
                        rule.setIsFinish(true);
                    }
                });
            });
            mm.addAttribute("taskList",ruleList);
            mm.addAttribute("transactionList",transactionList);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取成长值任务时出错";
            logger.error(errMsg, e);
            return "error/error";
        }
        return "wechat/user/scoreTaskList";
    }

    /**
     * 成长值任务路由
     */
    @RequestMapping("goToTask")
    public String goToTask(String type) {
        if(type.equals(ScoreRuleConst.NEWS_READ) || type.equals(ScoreRuleConst.NEWS_LIKE)
            || type.equals(ScoreRuleConst.NEWS_COMMENT) || type.equals(ScoreRuleConst.NEWS_BOOK)){
            return "redirect:/wechat/front/category?categoryId=1";
        }
        if(type.equals(ScoreRuleConst.WATCH_VIDEO)){
            return "redirect:/wechat/resources/index";
        }
        if(type.equals(ScoreRuleConst.NEWS_SHARE)){
            return "redirect:/wechat/front/index";
        }
        return "redirect:scoreTaskList";
    }

    public static void main(String[] args) {
//        System.out.println(TimeUtil.stringToLong("2020-03-06 12:33:33",TimeUtil.FORMAT_DATETIME_FULL));
    }
}
