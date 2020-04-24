package com.kingyee.prad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kingyee.common.util.TimeUtil;

import java.io.Serializable;

/**
 * <p>
 * 成长值记录明细表
 * </p>
 *
 * @author niumuyao
 * @since 2020-04-09
 */
public class PradScoreTransaction implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "st_id", type = IdType.AUTO)
    private Long stId;

    /**
     * 用户ID
     */
    private Long stUserId;

    /**
     * 用户名
     */
    private String stUserName;

    /**
     * 规则id
     */
    private Long stRuleId;

    /**
     * 本次加的分数
     */
    private Integer stScore;

    /**
     * 交易类型
     */
    private String stType;

    /**
     * 创建时间
     */
    private Long stCreateTime;


    public Long getStId() {
        return stId;
    }

    public void setStId(Long stId) {
        this.stId = stId;
    }

    public Long getStUserId() {
        return stUserId;
    }

    public void setStUserId(Long stUserId) {
        this.stUserId = stUserId;
    }

    public String getStUserName() {
        return stUserName;
    }

    public void setStUserName(String stUserName) {
        this.stUserName = stUserName;
    }

    public Long getStRuleId() {
        return stRuleId;
    }

    public void setStRuleId(Long stRuleId) {
        this.stRuleId = stRuleId;
    }

    public Integer getStScore() {
        return stScore;
    }

    public void setStScore(Integer stScore) {
        this.stScore = stScore;
    }

    public String getStType() {
        return stType;
    }

    public void setStType(String stType) {
        this.stType = stType;
    }

    public Long getStCreateTime() {
        return stCreateTime;
    }

    public void setStCreateTime(Long stCreateTime) {
        this.stCreateTime = stCreateTime;
    }

    @Override
    public String toString() {
        return "PradScoreTransaction{" +
        "stId=" + stId +
        ", stUserId=" + stUserId +
        ", stUserName=" + stUserName +
        ", stRuleId=" + stRuleId +
        ", stScore=" + stScore +
        ", stType=" + stType +
        ", stCreateTime=" + stCreateTime +
        "}";
    }

    @TableField(exist = false)
    private String stCreateTimeStr;
    @TableField(exist = false)
    private String stCreateTimeStrCN;

    public String getStCreateTimeStr() {
        stCreateTimeStr = TimeUtil.longToString(this.getStCreateTime(), TimeUtil.FORMAT_DATETIME_FULL);
        return stCreateTimeStr;
    }

    public void setStCreateTimeStr(String stCreateTimeStr) {
        this.stCreateTimeStr = stCreateTimeStr;
    }

    public String getStCreateTimeStrCN() {
        stCreateTimeStrCN = TimeUtil.longToString(this.getStCreateTime(), TimeUtil.FORMAT_DATE_ONLY_DAY);
        StringBuilder sb = new StringBuilder(stCreateTimeStrCN);
//        sb.replace(4,5,"年");
//        sb.replace(7,8,"月");
        sb.replace(2,3,"月");
        stCreateTimeStrCN = sb.toString();
        stCreateTimeStrCN = stCreateTimeStrCN+"日";
        return stCreateTimeStrCN;
    }

    public void setStCreateTimeStrCN(String stCreateTimeStrCN) {
        this.stCreateTimeStrCN = stCreateTimeStrCN;
    }

    public static void main(String[] args) {
        String time = TimeUtil.longToString(1586747497630l,TimeUtil.FORMAT_DATE_ONLY_DAY);
        StringBuilder sb = new StringBuilder(time);
        sb.replace(2,3,"月");
        System.out.println(sb.toString());
    }
}
