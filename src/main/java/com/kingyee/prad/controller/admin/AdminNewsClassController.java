package com.kingyee.prad.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.common.Const;
import com.kingyee.prad.common.NewsCategoryConstant;
import com.kingyee.prad.common.security.AdminUserUtil;
import com.kingyee.prad.entity.PradNews;
import com.kingyee.prad.entity.PradNewsCategory;
import com.kingyee.prad.mapper.PradNewsCategoryMapper;
import com.kingyee.prad.service.IPradNewsCategoryService;
import com.kingyee.prad.service.IPradNewsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nmy
 * @date 2020/4/10
 * @desc 后台-资讯管理-规范化课程
 */
@Controller
@RequestMapping(value = "/admin/newsClass")
public class AdminNewsClassController {
    private static final Logger logger = LoggerFactory.getLogger(AdminNewsClassController.class);
    @Autowired
    private IPradNewsService newsService;
    @Autowired
    private IPradNewsCategoryService newsCategoryService;

    /**
     * list
     *
     * @return
     */
    @RequestMapping("/list")
    public String list() {
        return "admin/newsClass/list";
    }

    /**
     * ajax request
     *
     * @param page
     * @param limit
     * @param keyword
     * @return
     */
    @ResponseBody
    @RequestMapping("/listAjax")
    public JsonNode listAjax(Integer page, Integer limit, String keyword) {
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = Const.ADMIN_ROWSPERPAGE_MORE;
        }
        IPage<PradNews> pageInfo = new Page<>(page, limit);
        QueryWrapper<PradNews> newsQueryWrapper = new QueryWrapper<>();
        QueryWrapper<PradNewsCategory> categoryQueryWrapper = new QueryWrapper<>();
        try {
            categoryQueryWrapper.lambda()
                    .eq(PradNewsCategory::getNcPid,0)
                    .eq(PradNewsCategory::getNcName,NewsCategoryConstant.map.get(NewsCategoryConstant.NEWS_CLASS));
            PradNewsCategory category = newsCategoryService.getOne(categoryQueryWrapper);
            newsQueryWrapper.lambda()
                    .eq(PradNews::getNeNcId, category.getNcId())
                    .like(StringUtils.isNotEmpty(keyword), PradNews::getNeTitle, keyword);
            pageInfo = newsService.page(pageInfo, newsQueryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取规范化课程列表时出错";
            logger.error(errMsg + e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
        return JacksonMapper.newCountInstance(pageInfo);
    }

    /**
     * go to edit or add page
     *
     * @param mm
     * @param newsId 编辑所需参数
     * @return
     */
    @RequestMapping("/edit")
    public String edit(ModelMap mm, Long newsId) {
        try {
            if (newsId != null) {
                PradNews news = newsService.getById(newsId);
                mm.addAttribute("news", news);
                mm.addAttribute("title", "规范化课程——资讯编辑");
            } else {
                mm.addAttribute("title", "规范化课程——资讯添加");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("编辑规范化课程时异常", e);
        }
        return "admin/newsClass/edit";
    }


    /**
     * update and save
     * 此时news中的ne_nc_id是子树的id
     * 返回列表携带的参数catePk应该是规范化课程的父id
     *
     * @param news
     * @return
     */
    @RequestMapping(value = "/save")
    public String save(PradNews news) {
        try {
            QueryWrapper<PradNewsCategory> categoryQueryWrapper = new QueryWrapper<>();
            categoryQueryWrapper.lambda()
                    .eq(PradNewsCategory::getNcPid,0)
                    .eq(PradNewsCategory::getNcName,NewsCategoryConstant.map.get(NewsCategoryConstant.NEWS_CLASS));
            PradNewsCategory category = newsCategoryService.getOne(categoryQueryWrapper);
            news.setNeNcId(category.getNcId());
            if (news.getNeId() == null) {
                // 新增
                news.setNeLikeNum(0);
                news.setNeHits(0);
                news.setNeBookNum(0);
                news.setNeCreateTime(System.currentTimeMillis());
                news.setNePublishUserId(AdminUserUtil.getUserId());
                news.setNePublishUserName(AdminUserUtil.getShowName());
                newsService.save(news);
            } else {
                // 修改
                news.setNeUpdateTime(System.currentTimeMillis());
                news.setNeUpdateUserId(AdminUserUtil.getUserId());
                news.setNeUpdateUserName(AdminUserUtil.getShowName());
                newsService.updateById(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存规范化课程异常", e);
            return "error/error";
        }
        return "redirect:list";
    }

    /**
     * delete by id
     *
     * @param pk
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public JsonNode delete(Long pk) {
        try {
            PradNews news = newsService.getById(pk);
            news.setNeIsValid(0);
            newsService.updateById(news);
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "删除规范化课程异常";
            logger.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * Change state(是否是头图,轮播图是否显示,资讯是否有效)
     *
     * @param pk
     * @param banner
     * @param valid
     * @param carousel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "changeState", method = RequestMethod.GET)
    public JsonNode changState(Long pk, String banner, String valid, String carousel) {
        try {
            PradNews news = newsService.getById(pk);
            if (StringUtils.isNotEmpty(banner)) {
                news.setNeIsBanner(Integer.parseInt(banner));
                newsService.updateById(news);
                return JacksonMapper.newSuccessInstance();
            } else if (StringUtils.isNotEmpty(valid)) {
                news.setNeIsValid(Integer.parseInt(valid));
                newsService.updateById(news);
                return JacksonMapper.newSuccessInstance();
            } else if (StringUtils.isNotEmpty(carousel)) {
                news.setNeIsCarousel(Integer.parseInt(carousel));
                newsService.updateById(news);
                return JacksonMapper.newSuccessInstance();
            } else {
                return JacksonMapper.newErrorInstance("状态更改失败");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JacksonMapper.newErrorInstance("参数传递不完整");
        }
    }

}
