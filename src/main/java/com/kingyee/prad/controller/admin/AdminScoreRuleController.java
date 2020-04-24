package com.kingyee.prad.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.prad.common.Const;
import com.kingyee.prad.entity.PradScoreRule;
import com.kingyee.prad.service.IPradScoreRuleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 积分规则管理
 *
 */
@Controller
@RequestMapping("admin/score")
public class AdminScoreRuleController {
    private final static Logger logger = LoggerFactory.getLogger(AdminScoreRuleController.class);
    @Autowired
    private IPradScoreRuleService scoreRuleService;


    /**
     * 积分规则
     * @return
     */
    @RequestMapping("listRule")
    public String listRule() {
        return "admin/score/rule/list";
    }

    /**
     * weChat user ajax request
     *
     * @param page
     * @param limit
     * @param keyword
     * @return
     */
    @ResponseBody
    @RequestMapping("listRuleAjax")
    public JsonNode listRuleAjax(Integer page, Integer limit, String keyword) {
        if (limit == null) {
            limit = Const.ADMIN_ROWSPERPAGE_MORE;
        }
        if (page == null) {
            page = 1;
        }
        try {
            IPage<PradScoreRule> pageInfo = new Page<>(page, limit);
            QueryWrapper<PradScoreRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .like(StringUtils.isNotEmpty(keyword), PradScoreRule::getSrName, keyword);
            pageInfo = scoreRuleService.page(pageInfo, queryWrapper);
            return JacksonMapper.newCountInstance(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取积分规则列表出错";
            logger.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * go to edit or add page
     *
     * @param mm
     * @return
     */
    @RequestMapping("/editRule")
    public String editRule(ModelMap mm, Long ruleId) {
        try {
            if (ruleId != null) {
                PradScoreRule rule = scoreRuleService.getById(ruleId);
                mm.addAttribute("rule", rule);
                mm.addAttribute("title", "规则编辑");
            } else {
                mm.addAttribute("title", "规则添加");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("编辑积分规则时异常", e);
            return "error/error";
        }
        return "admin/score/rule/detail";
    }


    /**
     * update and save
     * @param rule
     * @return
     */
    @RequestMapping(value = "/saveRule")
    public String saveRule(PradScoreRule rule) {
        try {
            if (rule.getSrId() == null) {
                // 新增
                rule.setSrCreateTime(System.currentTimeMillis());
                scoreRuleService.save(rule);
            } else {
                // 修改
                rule.setSrUpdateTime(System.currentTimeMillis());
                scoreRuleService.updateById(rule);
            }
            return "redirect:listRule";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存规则异常", e);
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
    @RequestMapping(value = "/deleteRule", method = RequestMethod.GET)
    public JsonNode deleteRule(Long pk) {
        try {
            PradScoreRule rule = scoreRuleService.getById(pk);
            scoreRuleService.removeById(rule);
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "删除规则异常";
            logger.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

}
