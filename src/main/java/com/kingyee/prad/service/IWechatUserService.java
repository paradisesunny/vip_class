package com.kingyee.prad.service;

import com.kingyee.common.excel.ExcelData;
import com.kingyee.prad.entity.WechatUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 微信用户表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2019-11-28
 */
public interface IWechatUserService extends IService<WechatUser> {
    ExcelData exportUserExcel(List<WechatUser> userList);
}
