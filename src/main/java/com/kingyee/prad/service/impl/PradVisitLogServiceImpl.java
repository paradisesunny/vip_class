package com.kingyee.prad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingyee.common.excel.ExcelData;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.common.NewsCategoryConstant;
import com.kingyee.prad.common.security.UserUtil;
import com.kingyee.prad.entity.*;
import com.kingyee.prad.mapper.PradNewsCategoryMapper;
import com.kingyee.prad.mapper.PradVisitLogMapper;
import com.kingyee.prad.service.IPradVisitLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 访问记录日志 服务实现类
 * </p>
 *
 * @author dxl
 * @since 2020-03-24
 */
@Service
public class PradVisitLogServiceImpl extends ServiceImpl<PradVisitLogMapper, PradVisitLog> implements IPradVisitLogService {
    @Autowired
    private PradNewsServiceImpl newsService;
    @Autowired
    private PradNewsCategoryServiceImpl categoryService;
    @Autowired
    private PradLikeRecordServiceImpl likeRecordService;
    @Autowired
    private PradViewRecordServiceImpl viewRecordService;
    @Autowired
    private PradBookmarkRecordServiceImpl bookmarkRecordService;

    /**
     * 保存一条访问记录
     *
     * @param newsId
     * @param request
     * @param like
     * @param favorite
     * @return
     */
    public boolean saveVisit(Long newsId, HttpServletRequest request, Integer like, Integer favorite, Long vrId) {
        PradNews news = newsService.getById(newsId);
        if (null == news) {
            return false;
        }
        Long userId = UserUtil.getUserId();
        Long now = System.currentTimeMillis();
        String ip = getRemoteIpAddr(request);
        PradVisitLog visitLog = new PradVisitLog();
        visitLog.setVlUserId(userId);
        QueryWrapper<PradNewsCategory> cqueryWrapper = new QueryWrapper<>();
        cqueryWrapper.lambda().eq(PradNewsCategory::getNcId, news.getNeNcId());
        PradNewsCategory child = categoryService.getOne(cqueryWrapper, false);
        if(child != null){
            visitLog.setVlNewsModule(child.getNcName());
            visitLog.setVlVisitModule(NewsCategoryConstant.getType(child.getNcPid()));
        }
        visitLog.setVlNewsId(newsId);
        if (StringUtils.isNotEmpty(news.getNeVideoPath())) {
            visitLog.setVlIncludeVideo(1);//包含视频
        } else {
            visitLog.setVlIncludeVideo(0);//不包含视频
        }
        visitLog.setVlStartTime(now);
        visitLog.setVlEndTime(now);
        //访问时长,随机1-5秒
        visitLog.setVlDuration(new Random().nextInt(5) * 1L);
        if (like.equals(1)) {
            QueryWrapper<PradLikeRecord> likeRecordQueryWrapper = new QueryWrapper<>();
            likeRecordQueryWrapper.lambda().eq(PradLikeRecord::getLrUserId, userId)
                    .eq(PradLikeRecord::getLrNewsId, newsId);
            Long time = likeRecordService.getOne(likeRecordQueryWrapper, false).getLrTime();
            visitLog.setVlLike(1);
            visitLog.setVlLikeTime(time);
        } else {
            visitLog.setVlLike(0);
        }
        if (favorite.equals(1)) {
            QueryWrapper<PradBookmarkRecord> bookmarkRecordQueryWrapper = new QueryWrapper<>();
            bookmarkRecordQueryWrapper.lambda().eq(PradBookmarkRecord::getBrUserId, userId)
                    .eq(PradBookmarkRecord::getBrNewsId, newsId);
            Long time = bookmarkRecordService.getOne(bookmarkRecordQueryWrapper, false).getBrTime();
            visitLog.setVlFavorite(1);
            visitLog.setVlFavoriteTime(time);
        } else {
            visitLog.setVlFavorite(0);
        }
        visitLog.setVlIp(ip);
        visitLog.setVlVrId(vrId);
        return save(visitLog);
    }

    /**
     * 导出日志
     *
     * @param visitLogList
     * @return
     */
    @Override
    public ExcelData exportLogExcel(List<PradVisitLog> visitLogList) {
        ExcelData data = new ExcelData();
        data.setName("访问日志");
        List<String> titles = Arrays.asList("用户表主键", "观看板块", "资讯主键ID", "资讯所属领域", "浏览开始时间", "浏览结束时间",
                "浏览总时长（秒）", "是否包含视频（是/否）", "是否点赞（是/否/取消点赞）", "点赞时间", "是否收藏（是/否/取消收藏）",
                "收藏时间", "视频浏览ID");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList<>();
        if (visitLogList != null && visitLogList.size() > 0) {
            for (PradVisitLog log : visitLogList) {
                List<Object> row = new ArrayList<>();
                row.add(log.getVlUserId());
                row.add(log.getVlVisitModule());
                row.add(log.getVlNewsId());
                row.add(log.getVlNewsModule());
                row.add(TimeUtil.longToString(log.getVlStartTime(), "yyyy-MM-dd"));
                row.add(TimeUtil.longToString(log.getVlEndTime(), "yyyy-MM-dd"));
                row.add(String.valueOf(log.getVlDuration()));
                row.add(log.getVlIncludeVideo());
                row.add(log.getVlLike());
                row.add(TimeUtil.longToString(log.getVlLikeTime(), "yyyy-MM-dd"));
                row.add(log.getVlFavorite());
                row.add(TimeUtil.longToString(log.getVlFavoriteTime(), "yyyy-MM-dd"));
                row.add(log.getVlVrId());
                rows.add(row);
            }
        }
        data.setRows(rows);
        return data;
    }

    /**
     * 判断IP地址是否有效
     *
     * @param ip
     * @return
     */
    private static boolean isValidIpAddr(String ip) {
        return ip != null && !ip.isEmpty() && !ip.equalsIgnoreCase("unknown");
    }

    /**
     * 取得请求的IP地址
     *
     * @param request
     * @return
     */
    public static String getRemoteIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (isValidIpAddr(ip)) {
            return ip.split(",")[0];
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIpAddr(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIpAddr(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIpAddr(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }
}
