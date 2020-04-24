package com.kingyee.prad.controller.wechat;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.common.Const;
import com.kingyee.prad.common.NewsCategoryConstant;
import com.kingyee.prad.entity.PradNews;
import com.kingyee.prad.entity.PradNewsCategory;
import com.kingyee.prad.service.IPradNewsCategoryService;
import com.kingyee.prad.service.IPradNewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Optional;

/**
 * @author nmy
 * @date 2020/3/10
 * @desc 规范化课程Controller
 */
@Controller
@RequestMapping(value = "/wechat/newsClass")
public class NewsClassController {
    private static final Logger logger = LoggerFactory.getLogger(NewsClassController.class);
    @Autowired
    private IPradNewsCategoryService newsCategoryService;
    @Autowired
    private IPradNewsService newsService;
    /**
     * list
     *
     * @return
     */
    @RequestMapping("/list")
    public String list(ModelMap mm) {
        QueryWrapper<PradNewsCategory> categoryQueryWrapper = new QueryWrapper<>();
        try {
            categoryQueryWrapper.lambda()
                    .eq(PradNewsCategory::getNcPid,0)
                    .eq(PradNewsCategory::getNcName,NewsCategoryConstant.map.get(NewsCategoryConstant.NEWS_CLASS));
            PradNewsCategory category = newsCategoryService.getOne(categoryQueryWrapper);
            mm.addAttribute("categoryId", category.getNcId());
            mm.addAttribute("title", category.getNcName());
            mm.addAttribute("date", new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取规范化课程列表时出错";
            logger.error(errMsg + e);
            return "error/error";
        }
        return "wechat/resources/newsList";
    }

    @ResponseBody
    @RequestMapping(value = "listNewsAjax")
    public JsonNode listNewsAjax(Integer rowsPerPage, Integer page, Long categoryId) {
        rowsPerPage = Optional.ofNullable(rowsPerPage).orElse(Const.ROWSPERPAGE_EIGHT);
        page = Optional.ofNullable(page).orElse(1);
        QueryWrapper<PradNews> queryWrapper = new QueryWrapper<>();
        IPage<PradNews> pageInfo = new Page<>(page, rowsPerPage);
        try {
            queryWrapper.lambda()
                    .eq(categoryId != null, PradNews::getNeNcId, categoryId)
                    .eq(PradNews::getNeIsValid, 1)
                    .orderByDesc(PradNews::getNeDate);
            pageInfo = newsService.page(pageInfo, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取医学资源列表时出错";
            logger.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
        return JacksonMapper.newCountInstance(pageInfo);
    }
}
