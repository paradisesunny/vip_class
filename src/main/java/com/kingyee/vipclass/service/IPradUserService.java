package com.kingyee.vipclass.service;

import com.kingyee.common.excel.ExcelData;
import com.kingyee.vipclass.entity.PradUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
public interface IPradUserService extends IService<PradUser> {
    ExcelData exportUserExcel(List<PradUser> userList);
	PradUser getUserByMedliveId(Long medUserId);
}
