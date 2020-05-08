package com.kingyee.vipclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 短信验证码
 * </p>
 *
 * @author Administrator
 * @since 2020-03-16
 */
public class SmsCode implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "sc_id", type = IdType.AUTO)
    private Long scId;

    /**
     * 邮箱
     */
    private String scEmail;

    /**
     * 手机号
     */
    private String scPhone;

    /**
     * 激活码
     */
    private String scCode;

    /**
     * 类型
     */
    private String scType;

    /**
     * 失效时间
     */
    private Long scInvalidDate;

    /**
     * 创建时间
     */
    private Long scCreateDate;

    /**
     * 是否使用(0:否1:是)
     */
    private Integer scIsUsed;

    /**
     * 用户id
     */
    private Long scUserId;

    /**
     * IP地址
     */
    private String scIpAddress;

    /**
     * 发送提示
     */
    private String scSendPrompt;

    /**
     * 发送标志（0：失败；1：成功）
     */
    private Integer scSendFlag;

    public String getScEmail() {
        return scEmail;
    }

    public void setScEmail(String scEmail) {
        this.scEmail = scEmail;
    }

    public Long getScId() {
        return scId;
    }

    public void setScId(Long scId) {
        this.scId = scId;
    }

    public String getScPhone() {
        return scPhone;
    }

    public void setScPhone(String scPhone) {
        this.scPhone = scPhone;
    }

    public String getScCode() {
        return scCode;
    }

    public void setScCode(String scCode) {
        this.scCode = scCode;
    }

    public String getScType() {
        return scType;
    }

    public void setScType(String scType) {
        this.scType = scType;
    }

    public Long getScInvalidDate() {
        return scInvalidDate;
    }

    public void setScInvalidDate(Long scInvalidDate) {
        this.scInvalidDate = scInvalidDate;
    }

    public Long getScCreateDate() {
        return scCreateDate;
    }

    public void setScCreateDate(Long scCreateDate) {
        this.scCreateDate = scCreateDate;
    }

    public Integer getScIsUsed() {
        return scIsUsed;
    }

    public void setScIsUsed(Integer scIsUsed) {
        this.scIsUsed = scIsUsed;
    }

    public Long getScUserId() {
        return scUserId;
    }

    public void setScUserId(Long scUserId) {
        this.scUserId = scUserId;
    }

    public String getScIpAddress() {
        return scIpAddress;
    }

    public void setScIpAddress(String scIpAddress) {
        this.scIpAddress = scIpAddress;
    }

    public String getScSendPrompt() {
        return scSendPrompt;
    }

    public void setScSendPrompt(String scSendPrompt) {
        this.scSendPrompt = scSendPrompt;
    }

    public Integer getScSendFlag() {
        return scSendFlag;
    }

    public void setScSendFlag(Integer scSendFlag) {
        this.scSendFlag = scSendFlag;
    }

    @Override
    public String toString() {
        return "SmsCode{" +
        "scId=" + scId +
        ", scPhone=" + scPhone +
        ", scCode=" + scCode +
        ", scType=" + scType +
        ", scInvalidDate=" + scInvalidDate +
        ", scCreateDate=" + scCreateDate +
        ", scIsUsed=" + scIsUsed +
        ", scUserId=" + scUserId +
        ", scIpAddress=" + scIpAddress +
        ", scSendPrompt=" + scSendPrompt +
        ", scSendFlag=" + scSendFlag +
        "}";
    }
}
