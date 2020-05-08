package com.kingyee.vipclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
public class SysUser implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "su_id", type = IdType.AUTO)
    private Long suId;

    /**
     * 用户名
     */
    private String suLoginName;

    /**
     * 密码
     */
    private String suPassword;

    /**
     * 创建时间
     */
    private Long suCreateTime;

    /**
     * 修改时间
     */
    private Long suUpdateTime;


    public Long getSuId() {
        return suId;
    }

    public void setSuId(Long suId) {
        this.suId = suId;
    }

    public String getSuLoginName() {
        return suLoginName;
    }

    public void setSuLoginName(String suLoginName) {
        this.suLoginName = suLoginName;
    }

    public String getSuPassword() {
        return suPassword;
    }

    public void setSuPassword(String suPassword) {
        this.suPassword = suPassword;
    }

    public Long getSuCreateTime() {
        return suCreateTime;
    }

    public void setSuCreateTime(Long suCreateTime) {
        this.suCreateTime = suCreateTime;
    }

    public Long getSuUpdateTime() {
        return suUpdateTime;
    }

    public void setSuUpdateTime(Long suUpdateTime) {
        this.suUpdateTime = suUpdateTime;
    }

    @Override
    public String toString() {
        return "SysUser{" +
        "suId=" + suId +
        ", suLoginName=" + suLoginName +
        ", suPassword=" + suPassword +
        ", suCreateTime=" + suCreateTime +
        ", suUpdateTime=" + suUpdateTime +
        "}";
    }
}
