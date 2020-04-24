package com.kingyee.prad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 浏览记录表
 * </p>
 *
 * @author Administrator
 * @since 2020-03-18
 */
public class PradViewRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "vr_id", type = IdType.AUTO)
    private Long vrId;

    /**
     * 用户主键
     */
    private Long vrUserId;

    /**
     * 课程主键
     */
    private Long vrNewsId;

    /**
     * 浏览时间
     */
    private Long vrTime;

    public Long getVrId() {
        return vrId;
    }

    public void setVrId(Long vrId) {
        this.vrId = vrId;
    }

    public Long getVrUserId() {
        return vrUserId;
    }

    public void setVrUserId(Long vrUserId) {
        this.vrUserId = vrUserId;
    }

    public Long getVrNewsId() {
        return vrNewsId;
    }

    public void setVrNewsId(Long vrNewsId) {
        this.vrNewsId = vrNewsId;
    }

    public Long getVrTime() {
        return vrTime;
    }

    public void setVrTime(Long vrTime) {
        this.vrTime = vrTime;
    }

    @Override
    public String toString() {
        return "PradViewRecord{" +
                "vrId=" + vrId +
                ", vrUserId=" + vrUserId +
                ", vrNewsId=" + vrNewsId +
                ", vrTime=" + vrTime +
                '}';
    }
}
