package com.kingyee.prad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 成长值规则表
 * </p>
 *
 * @author niumuyao
 * @since 2020-04-09
 */
public class PradScoreRule implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "sr_id", type = IdType.AUTO)
    private Long srId;

    /**
     * 规则编号
     */
    private Integer srNumber;

    /**
     * 规则名称
     */
    private String srName;

    /**
     * 奖励分值
     */
    private Integer srScore;

    /**
     * 创建时间
     */
    private Long srCreateTime;

    /**
     * 修改时间
     */
    private Long srUpdateTime;

    /**
     * 加分频次 1:只能一次 2:每个业务一次 3:不限次
     */
    private Integer srFrequency;
    /**
     * 类型
     */
    private String srType;


    public Long getSrId() {
        return srId;
    }

    public void setSrId(Long srId) {
        this.srId = srId;
    }

    public Integer getSrNumber() {
        return srNumber;
    }

    public void setSrNumber(Integer srNumber) {
        this.srNumber = srNumber;
    }

    public String getSrName() {
        return srName;
    }

    public void setSrName(String srName) {
        this.srName = srName;
    }

    public Integer getSrScore() {
        return srScore;
    }

    public void setSrScore(Integer srScore) {
        this.srScore = srScore;
    }

    public Long getSrCreateTime() {
        return srCreateTime;
    }

    public void setSrCreateTime(Long srCreateTime) {
        this.srCreateTime = srCreateTime;
    }

    public Long getSrUpdateTime() {
        return srUpdateTime;
    }

    public void setSrUpdateTime(Long srUpdateTime) {
        this.srUpdateTime = srUpdateTime;
    }

    public Integer getSrFrequency() {
        return srFrequency;
    }

    public void setSrFrequency(Integer srFrequency) {
        this.srFrequency = srFrequency;
    }

    public String getSrType() {
        return srType;
    }

    public void setSrType(String srType) {
        this.srType = srType;
    }

    @Override
    public String toString() {
        return "PradScoreRule{" +
        "srId=" + srId +
        ", srNumber=" + srNumber +
        ", srName=" + srName +
        ", srScore=" + srScore +
        ", srCreateTime=" + srCreateTime +
        ", srUpdateTime=" + srUpdateTime +
        ", srFrequency=" + srFrequency +
        ", srType=" + srType +
        "}";
    }

    @TableField(exist = false)
    private boolean isFinish;
    public boolean getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(boolean finish) {
        isFinish = finish;
    }
}
