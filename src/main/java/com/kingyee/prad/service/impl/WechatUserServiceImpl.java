package com.kingyee.prad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingyee.common.excel.ExcelData;
import com.kingyee.common.spring.mvc.WebUtil;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.common.util.http.HttpUtil;
import com.kingyee.prad.common.Const;
import com.kingyee.prad.entity.WechatUser;
import com.kingyee.prad.mapper.WechatUserMapper;
import com.kingyee.prad.service.IWechatUserService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 微信用户表 服务实现类
 * </p>
 *
 * @author zhl
 * @since 2019-11-28
 */
@Service
public class WechatUserServiceImpl extends ServiceImpl<WechatUserMapper, WechatUser> implements IWechatUserService {
    private final static Logger logger = LoggerFactory.getLogger(WechatUserServiceImpl.class);

    @Autowired
    private WechatUserMapper wechatUserMapper;

    /**
     * 根据openid取得微信用户信息
     *
     * @param openid
     */
    public WechatUser getWechatUserByOpenid(String openid) {
        if (StringUtils.isEmpty(openid)) {
            return null;
        }
        LambdaQueryWrapper<WechatUser> lambdaQueryWrapper = new LambdaQueryWrapper<WechatUser>().eq(WechatUser::getWuOpenid, openid);
        return wechatUserMapper.selectOne(lambdaQueryWrapper);
    }

    /**
     * 保存或新增微信用户
     *
     * @param wxMpUser
     * , String source
     * @return
     * @throws Exception
     */
    public Long saveOrUpdateWechatUser(WxMpUser wxMpUser) throws Exception {
        WechatUser wechatUser = getWechatUserByOpenid(wxMpUser.getOpenId());
        Long now = TimeUtil.dateToLong();
        boolean isExsit = true;
        if (wechatUser == null) {
            isExsit = false;
            wechatUser = new WechatUser();
            wechatUser.setWuCreateTime(now);
//            wechatUser.setWuSource(source);
        }
        wechatUser.setWuUpdateTime(now);
        if (StringUtils.isNotEmpty(wxMpUser.getHeadImgUrl())) {
            wechatUser.setWuHeadimgurl(wxMpUser.getHeadImgUrl());
            wechatUser.setWuHeadimg(getHeadImgPath(wxMpUser.getOpenId(), wxMpUser.getHeadImgUrl()));
        }
        wechatUser.setWuSubscribe(wxMpUser.getSubscribe() ? 1 : 0);
        wechatUser.setWuOpenid(wxMpUser.getOpenId());
        wechatUser.setWuNickname(wxMpUser.getNickname());
        wechatUser.setWuSex(wxMpUser.getSexDesc());
        wechatUser.setWuCity(wxMpUser.getCity());
        wechatUser.setWuCountry(wxMpUser.getCountry());
        wechatUser.setWuProvince(wxMpUser.getProvince());
        wechatUser.setWuLanguage(wxMpUser.getLanguage());
        wechatUser.setWuSubscribeTime(wxMpUser.getSubscribeTime() * 1000L);
        wechatUser.setWuUnionid(wxMpUser.getUnionId());
        wechatUser.setWuRemark(wxMpUser.getRemark());
        if (wxMpUser.getGroupId() != null) {
            wechatUser.setWuGroupid(wxMpUser.getGroupId().toString());
        }
        if (wxMpUser.getTagIds() != null && wxMpUser.getTagIds().length > 0) {
            wechatUser.setWuTagidList(StringUtils.join(wxMpUser.getTagIds(), ","));
        }
        long num;
        if (isExsit) {
            num = wechatUserMapper.updateById(wechatUser);
        } else {
            num = wechatUserMapper.insert(wechatUser);
        }
        return num;
    }

    /**
     * 下载用户头像
     *
     * @param openid
     * @param headImgUrl
     * @return
     */
    private String getHeadImgPath(String openid, String headImgUrl) {
        String headImgPath = Const.HEADIMG_PATH + openid + ".jpeg";
        String path = WebUtil.getRealPath(Const.HEADIMG_PATH);
        try {
            HttpUtil.downloadPicture(headImgUrl, path, openid + ".jpeg");
            return headImgPath;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("openid为：" + openid + "的用户头像下载出错！");
            return null;
        }
    }

    @Override
    public ExcelData exportUserExcel(List<WechatUser> userList) {
        ExcelData data = new ExcelData();
        data.setName("用户信息");
        List<String> titles = Arrays.asList("微信用户表主键", "类型", "openid", "昵称", "性别", "城市", "国家", "省份", "语言", "头像", "unionid",
                "备注", "头像本地路径", "用户分组ID", "标签ID列表", "是否订阅该公众号标识（0：未关注；1：关注）", "关注时间", "创建时间", "更新时间", "来源(1:翼多会议)");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList<>();
        if (userList != null && userList.size() > 0) {
            for (WechatUser user : userList) {
                List<Object> row = new ArrayList<>();
                row.add(user.getWuId());
                row.add(user.getWuUserType());
                row.add(user.getWuOpenid());
                row.add(user.getWuNickname());
                row.add(user.getWuSex());
                row.add(user.getWuCity());
                row.add(user.getWuCountry());
                row.add(user.getWuProvince());
                row.add(user.getWuLanguage());
                row.add(user.getWuHeadimg());
                row.add(user.getWuUnionid());
                row.add(user.getWuRemark());
                row.add(user.getWuHeadimg());
                row.add(user.getWuGroupid());
                row.add(user.getWuTagidList());
                row.add(user.getWuSubscribe());
                row.add(user.getWuSubscribeTime());
                row.add(TimeUtil.longToString(user.getWuCreateTime(), "yyyy-MM-dd"));
                row.add(TimeUtil.longToString(user.getWuUpdateTime(), "yyyy-MM-dd"));
                row.add(user.getWuSource());
                rows.add(row);
            }
        }
        data.setRows(rows);
        return data;
    }
}
