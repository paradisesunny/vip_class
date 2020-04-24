package com.kingyee.prad.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.prad.common.Const;
import com.kingyee.prad.common.security.AdminUserUtil;
import com.kingyee.prad.entity.Hospital;
import com.kingyee.prad.entity.PradExpert;
import com.kingyee.prad.entity.PradNewsCategory;
import com.kingyee.prad.service.IHospitalService;
import com.kingyee.prad.service.IPradExpertService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nmy
 * @date 2020/3/10
 * @desc 后台-专家管理
 */
@Controller
@RequestMapping(value = "/admin/expert")
public class AdminExpertController {
    private static final Logger logger = LoggerFactory.getLogger(AdminExpertController.class);
    @Autowired
    private IPradExpertService expertService;
	@Autowired
	private IHospitalService hospitalService;

    /**
     * list
     * @return
     */
    @RequestMapping("/list")
    public String list(ModelMap mm) {
        try {
            mm.addAttribute("category", new PradNewsCategory());
            mm.addAttribute("catePk", 4l);
            return "admin/expert/list";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询专家列表参数不完整");
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
	public JsonNode listAjax(Integer page, Integer limit, String keyword, Long catePk) {
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = Const.ADMIN_ROWSPERPAGE_MORE;
		}
		IPage<PradExpert> pageInfo = new Page<>(page, limit);
		QueryWrapper<PradExpert> queryWrapper = new QueryWrapper<>();
		try {
			queryWrapper.lambda().like(StringUtils.isNotEmpty(keyword), PradExpert::getPeName, keyword);
			pageInfo = expertService.page(pageInfo, queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg = "获取专家列表时出错";
			logger.error(errMsg + e);
			return JacksonMapper.newErrorInstance(errMsg);
		}
		return JacksonMapper.newCountInstance(pageInfo);
	}

	/**
	 * go to edit or add page
	 * @param mm
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(ModelMap mm, Long id) {
		try {
			PradExpert expert = expertService.getById(id);
			mm.addAttribute("expert", expert);
			mm.addAttribute("category", new PradNewsCategory());
			mm.addAttribute("catePk", 4l);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("编辑专家信息时异常", e);
		}
		return "admin/expert/expertEdit";
	}


	/**
	 * update and save
	 * @param expert
	 * @return
	 */
	@RequestMapping(value = "/save")
	public String save(PradExpert expert) {
		try {
			if (expert.getPeId() == null) {
				expert.setPeCreateTime(System.currentTimeMillis());
				expert.setPeCreateUserId(AdminUserUtil.getUserId());
				expert.setPeCreateUserName(AdminUserUtil.getShowName());
				expertService.save(expert);
			} else {
				expert.setPeUpdateTime(System.currentTimeMillis());
				expert.setPeUpdateUserId(AdminUserUtil.getUserId());
				expert.setPeUpdateUserName(AdminUserUtil.getShowName());
				expertService.updateById(expert);
			}
			return "redirect:list";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存专家异常", e);
			return "error/error";
		}
	}

	/**
	 * delete by id
	 * @param pk
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public JsonNode delete(Long pk) {
		try {
			PradExpert expert = expertService.getById(pk);
			expertService.removeById(expert);
			//解除资讯与专家的关联
			//TODO
			return JacksonMapper.newSuccessInstance();
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg = "删除专家异常";
			logger.error(errMsg, e);
			return JacksonMapper.newErrorInstance(errMsg);
		}
	}


	/**
	 * 地区联动
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCityOrRegionAjax", method = RequestMethod.GET)
	public JsonNode getCityOrRegionAjax(Long id,Integer type) {
		try {
			Map<String,Object> result = new HashMap<>();
			QueryWrapper<Hospital> wrapper = new QueryWrapper<>();
			List<Hospital> hospitalList = null;
			List<Hospital> regionList = null;
			List<Hospital> cityList = null;
			if(type == 1){
				//市
				wrapper.lambda().eq(Hospital::getParentId,id);
				cityList = hospitalService.list(wrapper);
				id = cityList.get(0).getId();
				//区
				wrapper = new QueryWrapper<>();
				wrapper.lambda().eq(Hospital::getParentId,id);
				regionList = hospitalService.list(wrapper);
				id = regionList.get(0).getId();
			}else if(type == 2){
				//区
				wrapper = new QueryWrapper<>();
				wrapper.lambda().eq(Hospital::getParentId,id);
				regionList = hospitalService.list(wrapper);
			}
			//医院
			wrapper = new QueryWrapper<>();
			wrapper.lambda().eq(Hospital::getParentId,id);
			hospitalList = hospitalService.list(wrapper);
			result.put("cityList",cityList);
			result.put("regionList",regionList);
			result.put("hospitalList",hospitalList);
			return JacksonMapper.newDataInstance(result);
		} catch (Exception e) {
			e.printStackTrace();
			String errMsg = "地区联动异常";
			logger.error(errMsg, e);
			return JacksonMapper.newErrorInstance(errMsg);
		}
	}


}
