package com.kingyee.prad.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.excel.ExcelData;
import com.kingyee.common.excel.ExcelUtils;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.common.Const;
import com.kingyee.prad.entity.WechatUser;
import com.kingyee.prad.service.IWechatUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("admin/weChatUser")
public class AdminWeChatUserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(AdminWeChatUserController.class);
    @Autowired
    private IWechatUserService wechatUserService;

    @RequestMapping("list")
    public String list(ModelMap mm) {
        mm.addAttribute("menu", "微信用户");
        return "admin/user/weChatUser/list";
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
    @RequestMapping("listAjax")
    public JsonNode listAjax(Integer page, Integer limit, String keyword) {
        if (limit == null) {
            limit = Const.ADMIN_ROWSPERPAGE_MORE;
        }
        if (page == null) {
            page = 1;
        }
        try {
            IPage<WechatUser> pageInfo = new Page<>(page, limit);
            QueryWrapper<WechatUser> queryWrapper = new QueryWrapper<WechatUser>();
            queryWrapper.lambda()
                    .like(StringUtils.isNotEmpty(keyword), WechatUser::getWuNickname, keyword);
            pageInfo = wechatUserService.page(pageInfo, queryWrapper);
            return JacksonMapper.newCountInstance(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取微信用户列表出错";
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * weChat user list export
     *
     * @param response
     * @param keyword
     */
    @RequestMapping(value = "export")
    public void export(HttpServletResponse response, String keyword) {
        try {
            QueryWrapper<WechatUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().like(StringUtils.isNotEmpty(keyword), WechatUser::getWuNickname, keyword);
            List<WechatUser> list = wechatUserService.list(queryWrapper);
            ExcelData excelData = wechatUserService.exportUserExcel(list);
            String fileName = "安斯泰来微信用户导出" + TimeUtil.longToString(System.currentTimeMillis(), "yyyy-MM-dd") + ".xlsx";
            ExcelUtils.exportExcel(response, fileName, excelData);
        } catch (Exception e) {
            LOGGER.error("微信用户导出时" + e.getMessage(), e);
            try {
                e.printStackTrace(response.getWriter());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
