package com.kingyee.vipclass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kingyee.common.excel.ExcelData;
import com.kingyee.vipclass.entity.PradVisitLog;

import java.util.List;

/**
 * <p>
 * 访问记录日志 服务类
 * </p>
 *
 * @author dxl
 * @since 2020-03-24
 */
public interface IPradVisitLogService extends IService<PradVisitLog> {
    ExcelData exportLogExcel(List<PradVisitLog> visitLogList);

}
