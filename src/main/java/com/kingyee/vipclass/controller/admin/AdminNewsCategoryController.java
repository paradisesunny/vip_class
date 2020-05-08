package com.kingyee.vipclass.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.vipclass.entity.PradNews;
import com.kingyee.vipclass.entity.PradNewsCategory;
import com.kingyee.vipclass.mapper.PradNewsCategoryMapper;
import com.kingyee.vipclass.mapper.PradNewsMapper;
import com.kingyee.vipclass.model.NewsCategoryModel;
import com.kingyee.vipclass.service.IPradNewsCategoryService;
import com.kingyee.vipclass.service.impl.PradNewsServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类树
 *
 * @author dxl
 * @date 2020-3-19
 */
@Controller
@RequestMapping(value = "/admin/newsCate")
public class AdminNewsCategoryController {
    private final static Logger LOGGER = LoggerFactory.getLogger(AdminNewsCategoryController.class);
    @Autowired
    private IPradNewsCategoryService newsCategoryService;
    @Resource
    private PradNewsCategoryMapper newsCategoryMapper;
    @Autowired
    private PradNewsServiceImpl newsService;
	@Resource
	private PradNewsMapper newsMapper;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = {"", "index"})
    public String index(ModelMap mm,String type) {
		mm.addAttribute("type",type);
        return "admin/news/newsCateIndex";
    }

    /**
     * 获取列表
     */
    @ResponseBody
    @RequestMapping("listAjax")
    public JsonNode listAjax(String type) {
        try {
            List<NewsCategoryModel> categoryList = newsCategoryMapper.getCategoryModelList(0L,type);
            for (NewsCategoryModel categoryModel : categoryList) {
                List<NewsCategoryModel> childList = newsCategoryMapper.getCategoryModelList(Long.parseLong(categoryModel.getId()),type);
                if (childList != null && childList.size() > 0) {
                    categoryModel.setChildren(childList);
//                    categoryModel.setChecked(true);
                    categoryModel.setSpread(true);
                }
            }
            return JacksonMapper.newDataInstance(categoryList);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取列表异常";
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * 添加子节点
     *
     * @param pk
     * @return
     */
    @ResponseBody
    @RequestMapping("add")
    public JsonNode add(Long pk,String type) {
    	if(StringUtils.isEmpty(type)){
			type = "0";
		}
        if (null != pk) {
            PradNewsCategory newsCategory = new PradNewsCategory();
            newsCategory.setNcAdd1(type);
            newsCategory.setNcPid(pk);
            newsCategoryService.save(newsCategory);
            return JacksonMapper.newSuccessInstance();
        } else {
            String errMsg = "添加节点失败";
            LOGGER.error(errMsg);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * 获取要修改的节点信息
     *
     * @param pk
     * @return
     */
    @RequestMapping("getNode")
    public String getNode(Long pk, ModelMap mm) {
        if (null != pk) {
            PradNewsCategory category = newsCategoryService.getById(pk);
            mm.addAttribute("newsCategory", category);
            return "admin/news/newsCateUpdate";
        } else {
            String errMsg = "添加节点信息失败";
            LOGGER.error(errMsg);
            return "error/error";
        }
    }

    /**
     * 修改时获取所有父节点
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getParentNodeList")
    public JsonNode getParentNodeList(String type) {
        QueryWrapper<PradNewsCategory> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(PradNewsCategory::getNcPid, 0)
								.eq(PradNewsCategory::getNcAdd1,type);
            List<PradNewsCategory> pidList = newsCategoryService.list(queryWrapper);
            return JacksonMapper.newDataInstance(pidList);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "添加父节点列表失败";
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * 更新(添加或修改)节点
     *
     * @param category
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public JsonNode save(PradNewsCategory category) {
        try {
			if(StringUtils.isEmpty(category.getNcAdd1())){
				category.setNcAdd1("0");
			}
            if (category.getNcId() != null) {
                newsCategoryService.updateById(category);
            } else {
                newsCategoryService.save(category);
            }
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "更新失败";
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * 获取该节点下所有的资讯
     *
     * @param pk
     * @return
     */
    @ResponseBody
    @RequestMapping("getDelNews")
    public JsonNode getDelNews(Long pk) {
        QueryWrapper<PradNews> queryWrapper = new QueryWrapper<>();
        if (null != pk) {
            queryWrapper.lambda().eq(PradNews::getNeNcId, pk);
            List<PradNews> delNewsList = newsService.list(queryWrapper);
            return JacksonMapper.newCountInstance(delNewsList, delNewsList.size());
        }
        String errMsg = "获取节点下资讯列表失败";
        LOGGER.error(errMsg);
        return JacksonMapper.newErrorInstance(errMsg);
    }

    /**
     * 确认删除节点 并移除资讯对应的分类
     *
     * @param pk
     * @return
     */
    @ResponseBody
    @RequestMapping("del")
    public JsonNode del(Long pk) {
        if (null != pk) {
            newsCategoryService.removeById(pk);
			newsMapper.updateNewsByCateId(pk);
            return JacksonMapper.newSuccessInstance();
        } else {
            String errMsg = "删除失败";
            LOGGER.error(errMsg);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * 获取内容标签列表
     */
    @ResponseBody
    @RequestMapping("listTagAjax")
    public JsonNode listTagAjax(Long newsId) {
        String type = "1";
        String tagIds = "";
        try {
            if(newsId != null){
                PradNews news = newsService.getById(newsId);
                if(news != null && StringUtils.isNotEmpty(news.getNeTagIds())){
                    tagIds = news.getNeTagIds();
                }
            }
            List<NewsCategoryModel> categoryList = newsCategoryMapper.getCategoryModelList(0L,type);
            for (NewsCategoryModel categoryModel : categoryList) {
                if(StringUtils.isNotEmpty(tagIds)){
                    String[] tags = tagIds.split(",");
                    for(String tag:tags){
                        if(StringUtils.isNotEmpty(tag)){
                            if(tag.equals(categoryModel.getId())){
                                categoryModel.setChecked(true);
                            }
                        }
                    }
                }

                List<NewsCategoryModel> childList = newsCategoryMapper.getCategoryModelList(Long.parseLong(categoryModel.getId()),type);
                if (childList != null && childList.size() > 0) {
                    if(StringUtils.isNotEmpty(tagIds)){
                        String[] tags = tagIds.split(",");
                        for(NewsCategoryModel child:childList){
                            for(String tag:tags){
                                if(StringUtils.isNotEmpty(tag)){
                                    if(tag.equals(child.getId())){
                                        child.setChecked(true);
                                    }
                                }
                            }
                        }
                    }
                    categoryModel.setChildren(childList);
                    categoryModel.setSpread(true);
                }
            }
            return JacksonMapper.newDataInstance(categoryList);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取列表异常";
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }


}
