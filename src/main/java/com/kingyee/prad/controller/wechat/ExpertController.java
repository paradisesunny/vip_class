package com.kingyee.prad.controller.wechat;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.common.Const;
import com.kingyee.prad.entity.PradExpert;
import com.kingyee.prad.entity.PradNews;
import com.kingyee.prad.service.IPradExpertService;
import com.kingyee.prad.service.IPradNewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nmy
 * @date 2020/3/10
 * @desc 名家专栏
 */
@Controller
@RequestMapping(value = "/wechat/expert")
public class ExpertController {
    private static final Logger logger = LoggerFactory.getLogger(ExpertController.class);
    @Autowired
    private IPradExpertService expertService;
    @Autowired
    private IPradNewsService newsService;

    /**
     * 专家文章列表
     */
    @RequestMapping(value = {"getNewsListByExpert"})
    public String getNewsListByExpert(ModelMap mm, Long expertId) {
        PradExpert expert = expertService.getById(expertId);
        mm.addAttribute("expert", expert);
        return "wechat/front/expertNewsList";
    }

    /**
     * 专家文章列表AJAX
     *
     * @param rowsPerPage
     * @param page
     */
    @ResponseBody
    @RequestMapping(value = "/listExpertNewsAjax", method = RequestMethod.GET)
    public JsonNode listNewsAjax(Integer rowsPerPage, Integer page, Long expertId) {
        if (page == null) {
            page = 1;
        }
        if (rowsPerPage == null) {
            rowsPerPage = Const.ROWSPERPAGE_EIGHT;
        }
        QueryWrapper<PradNews> queryWrapper = new QueryWrapper<>();
        IPage<PradNews> pageInfo = new Page<>(page, rowsPerPage);
        try {
            queryWrapper.lambda().eq(expertId != null, PradNews::getNeExpertId, expertId)
                    .eq(PradNews::getNeIsValid, 1)
                    .orderByDesc(PradNews::getNeDate);
            pageInfo = newsService.page(pageInfo, queryWrapper);
            if(pageInfo != null && pageInfo.getSize() >0){
                for(PradNews news:pageInfo.getRecords()){
                    if(news != null && news.getNeDate() != null){
                        news.setNePublishDateStr(TimeUtil.longToString(news.getNeDate(),TimeUtil.FORMAT_DATE));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = "ajax获取专家文章列表时出错。";
            logger.error(errmsg, e);
            return JacksonMapper.newErrorInstance(errmsg);
        }
        return JacksonMapper.newDataInstance(pageInfo);
    }
}
