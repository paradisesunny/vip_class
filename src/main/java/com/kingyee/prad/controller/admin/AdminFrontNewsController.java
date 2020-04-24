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
import com.kingyee.prad.entity.PradExpert;
import com.kingyee.prad.entity.PradNews;
import com.kingyee.prad.entity.PradNewsCategory;
import com.kingyee.prad.mapper.PradNewsCategoryMapper;
import com.kingyee.prad.service.IPradExpertService;
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
 * @date 2020/3/10
 * @desc 后台-前沿进展-资讯管理
 */
@Controller
@RequestMapping(value = "/admin/front/news")
public class AdminFrontNewsController {
    private static final Logger logger = LoggerFactory.getLogger(AdminFrontNewsController.class);
    @Autowired
    private IPradNewsService newsService;
    @Autowired
    private IPradNewsCategoryService newsCategoryService;
    @Autowired
    private IPradExpertService expertService;
    @Resource
    private PradNewsCategoryMapper newsCategoryMapper;

    /**
     * list
     * @return
     */
    @RequestMapping("/list")
    public String list(ModelMap mm) {
		return "admin/frontNews/list";
    }

    /**
     * 查询前沿进展和文章专题下的所有资讯列表
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
        List<Long> list = new ArrayList<>();
        //文章专题下的所有分类
		List<PradNewsCategory> childList = newsCategoryMapper.getChildList(NewsCategoryConstant.ARTICLE_TOPI);
        //前沿进展
		list.add(NewsCategoryConstant.THEORY_FRONT);
		if (childList != null && childList.size() != 0) {
			for (int i = 0; i < childList.size(); i++) {
				list.add(i, childList.get(i).getNcId());
			}
		}
        IPage<PradNews> pageInfo = new Page<>(page, limit);
        QueryWrapper<PradNews> queryWrapper = new QueryWrapper<>();
        try {
            if(list != null && list.size()>0){
                for(Long id:list){
                    queryWrapper.lambda()
                            .in(PradNews::getNeNcIds, id).or();
                }
            }
            queryWrapper.lambda()
                    .like(StringUtils.isNotEmpty(keyword), PradNews::getNeTitle, keyword)
                    .orderByDesc(PradNews::getNeDate);
            pageInfo = newsService.page(pageInfo, queryWrapper);
            if(pageInfo.getRecords() != null && pageInfo.getRecords().size()>0){
            	for(PradNews news : pageInfo.getRecords()){
					if(StringUtils.isNotEmpty(news.getNeNcIds())){
					    String[] ids = news.getNeNcIds().split(",");
					    String cateName = "";
					    if(ids != null && ids.length >0){
					        for(String id :ids){
                                PradNewsCategory category = newsCategoryService.getById(Long.parseLong(id));
                                cateName += category.getNcName()+" | ";
                            }
                        }
                        news.setCategoryName(cateName);
					}
					if(news.getNeExpertId() == null || news.getNeExpertId() == 0){
						news.setExpertName("无");
					}else{
						PradExpert expert = expertService.getById(news.getNeExpertId());
						news.setExpertName(expert.getPeName());
					}
				}
			}
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取前沿进展-资讯列表时出错";
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
                mm.addAttribute("title", "资讯编辑");
            } else {
                mm.addAttribute("title", "资讯添加");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("编辑资讯时异常", e);
			return "error/error";
        }
		return "admin/frontNews/frontEdit";
    }


    /**
     * update and save
     * 此时news中的ne_nc_id是子树的id
     * @param news
     * @return
     */
    @RequestMapping(value = "/save")
    public String save(PradNews news) {
        try {
            news.setNeNcId(null);
            Long now = TimeUtil.dateToLong();
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
            logger.error("保存资讯异常", e);
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
			news.setNeUpdateTime(System.currentTimeMillis());
			news.setNeUpdateUserId(AdminUserUtil.getUserId());
			news.setNeUpdateUserName(AdminUserUtil.getShowName());
			newsService.updateById(news);
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "删除资讯异常";
            logger.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * Change state(是否是头图,轮播图是否显示,资讯是否有效)
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
     * 查询专家列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getExpertList", method = RequestMethod.GET)
    public JsonNode getExpertList() {
        List<PradExpert> expertList = expertService.list();
        return JacksonMapper.newDataInstance(expertList);
    }

    /**
     * 查询文章专题
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getCategory", method = RequestMethod.GET)
    public JsonNode getCategory() {
		try {
		    //前沿进展
            PradNewsCategory parent = newsCategoryService.getById(NewsCategoryConstant.THEORY_FRONT);
            //文章专题
			QueryWrapper<PradNewsCategory> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(PradNewsCategory::getNcPid, NewsCategoryConstant.ARTICLE_TOPI);
			List<PradNewsCategory> cList = newsCategoryService.list(queryWrapper);
            cList.add(0,parent);
			return JacksonMapper.newDataInstance(cList);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg = "查询文章专题异常";
			logger.error(errMsg, e);
			return JacksonMapper.newErrorInstance(errMsg);
		}
    }
}
