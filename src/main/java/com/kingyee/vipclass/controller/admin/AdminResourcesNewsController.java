package com.kingyee.vipclass.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.vipclass.common.Const;
import com.kingyee.vipclass.common.NewsCategoryConstant;
import com.kingyee.vipclass.common.security.AdminUserUtil;
import com.kingyee.vipclass.entity.PradNews;
import com.kingyee.vipclass.entity.PradNewsCategory;
import com.kingyee.vipclass.mapper.PradNewsCategoryMapper;
import com.kingyee.vipclass.service.IPradNewsCategoryService;
import com.kingyee.vipclass.service.IPradNewsService;
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
 * @author dxl
 * @date 2020/4/10
 * @desc 后台-资讯管理-医学资源
 */
@Controller
@RequestMapping(value = "/admin/resources/news")
public class AdminResourcesNewsController {
    private static final Logger logger = LoggerFactory.getLogger(AdminResourcesNewsController.class);
    @Autowired
    private IPradNewsService newsService;
    @Autowired
    private IPradNewsCategoryService newsCategoryService;
    @Resource
    private PradNewsCategoryMapper newsCategoryMapper;

    /**
     * list
     *
     * @return
     */
    @RequestMapping("/list")
    public String list(ModelMap mm) {
        try {
            PradNewsCategory category = newsCategoryService.getById(NewsCategoryConstant.DOCTOR_RESOURCES);
            mm.addAttribute("category", category);
//            左侧导航栏需要使用
            mm.addAttribute("catePk", NewsCategoryConstant.DOCTOR_RESOURCES);
            return "admin/resourcesNews/list";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询医学资源列表参数不完整");
            return "error/error";
        }
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
//        获取该模块下所有的子树
        List<PradNewsCategory> childList = newsCategoryMapper.getChildList(NewsCategoryConstant.DOCTOR_RESOURCES);
        List<Long> list = new ArrayList<Long>();
        if (childList == null || childList.size() == 0) {
            list.add(NewsCategoryConstant.DOCTOR_RESOURCES);
        } else {
            for (int i = 0; i < childList.size(); i++) {
                list.add(i, childList.get(i).getNcId());
            }
        }
        IPage<PradNews> pageInfo = new Page<>(page, limit);
        QueryWrapper<PradNews> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda()
                    .in(PradNews::getNeNcId, list)
                    .like(StringUtils.isNotEmpty(keyword), PradNews::getNeTitle, keyword);
            pageInfo = newsService.page(pageInfo, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取医学资源列表时出错";
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
//                time是直播倒计时专用字段
                mm.addAttribute("startTime", TimeUtil.longToString(news.getNeDate(), "yyyy-MM-dd HH:mm:ss"));
                mm.addAttribute("endTime", TimeUtil.longToString(news.getNeEndDate(), "yyyy-MM-dd HH:mm:ss"));
                mm.addAttribute("title", "资讯编辑");
            } else {
                mm.addAttribute("title", "资讯添加");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("编辑医学资源时异常", e);
        }
        return "admin/resourcesNews/edit";
    }


    /**
     * update and save
     * 此时news中的ne_nc_id是子树的id
     * 返回列表携带的参数catePk应该是医学资源的父id
     *
     * @param news
     * @return
     */
    @RequestMapping(value = "/save")
    public String save(PradNews news, String neStartDateStr, String neEndDateStr) {
        try {
            Long now = TimeUtil.dateToLong();
            if (StringUtils.isNotEmpty(neEndDateStr)) {
                news.setNeEndDate(TimeUtil.stringToLong(neEndDateStr, TimeUtil.FORMAT_DATETIME_FULL));
            }
            if (StringUtils.isNotEmpty(neStartDateStr)) {
                news.setNeDate(TimeUtil.stringToLong(neStartDateStr, TimeUtil.FORMAT_DATETIME_FULL));
            }
            if (news.getNeId() == null) {
                // 新增
                news.setNeLikeNum(0);
                news.setNeHits(0);
                news.setNeBookNum(0);
                news.setNeCreateTime(now);
                news.setNePublishUserId(AdminUserUtil.getUserId());
                news.setNePublishUserName(AdminUserUtil.getShowName());
                newsService.save(news);
            } else {
                // 修改
                news.setNeUpdateTime(now);
                news.setNeUpdateUserId(AdminUserUtil.getUserId());
                news.setNeUpdateUserName(AdminUserUtil.getShowName());
                newsService.updateById(news);
            }
            return "redirect:list";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存医学资源异常", e);
            return "error/error";
        }
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
            String errMsg = "删除医学资源异常";
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

    /**
     * 查询医学资源模块下的专题列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getTopic", method = RequestMethod.GET)
    public JsonNode getTopic() {
        QueryWrapper<PradNewsCategory> queryWrapper = new QueryWrapper<PradNewsCategory>();
        queryWrapper.lambda().eq(PradNewsCategory::getNcPid, NewsCategoryConstant.DOCTOR_RESOURCES);
        List<PradNewsCategory> topicList = newsCategoryService.list(queryWrapper);
        return JacksonMapper.newDataInstance(topicList);
    }
}
