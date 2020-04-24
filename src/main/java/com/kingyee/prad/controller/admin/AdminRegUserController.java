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
import com.kingyee.prad.entity.Hospital;
import com.kingyee.prad.entity.PradUser;
import com.kingyee.prad.service.IPradUserService;
import com.kingyee.prad.service.impl.HospitalServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("admin/regUser")
public class AdminRegUserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(AdminRegUserController.class);
    @Autowired
    private IPradUserService userService;
    @Autowired
    private HospitalServiceImpl hospitalService;

    /**
     * register user list
     *
     * @param mm
     * @return
     */
    @RequestMapping("list")
    public String list(ModelMap mm) {
        mm.addAttribute("menu", "注册用户");
        return "admin/user/regUser/list";
    }

    /**
     * register user ajax request
     *
     * @param page
     * @param limit
     * @param keyword
     * @return
     */
    @ResponseBody
    @RequestMapping("listAjax")
    public JsonNode listAjax(Integer page, Integer limit, String keyword) {
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = Const.ADMIN_ROWSPERPAGE_MORE;
        }
        try {
            IPage<PradUser> pageInfo = new Page<>(page, limit);
            QueryWrapper<PradUser> queryWrapper = new QueryWrapper<PradUser>();
            queryWrapper.lambda()
                    .like(StringUtils.isNotEmpty(keyword), PradUser::getPuName, keyword)
                    .or()
                    .like(StringUtils.isNotEmpty(keyword), PradUser::getPuPhone, keyword);
            pageInfo = userService.page(pageInfo, queryWrapper);
            return JacksonMapper.newCountInstance(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "获取注册用户列表出错";
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * go to edit register user page
     *
     * @param mm
     * @param id
     * @return
     */
    @RequestMapping("edit")
    public String edit(ModelMap mm, String id) {
        mm.addAttribute("menu", "注册用户");
        try {
            PradUser regUser = userService.getById(id);
            mm.addAttribute("regUser", regUser);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("编辑注册用户异常", e);
        }
        return "admin/user/regUser/detail";
    }

    /**
     * update and save register user
     *
     * @param regUser
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "save")
    public JsonNode save(PradUser regUser) {
        try {
            regUser.setPuUpdateTime(TimeUtil.dateToLong());
            userService.updateById(regUser);
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "保存注册用户信息异常。";
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * delete register user by id
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "del", method = RequestMethod.GET)
    public JsonNode del(String id) {
        try {
            PradUser regUser = userService.getById(id);
            userService.removeById(regUser);
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = "删除注册用户信息异常";
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * register user list export
     *
     * @param response
     * @param keyword
     */
    @RequestMapping(value = "export")
    public void export(HttpServletResponse response, String keyword) {
        try {
            QueryWrapper<PradUser> queryWrapper = new QueryWrapper<PradUser>();
            queryWrapper.lambda().like(StringUtils.isNotEmpty(keyword), PradUser::getPuName, keyword);
            List<PradUser> list = userService.list(queryWrapper);
            ExcelData excelData = userService.exportUserExcel(list);
            String fileName = "安斯泰来注册用户导出" + TimeUtil.longToString(System.currentTimeMillis(), "yyyy-MM-dd") + ".xlsx";
            ExcelUtils.exportExcel(response, fileName, excelData);
        } catch (Exception e) {
            LOGGER.error("注册用户导出时" + e.getMessage(), e);
            try {
                e.printStackTrace(response.getWriter());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 根据父id查找县市级医院
     *
     * @param fatherId:当fatherId为0时显示所有县 不为0时逐级查询
     * @return
     */
    @ResponseBody
    @RequestMapping("getChild")
    public JsonNode getChild(Long fatherId) {
        try {
            QueryWrapper<Hospital> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Hospital::getParentId, fatherId);
            List<Hospital> list = hospitalService.list(queryWrapper);
            return JacksonMapper.newDataInstance(list);
        } catch (Exception e) {
            String errMsg = "获取子节点失败";
            e.printStackTrace();
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

}
