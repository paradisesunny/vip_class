package com.kingyee.vipclass.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.excel.ExcelData;
import com.kingyee.common.excel.ExcelUtils;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.vipclass.common.Const;
import com.kingyee.vipclass.entity.PradVisitLog;
import com.kingyee.vipclass.service.impl.PradVisitLogServiceImpl;
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
@RequestMapping("admin/visitLog")
public class AdminVisitLogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminVisitLogController.class);
    @Autowired
    private PradVisitLogServiceImpl visitLogService;

    /**
     * visit log list
     *
     * @return
     */
    @RequestMapping("list")
    public String list(ModelMap mm) {
        mm.addAttribute("otherMenu", "访问记录日志");
        return "admin/visitLog/list";
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
    public JsonNode listAjax(Integer page, Integer limit, String keyword, String startTime, String endTime) {
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = Const.ADMIN_ROWSPERPAGE_MORE;
        }
        IPage<PradVisitLog> pageInfo = new Page<>(page, limit);
        try {
            QueryWrapper<PradVisitLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().between(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime), PradVisitLog::getVlStartTime, TimeUtil.stringToLong(startTime), TimeUtil.stringToLong(endTime))
                    .like(StringUtils.isNotEmpty(keyword), PradVisitLog::getVlUserId, keyword);
            pageInfo = visitLogService.page(pageInfo, queryWrapper);
        } catch (Exception e) {
            String errMsg = "查询访问记录日志出错";
            e.printStackTrace();
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
        return JacksonMapper.newCountInstance(pageInfo);
    }

    /**
     * visit log  user list export
     *
     * @param response
     */
    @RequestMapping(value = "export")
    public void export(HttpServletResponse response) {
        try {
            List<PradVisitLog> list = visitLogService.list();
            ExcelData excelData = visitLogService.exportLogExcel(list);
            String fileName = "安斯泰来访问日志导出" + TimeUtil.longToString(System.currentTimeMillis(), "yyyy-MM-dd") + ".xlsx";
            ExcelUtils.exportExcel(response, fileName, excelData);
        } catch (Exception e) {
            LOGGER.error("访问日志导出时" + e.getMessage(), e);
            try {
                e.printStackTrace(response.getWriter());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
