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
import com.kingyee.prad.mapper.PradNewsMapper;
import com.kingyee.prad.model.NewsModel;
import com.kingyee.prad.service.IPradNewsCategoryService;
import com.kingyee.prad.service.IPradNewsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.Time;
import java.util.List;

/**
 * @author nmy
 * @date 2020/3/10
 * @desc 我的专区-理论前沿
 */
@Controller
@RequestMapping(value = "/wechat/front")
public class TheoryFrontController {
    private static final Logger logger = LoggerFactory.getLogger(TheoryFrontController.class);
    @Autowired
    private IPradNewsService newsService;
    @Autowired
    private IPradNewsCategoryService newsCategoryService;
    @Resource
    private PradNewsMapper newsMapper;


    /**
     * 我的专区-首页-展示10篇热门文章，规则为全平台的内容中，三天内浏览量最高的文章
     */
    @RequestMapping("index")
    public String index(ModelMap mm) {
        Integer current = 1;
        Integer size =  Const.ADMIN_ROWSPERPAGE_LESS;
        QueryWrapper<PradNews> queryWrapper = new QueryWrapper<>();
        IPage<PradNews> pageInfo = new Page<>(current, size);
        try {
            Long nowDate = System.currentTimeMillis();
            Long lastThreeDate = TimeUtil.thisDateLastDate(3);
            queryWrapper.lambda().eq(PradNews::getNeIsValid,1)
                    .between(PradNews::getNeDate,lastThreeDate,nowDate)
                    .orderByDesc( PradNews::getNeHits);
            pageInfo = newsService.page(pageInfo, queryWrapper);
            if(pageInfo.getSize() == 0 || pageInfo.getRecords().size() ==0){
                //最近浏览量最高的
                queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(PradNews::getNeIsValid,1)
                        .orderByDesc( PradNews::getNeDate)
                        .orderByDesc( PradNews::getNeHits);
                pageInfo = newsService.page(pageInfo, queryWrapper);
            }
            //获取轮播图
            queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PradNews::getNeIsCarousel, 1)
								.eq(PradNews::getNeIsValid,1);
            List<PradNews> bannerList = newsService.list(queryWrapper);
            mm.addAttribute("pageInfo", pageInfo);
            mm.addAttribute("bannerList", bannerList);
            mm.addAttribute("isIndex", true);
            mm.addAttribute("bannerSize", bannerList.size()-1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("进入我的专区出错。", e);
            return "error/error";
        }
        return "wechat/front/index";
    }

    /**
     * 我的专区-首页-搜索结果页
     */
    @RequestMapping(value = {"searchResult"})
    public String searchResult(ModelMap mm, String keyword) {
        mm.addAttribute("keyword", keyword);
        return "wechat/front/searchResult";
    }

    /**
     * 我的专区-前沿进展 or 文章专题
     */
    @RequestMapping(value = {"category"})
    public String category(ModelMap mm, Long categoryId) {
        if(categoryId == null){
            categoryId = NewsCategoryConstant.THEORY_FRONT;
        }else{
        	if(categoryId != NewsCategoryConstant.THEORY_FRONT){
				return "redirect:listNewsByCategory?categoryId="+categoryId;
			}
		}
        mm.addAttribute("categoryId",categoryId);
        return "wechat/front/category";
    }

    /**
     * 我的专区-首页-搜索结果AJAX
     * @param keyword 关键字
     * @param rowsPerPage
     * @param page
     */
    @ResponseBody
    @RequestMapping(value = "/searchResultAjax", method = RequestMethod.GET)
    public JsonNode searchResultAjax(String keyword, Integer rowsPerPage, Integer page) {
        if (page == null) {
            page = 1;
        }
        if (rowsPerPage == null) {
            rowsPerPage = Const.ROWSPERPAGE_EIGHT;
        }
        QueryWrapper<PradNews> queryWrapper = new QueryWrapper<>();
        IPage<PradNews> pageInfo = new Page<>(page, rowsPerPage);
        try {
            queryWrapper.lambda().like(StringUtils.isNotEmpty(keyword), PradNews::getNeTitle, keyword)
                    .eq(PradNews::getNeIsValid,1)
                    .orderByDesc(PradNews::getNeDate);
            pageInfo = newsService.page(pageInfo, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = "搜索时出错。";
            logger.error(errmsg , e);
            return JacksonMapper.newErrorInstance(errmsg);
        }
        return JacksonMapper.newDataInstance(pageInfo);
    }


    /**
     * 我的专区-前沿进展列表
     * @param rowsPerPage
     * @param page
     * @param categoryId
     */
    @ResponseBody
    @RequestMapping(value = "/listNewsAjax", method = RequestMethod.GET)
    public JsonNode listNewsAjax(Integer rowsPerPage, Integer page,Long categoryId) {
        if (page == null) {
            page = 1;
        }
        if (rowsPerPage == null) {
            rowsPerPage = Const.ROWSPERPAGE_EIGHT;
        }
        QueryWrapper<PradNews> queryWrapper = new QueryWrapper<>();
        IPage<PradNews> pageInfo = new Page<>(page, rowsPerPage);
        try {
            queryWrapper.lambda().in(PradNews::getNeNcIds,categoryId)
                    .eq(PradNews::getNeIsValid,1)
                    .orderByDesc(PradNews::getNeDate);
//            pageInfo = newsService.page(pageInfo, queryWrapper);
            pageInfo = newsMapper.selectNewsList(pageInfo,categoryId+"");
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = "获取前沿进展文章列表时出错。";
            logger.error(errmsg , e);
            return JacksonMapper.newErrorInstance(errmsg);
        }
        return JacksonMapper.newDataInstance(pageInfo);
    }


    /**
     * 理论前沿-文章专题列表
     */
    @RequestMapping(value = {"listArticleTopic"})
    public String listArticleTopic(ModelMap mm) {
        try {
            QueryWrapper<PradNewsCategory> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(PradNewsCategory::getNcPid,NewsCategoryConstant.ARTICLE_TOPI);
            List<PradNewsCategory> categoryList = newsCategoryService.list(wrapper);
            if(categoryList != null && categoryList.size()>0){
                for(PradNewsCategory category : categoryList){
                   List<PradNews> newsList = newsMapper.selectNewsListByCateId(category.getNcId());
                   category.setNewsCount(newsList.size());
                   if(newsList != null && newsList.size()>0){
                       Long updateTime = newsList.get(0).getNeDate();
                       if(updateTime != null){
                           category.setNewsUpdateTimeStr(TimeUtil.longToString(updateTime, TimeUtil.FORMAT_DATE));
                       }
                   }
                }
            }
//            List<NewsModel> topicList = newsMapper.selectArticleTopic(NewsCategoryConstant.ARTICLE_TOPI);
//            mm.addAttribute("topicList", topicList);
            mm.addAttribute("categoryList", categoryList);
            mm.addAttribute("categoryId", NewsCategoryConstant.ARTICLE_TOPI);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取文章专题列表时出错。" , e);
            return "error/error";
        }
        return "wechat/front/category";
    }

    /**
     * 理论前沿-文章专题-文章列表
     */
    @RequestMapping(value = {"listNewsByCategory"})
    public String listNews(ModelMap mm,Long categoryId){
        PradNewsCategory category = newsCategoryService.getById(categoryId);
        mm.addAttribute("categoryId", categoryId);
        mm.addAttribute("title", category.getNcName());
        return "wechat/front/newsList";
    }

}
