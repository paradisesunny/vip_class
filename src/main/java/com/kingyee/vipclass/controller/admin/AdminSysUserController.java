package com.kingyee.vipclass.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.EncryptUtil;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.vipclass.common.Const;
import com.kingyee.vipclass.entity.SysUser;
import com.kingyee.vipclass.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/admin/sysUser")
public class AdminSysUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminSysUserController.class);
    @Autowired
    private ISysUserService sysUserService;

    /**
     * system user list
     *
     * @return
     */
    @RequestMapping("/list")
    public String list(ModelMap mm) {
        mm.addAttribute("menu", "系统用户");
        return "admin/user/sysUser/list";
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
        IPage<SysUser> pageInfo = new Page<>(page, limit);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
        queryWrapper.lambda().like(StringUtils.isNotEmpty(keyword), SysUser::getSuLoginName, keyword);
        pageInfo = sysUserService.page(pageInfo, queryWrapper);
        return JacksonMapper.newCountInstance(pageInfo);
    }

    /**
     * go to edit page
     *
     * @param mm
     * @param id
     * @return
     */
    @RequestMapping("/edit")
    public String edit(ModelMap mm, String id) {
        try {
            if (StringUtils.isNotEmpty(id)) {
                SysUser sysUser = sysUserService.getById(id);
                mm.addAttribute("operate", "系统管理员-编辑");
                mm.addAttribute("sysUser", sysUser);
            } else {
                mm.addAttribute("operate", "系统管理员-添加");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("编辑系统用户异常", e);
        }
        mm.addAttribute("menu", "系统用户");
        return "admin/user/sysUser/detail";
    }

    /**
     * delete by id
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public JsonNode del(String id) {
        try {
            SysUser sysUser = sysUserService.getById(id);
            sysUserService.removeById(sysUser);
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "删除信息异常";
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * update and save
     *
     * @param sysUser
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save")
    public JsonNode save(SysUser sysUser) {
        try {
            Long now = TimeUtil.dateToLong();
            if (sysUser.getSuId() == null) {
                if(StringUtils.isEmpty(sysUser.getSuPassword())){
                    return JacksonMapper.newErrorInstance("请输入密码");
                }
                // 新增
                sysUser.setSuPassword(EncryptUtil.getSHA256Value(sysUser.getSuPassword()));
                sysUser.setSuCreateTime(now);
                sysUserService.save(sysUser);
            } else {
                // 修改
                if(StringUtils.isNotEmpty(sysUser.getSuPassword())){
                    sysUser.setSuPassword(EncryptUtil.getSHA256Value(sysUser.getSuPassword()));
                    sysUser.setSuUpdateTime(now);
                    sysUserService.updateById(sysUser);
                }
            }
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "保存系统用户信息异常。";
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

}