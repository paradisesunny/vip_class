package com.kingyee.prad.controller.wechat;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.common.Const;
import com.kingyee.prad.common.NewsCategoryConstant;
import com.kingyee.prad.common.security.UserUtil;
import com.kingyee.prad.entity.PradNews;
import com.kingyee.prad.entity.PradNewsCategory;
import com.kingyee.prad.entity.PradViewRecord;
import com.kingyee.prad.mapper.PradNewsCategoryMapper;
import com.kingyee.prad.mapper.PradNewsMapper;
import com.kingyee.prad.model.NewsModel;
import com.kingyee.prad.service.IPradNewsCategoryService;
import com.kingyee.prad.service.IPradNewsService;
import com.kingyee.prad.service.impl.PradViewRecordServiceImpl;
import com.kingyee.prad.service.impl.PradVisitLogServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author nmy
 * @date 2020/3/10
 * @desc 医学资源
 */
@Controller
@RequestMapping(value = "/wechat/resources")
public class DoctorResourcesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorResourcesController.class);
    @Autowired
    private IPradNewsService newsService;
    @Autowired
    private IPradNewsCategoryService newsCategoryService;
    @Resource
    private PradNewsMapper newsMapper;
    @Resource
    private PradNewsCategoryMapper newsCategoryMapper;
    @Autowired
    private PradViewRecordServiceImpl recordService;
    @Autowired
    private PradVisitLogServiceImpl visitLogService;

    /**
     * 医学资源进入页 精彩回放显示两天内发布并且点击率最高的两个视频
     */
    @RequestMapping(value = {"", "index"})
    public String index(ModelMap mm) {
        QueryWrapper<PradNews> qPlayback = new QueryWrapper<>();
        QueryWrapper<PradNews> qLive = new QueryWrapper<>();
        try {
//           取回放
            Long nowDate = System.currentTimeMillis();
            Long lastTwoDate = TimeUtil.thisDateLastDate(2);
            List<PradNewsCategory> childList = newsCategoryMapper.getChildList(NewsCategoryConstant.DOCTOR_RESOURCES);
            List<Long> list = new ArrayList<Long>();
            for (int i = 0; i < childList.size(); i++) {
                list.add(i, childList.get(i).getNcId());
            }
            qPlayback.lambda()
                    .in(PradNews::getNeNcId, list)
                    .between(PradNews::getNeEndDate, lastTwoDate, nowDate)
                    .eq(PradNews::getNeIsValid, 1)
                    .orderByDesc(PradNews::getNeHits)
                    .last("limit 2");
            List playbackList = newsService.list(qPlayback);
//            取近期直播
            qLive.lambda().in(PradNews::getNeNcId, list)
                    .gt(PradNews::getNeEndDate, nowDate)
                    .eq(PradNews::getNeIsValid, 1)
                    .orderByAsc(PradNews::getNeDate)
                    .last("limit 1");
            PradNews live = newsService.getOne(qLive, false);
            if (live != null) {
                mm.addAttribute("live", live);
            }
            mm.addAttribute("playbackList", playbackList);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("进入我的专区出错。", e);
            return "error/error";
        }
        return "wechat/resources/index";
    }

    /**
     * 医学资讯-精彩回放-文件夹列表
     *
     * @param mm
     * @return
     */
    @RequestMapping(value = {"playbackFolder"})
    public String playbackFolder(ModelMap mm) {
        try {
            List<NewsModel> playbackFolder = newsMapper.getPlaybackFolder(NewsCategoryConstant.DOCTOR_RESOURCES);
            mm.addAttribute("playbackFolder", playbackFolder);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("获取医学资源文件夹列表时出错。", e);
            return "error/error";
        }
        return "wechat/resources/playbackFolder";
    }

    /**
     * 医学资讯-精彩回放-内容章列表
     */
    @RequestMapping(value = {"listNewsByCategory"})
    public String listNews(ModelMap mm, Long categoryId) {
        mm.addAttribute("categoryId", categoryId);
        mm.addAttribute("title", newsCategoryService.getById(categoryId).getNcName());
        mm.addAttribute("date", new Date().getTime());
        return "wechat/resources/newsList";
    }

    @ResponseBody
    @RequestMapping(value = "listNewsAjax")
    public JsonNode listNewsAjax(Integer rowsPerPage, Integer page, Long categoryId) {
        if (rowsPerPage == null) {
            rowsPerPage = Const.ROWSPERPAGE_EIGHT;
        }
        if (page == null) {
            page = 1;
        }
        QueryWrapper<PradNews> queryWrapper = new QueryWrapper<>();
        IPage<PradNews> pageInfo = new Page<>(page, rowsPerPage);
        Long now = TimeUtil.dateTolong();
        try {
            queryWrapper.lambda()
                    .eq(categoryId != null, PradNews::getNeNcId, categoryId)
                    .eq(PradNews::getNeIsValid, 1)
                    .lt(PradNews::getNeEndDate, now);
            pageInfo = newsService.page(pageInfo, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取医学资源列表时出错";
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
        return JacksonMapper.newCountInstance(pageInfo);
    }

    /**
     * 详情
     *
     * @param mm
     * @param pk
     * @return
     */
    @RequestMapping(value = "detail")
    public String detail(ModelMap mm, Long pk, HttpServletRequest request) {
        try {
            Long userId = UserUtil.getUserId();
            PradNews news = newsService.getById(pk);
//            点击量+1
            news.setNeHits(news.getNeHits() + 1);
            newsService.updateById(news);
            Long childPk = news.getNeNcId();
            PradNewsCategory category = newsCategoryService.getById(childPk);
//            获取主题名称
            mm.addAttribute("title", category.getNcName());
            mm.addAttribute("news", news);
            mm.addAttribute("openId", UserUtil.getOpenid());
//           保存至浏览记录
            QueryWrapper<PradViewRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PradViewRecord::getVrUserId, userId)
                    .eq(PradViewRecord::getVrNewsId, pk);
            PradViewRecord record = recordService.getOne(queryWrapper, false);
            if (null != record) {
                record.setVrTime(TimeUtil.dateTolong());
                recordService.updateById(record);
            } else {
                PradViewRecord newRecord = new PradViewRecord();
                newRecord.setVrNewsId(pk);
                newRecord.setVrUserId(userId);
                newRecord.setVrTime(TimeUtil.dateTolong());
                recordService.save(newRecord);
            }
//            保存记录日志
            visitLogService.saveVisit(pk, request, 0, 0, record.getVrId());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("详情查询出错", e);
        }
        return "wechat/resources/detail";
    }

    /**
     * 跳转直播
     *
     * @param mm
     * @param pk
     * @return
     */
    @RequestMapping(value = "live")
    public String live(ModelMap mm, Long pk) {
        try {
            PradNews news = newsService.getById(pk);
//            点击量+1
            news.setNeHits(news.getNeHits() + 1);
            newsService.updateById(news);
            Long childPk = news.getNeNcId();
            PradNewsCategory category = newsCategoryService.getById(childPk);
//            获取主题名称
            mm.addAttribute("title", category.getNcName());
            mm.addAttribute("news", news);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("详情查询出错", e);
        }
        return "wechat/resources/live";
    }
}
