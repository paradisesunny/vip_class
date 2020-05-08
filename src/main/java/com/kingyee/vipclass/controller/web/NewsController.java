package com.kingyee.vipclass.controller.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeCountUtil;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.vipclass.common.NewsCategoryConstant;
import com.kingyee.vipclass.common.security.UserUtil;
import com.kingyee.vipclass.entity.*;
import com.kingyee.vipclass.mapper.PradCommentMapper;
import com.kingyee.vipclass.model.NewsCommentModel;
import com.kingyee.vipclass.service.*;
import com.kingyee.vipclass.service.impl.PradViewRecordServiceImpl;
import com.kingyee.vipclass.service.impl.PradVisitLogServiceImpl;
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
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nmy
 * @date 2020/3/10
 * @desc 文章Controller
 */
@Controller
@RequestMapping(value = "/web/news")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    private IPradNewsService newsService;
    @Autowired
    private IPradBookmarkRecordService bookmarkRecordService;
    @Autowired
    private IPradLikeRecordService likeRecordService;
    @Autowired
    private IPradNewsCategoryService newsCategoryService;
    @Autowired
    private IPradCommentService commentService;
	@Resource
	private PradCommentMapper commentMapper;
    @Autowired
    private PradVisitLogServiceImpl visitLogService;
    @Autowired
    private PradViewRecordServiceImpl recordService;


    /**
     * 文章详情
     */
    @RequestMapping(value = {"getNewsById"})
    public String getNewsById(ModelMap mm, Long newsId, HttpServletRequest request) {
        try {
            PradNews news = newsService.getById(newsId);
            if (news != null) {
                Long catId = news.getNeNcId();
                if (catId != null) {
                    PradNewsCategory category = newsCategoryService.getById(catId);
                    if (NewsCategoryConstant.DOCTOR_RESOURCES.equals(category.getNcPid())) {
                        return "redirect:/web/resources/detail?pk=" + news.getNeId();
                    }
                }
                news.setNeHits(news.getNeHits() == null ? 1 : news.getNeHits() + 1);
                newsService.updateById(news);
                //查询所属分类
                List<PradNewsCategory> categoryList = new ArrayList<>();
                if (StringUtils.isNotEmpty(news.getNeNcIds())) {
                    String[] cateIds = news.getNeNcIds().split(",");
                    if (cateIds != null && cateIds.length > 0) {
                        for (String cateId : cateIds) {
                            if (StringUtils.isNotEmpty(cateId)) {
                                PradNewsCategory category = newsCategoryService.getById(cateId);
                                categoryList.add(category);
                            }
                        }
                    }
                }
                mm.addAttribute("categoryList", categoryList);

                //是否收藏和点赞
                if (UserUtil.hasLogin()) {
                    QueryWrapper<PradBookmarkRecord> bookWrapper = new QueryWrapper();
                    bookWrapper.lambda().eq(PradBookmarkRecord::getBrNewsId, newsId)
                            .eq(PradBookmarkRecord::getBrUserId, UserUtil.getUserId());
                    PradBookmarkRecord bookRecord = bookmarkRecordService.getOne(bookWrapper);
                    mm.addAttribute("bookRecord", bookRecord);

                    QueryWrapper<PradLikeRecord> likeWrapper = new QueryWrapper();
                    likeWrapper.lambda().eq(PradLikeRecord::getLrNewsId, newsId)
                            .eq(PradLikeRecord::getLrType, 0)
                            .eq(PradLikeRecord::getLrUserId, UserUtil.getUserId());
                    PradLikeRecord likeRecord = likeRecordService.getOne(likeWrapper);
                    mm.addAttribute("likeRecord", likeRecord);
                }

                //评论列表
                List<NewsCommentModel> commentList = new ArrayList<>();
                if (UserUtil.hasLogin()) {
                    commentList = commentMapper.getCommentList(newsId, UserUtil.getUserId());
                } else {
                    QueryWrapper<PradComment> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(PradComment::getPcNewsId, newsId)
                            .orderByDesc(PradComment::getPcTime);
                    List<PradComment> commentList_ = commentService.list(queryWrapper);
                    if (commentList_ != null && commentList_.size() > 0) {
                        for (PradComment comment : commentList_) {
                            if (comment.getPcTime() != null) {
                                String timeStr = TimeCountUtil.format(TimeUtil.longToDate(comment.getPcTime()));
                                comment.setPcTimeStr(timeStr);
                            }
                            NewsCommentModel commentModel = new NewsCommentModel();
                            commentModel.setCommentId(comment.getPcId());
                            commentModel.setCommentTime(comment.getPcTime());
                            commentModel.setContent(comment.getPcContent());
                            commentModel.setLikeCommentId(comment.getPcId());
                            commentModel.setUserName(comment.getPcUserName());
                            commentModel.setLikeNum(comment.getPcHits());
                            commentList.add(commentModel);
                        }
                    }
                }
                mm.addAttribute("commentList", commentList);
            }
            mm.addAttribute("news", news);
            //保存至浏览记录
            QueryWrapper<PradViewRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PradViewRecord::getVrUserId, UserUtil.getUserId())
                    .eq(PradViewRecord::getVrNewsId, newsId);
            PradViewRecord record = recordService.getOne(queryWrapper, false);
            if (null != record) {
                record.setVrTime(TimeUtil.dateTolong());
                recordService.updateById(record);
            } else {
                PradViewRecord newRecord = new PradViewRecord();
                newRecord.setVrNewsId(newsId);
                newRecord.setVrUserId(UserUtil.getUserId());
                newRecord.setVrTime(TimeUtil.dateTolong());
                recordService.save(newRecord);
            }
            //保存记录日志
            visitLogService.saveVisit(newsId, request, 0, 0, news.getNeId());
            return "web/front/newsDetail";
        }catch (Exception e){
            e.printStackTrace();
            String errmsg = "获取前沿进展——文章详情失败";
            logger.error(errmsg , e);
            return "error/error";
        }
    }


    /**
     * 收藏 取消收藏
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addOrRemoveBookRecord", method = RequestMethod.GET)
    public JsonNode addOrRemoveBookRecord(Long newsId, String url) {
        Integer tag = 0;
        try {
            QueryWrapper<PradBookmarkRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PradBookmarkRecord::getBrNewsId,newsId)
                    .eq(PradBookmarkRecord::getBrUserId,UserUtil.getUserId());
            PradBookmarkRecord record = bookmarkRecordService.getOne(queryWrapper);
            if(record != null){
                bookmarkRecordService.removeById(record);
                PradNews news = newsService.getById(newsId);
                if(news.getNeLikeNum() > 0){
                    news.setNeBookNum(news.getNeBookNum()-1);
                }else{
                    news.setNeBookNum(0);
                }
                newsService.updateById(news);
            }else{
                record = new PradBookmarkRecord();
                record.setBrArticlePath(url);
                record.setBrNewsId(newsId);
                record.setBrUserId(UserUtil.getUserId());
                record.setBrTime(System.currentTimeMillis());
                bookmarkRecordService.save(record);
                PradNews news = newsService.getById(newsId);
                news.setNeBookNum(news.getNeBookNum() == null?1:news.getNeBookNum()+1);
                newsService.updateById(news);
                tag = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = "";
            if(tag == 0){
                errmsg = "取消资讯收藏失败。";
            }else{
                errmsg = "资讯收藏失败。";
            }
            logger.error(errmsg , e);
            return JacksonMapper.newErrorInstance(errmsg);
        }
        return JacksonMapper.newSuccessInstance();
    }

    /**
     * 资讯点赞/评论点赞  不可取消
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addLikeRecord", method = RequestMethod.GET)
    public JsonNode addLikeRecord(Long newsId,Long commentId) {
        QueryWrapper<PradLikeRecord> queryWrapper = new QueryWrapper<>();
        try {
            if(newsId == null && commentId == null){
                return JacksonMapper.newErrorInstance("点赞失败,id为空");
            }
			if(commentId == null){
				//资讯点赞
				queryWrapper.lambda().eq(PradLikeRecord::getLrNewsId,newsId)
						.eq(PradLikeRecord::getLrType,0)
						.eq(PradLikeRecord::getLrUserId,UserUtil.getUserId());
				PradLikeRecord record = likeRecordService.getOne(queryWrapper);
				if(record == null){
					record = new PradLikeRecord();
					record.setLrNewsId(newsId);
					record.setLrUserId(UserUtil.getUserId());
					record.setLrTime(System.currentTimeMillis());
					record.setLrType(0);
					likeRecordService.save(record);
					PradNews news = newsService.getById(newsId);
					news.setNeLikeNum(news.getNeLikeNum() == null?1:news.getNeLikeNum()+1);
					newsService.updateById(news);
				}
			}else if(commentId != null) {
				//评论点赞
				queryWrapper.lambda().eq(PradLikeRecord::getLrCommentId, commentId)
						.eq(PradLikeRecord::getLrType,1)
						.eq(PradLikeRecord::getLrUserId, UserUtil.getUserId());
				PradLikeRecord record = likeRecordService.getOne(queryWrapper);
				if (record == null) {
					record = new PradLikeRecord();
					record.setLrNewsId(newsId);
					record.setLrUserId(UserUtil.getUserId());
					record.setLrCommentId(commentId);
					record.setLrTime(System.currentTimeMillis());
					record.setLrType(1);
					likeRecordService.save(record);
					PradComment comment = commentService.getById(commentId);
					comment.setPcHits(comment.getPcHits() == null ? 1 : comment.getPcHits() + 1);
					commentService.updateById(comment);
				}
			}
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = "点赞失败";
            logger.error(errmsg , e);
            return JacksonMapper.newErrorInstance(errmsg);
        }
        return JacksonMapper.newSuccessInstance();
    }


    /**
     * 发表评论——查看是否评论过
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCommentById", method = RequestMethod.GET)
    public JsonNode getCommentById(Long newsId) {
        try {
            QueryWrapper<PradComment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PradComment::getPcNewsId,newsId)
                    .eq(PradComment::getPcUserId,UserUtil.getUserId());
            PradComment record = commentService.getOne(queryWrapper);
            if(record != null){
                return JacksonMapper.newErrorInstance("您已经评论过。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = "获取用户是否评论失败";
            logger.error(errmsg , e);
            return JacksonMapper.newErrorInstance(errmsg);
        }
        return JacksonMapper.newSuccessInstance();
    }


    /**
     * 发表评论
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/saveComment", method = RequestMethod.GET)
    public JsonNode saveComment(PradComment pradComment) {
        try {
            pradComment.setPcUserId(UserUtil.getUserId());
            pradComment.setPcUserName(UserUtil.getUserName());
            pradComment.setPcTime(System.currentTimeMillis());
            commentService.save(pradComment);
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg = "评论失败";
            logger.error(errmsg , e);
			return JacksonMapper.newErrorInstance(errmsg);
        }
		return JacksonMapper.newSuccessInstance();
    }
}
