package com.kingyee.vipclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 微信用户表
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
public class WechatUser implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "wu_id", type = IdType.AUTO)
    private Long wuId;

    /**
     * 用户id
     */
    private Long wuUserId;

    /**
     * 用户类型(医生)
     */
    private Integer wuUserType;

    /**
     * openid
     */
    private String wuOpenid;

    /**
     * 昵称
     */
    private String wuNickname;

    /**
     * 性别
     */
    private String wuSex;

    /**
     * 城市
     */
    private String wuCity;

    /**
     * 国家
     */
    private String wuCountry;

    /**
     * 省份
     */
    private String wuProvince;

    /**
     * 语言
     */
    private String wuLanguage;

    /**
     * 头像
     */
    private String wuHeadimgurl;

    /**
     * unionid
     */
    private String wuUnionid;

    /**
     * 备注
     */
    private String wuRemark;

    /**
     * 头像本地路径
     */
    private String wuHeadimg;

    /**
     * 用户分组id
     */
    private String wuGroupid;

    /**
     * 标签ID列表
     */
    private String wuTagidList;

    /**
     * 是否订阅该公众号标识（0：未关注；1：关注）
     */
    private Integer wuSubscribe;

    /**
     * 关注时间
     */
    private Long wuSubscribeTime;

    /**
     * 创建时间
     */
    private Long wuCreateTime;

    /**
     * 修改时间
     */
    private Long wuUpdateTime;

    /**
     * 用户来源(1:翼多会议)
     */
    private String wuSource;


    public Long getWuId() {
        return wuId;
    }

    public void setWuId(Long wuId) {
        this.wuId = wuId;
    }

    public Long getWuUserId() {
        return wuUserId;
    }

    public void setWuUserId(Long wuUserId) {
        this.wuUserId = wuUserId;
    }

    public Integer getWuUserType() {
        return wuUserType;
    }

    public void setWuUserType(Integer wuUserType) {
        this.wuUserType = wuUserType;
    }

    public String getWuOpenid() {
        return wuOpenid;
    }

    public void setWuOpenid(String wuOpenid) {
        this.wuOpenid = wuOpenid;
    }

    public String getWuNickname() {
        return wuNickname;
    }

    public void setWuNickname(String wuNickname) {
        this.wuNickname = wuNickname;
    }

    public String getWuSex() {
        return wuSex;
    }

    public void setWuSex(String wuSex) {
        this.wuSex = wuSex;
    }

    public String getWuCity() {
        return wuCity;
    }

    public void setWuCity(String wuCity) {
        this.wuCity = wuCity;
    }

    public String getWuCountry() {
        return wuCountry;
    }

    public void setWuCountry(String wuCountry) {
        this.wuCountry = wuCountry;
    }

    public String getWuProvince() {
        return wuProvince;
    }

    public void setWuProvince(String wuProvince) {
        this.wuProvince = wuProvince;
    }

    public String getWuLanguage() {
        return wuLanguage;
    }

    public void setWuLanguage(String wuLanguage) {
        this.wuLanguage = wuLanguage;
    }

    public String getWuHeadimgurl() {
        return wuHeadimgurl;
    }

    public void setWuHeadimgurl(String wuHeadimgurl) {
        this.wuHeadimgurl = wuHeadimgurl;
    }

    public String getWuUnionid() {
        return wuUnionid;
    }

    public void setWuUnionid(String wuUnionid) {
        this.wuUnionid = wuUnionid;
    }

    public String getWuRemark() {
        return wuRemark;
    }

    public void setWuRemark(String wuRemark) {
        this.wuRemark = wuRemark;
    }

    public String getWuHeadimg() {
        return wuHeadimg;
    }

    public void setWuHeadimg(String wuHeadimg) {
        this.wuHeadimg = wuHeadimg;
    }

    public String getWuGroupid() {
        return wuGroupid;
    }

    public void setWuGroupid(String wuGroupid) {
        this.wuGroupid = wuGroupid;
    }

    public String getWuTagidList() {
        return wuTagidList;
    }

    public void setWuTagidList(String wuTagidList) {
        this.wuTagidList = wuTagidList;
    }

    public Integer getWuSubscribe() {
        return wuSubscribe;
    }

    public void setWuSubscribe(Integer wuSubscribe) {
        this.wuSubscribe = wuSubscribe;
    }

    public Long getWuSubscribeTime() {
        return wuSubscribeTime;
    }

    public void setWuSubscribeTime(Long wuSubscribeTime) {
        this.wuSubscribeTime = wuSubscribeTime;
    }

    public Long getWuCreateTime() {
        return wuCreateTime;
    }

    public void setWuCreateTime(Long wuCreateTime) {
        this.wuCreateTime = wuCreateTime;
    }

    public Long getWuUpdateTime() {
        return wuUpdateTime;
    }

    public void setWuUpdateTime(Long wuUpdateTime) {
        this.wuUpdateTime = wuUpdateTime;
    }

    public String getWuSource() {
        return wuSource;
    }

    public void setWuSource(String wuSource) {
        this.wuSource = wuSource;
    }

    @Override
    public String toString() {
        return "WechatUser{" +
        "wuId=" + wuId +
        ", wuUserId=" + wuUserId +
        ", wuUserType=" + wuUserType +
        ", wuOpenid=" + wuOpenid +
        ", wuNickname=" + wuNickname +
        ", wuSex=" + wuSex +
        ", wuCity=" + wuCity +
        ", wuCountry=" + wuCountry +
        ", wuProvince=" + wuProvince +
        ", wuLanguage=" + wuLanguage +
        ", wuHeadimgurl=" + wuHeadimgurl +
        ", wuUnionid=" + wuUnionid +
        ", wuRemark=" + wuRemark +
        ", wuHeadimg=" + wuHeadimg +
        ", wuGroupid=" + wuGroupid +
        ", wuTagidList=" + wuTagidList +
        ", wuSubscribe=" + wuSubscribe +
        ", wuSubscribeTime=" + wuSubscribeTime +
        ", wuCreateTime=" + wuCreateTime +
        ", wuUpdateTime=" + wuUpdateTime +
        ", wuSource=" + wuSource +
        "}";
    }
}
