package com.kingyee.prad.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.prad.common.Const;
import com.kingyee.prad.entity.PradScoreTransaction;
import com.kingyee.prad.entity.PradUser;
import com.kingyee.prad.service.IPradScoreTransactionService;
import com.kingyee.prad.service.IPradUserService;
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
 * 用户积分明细管理
 *
 */
@Controller
@RequestMapping("admin/userScore")
public class AdminUserScoreController {
    private final static Logger logger = LoggerFactory.getLogger(AdminUserScoreController.class);
    @Autowired
    private IPradScoreTransactionService transactionService;
    @Autowired
    private IPradUserService userService;


    /**
     * 用户积分明细
     * @return
     */
    @RequestMapping("list")
    public String list(ModelMap mm, Long userId) {
        PradUser user = userService.getById(userId);
        mm.addAttribute("user",user);
        return "admin/score/userScore/scoreList";
    }

    /**
     * weChat user ajax request
     *
     * @param page
     * @param limit
     * @param keyword
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping("listUserScoreAjax")
    public JsonNode listUserAjax(Integer page, Integer limit, String keyword, Long userId) {
        if (limit == null) {
            limit = Const.ADMIN_ROWSPERPAGE_MORE;
        }
        if (page == null) {
            page = 1;
        }
        try {
            IPage<PradScoreTransaction> pageInfo = new Page<>(page, limit);
            QueryWrapper<PradScoreTransaction> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(PradScoreTransaction::getStUserId, userId)
                    .like(StringUtils.isNotEmpty(keyword), PradScoreTransaction::getStUserName, keyword)
                    .or().like(StringUtils.isNotEmpty(keyword), PradScoreTransaction::getStType, keyword)
                    .orderByDesc(PradScoreTransaction::getStCreateTime);
            pageInfo = transactionService.page(pageInfo, queryWrapper);
            return JacksonMapper.newCountInstance(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取用户积分详情列表出错";
            logger.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * delete by id
     *
     * @param pk
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteUserScoreInfo", method = RequestMethod.GET)
    public JsonNode deleteUserScoreInfo(Long pk) {
        try {
            PradScoreTransaction info = transactionService.getById(pk);
            transactionService.removeById(info);
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "删除用户积分异常";
            logger.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

}
