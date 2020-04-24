package com.kingyee.prad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 成长值表
 * </p>
 *
 * @author niumuyao
 * @since 2020-04-09
 */
public class PradScore implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "ps_id", type = IdType.AUTO)
    private Long psId;

    /**
     * 用户主键
     */
    private Long psUserId;

    /**
     * 总分数
     */
    private Integer psTotalScore;

    /**
     * 时间
     */
    private Long psUpdateTime;


    public Long getPsId() {
        return psId;
    }

    public void setPsId(Long psId) {
        this.psId = psId;
    }

    public Long getPsUserId() {
        return psUserId;
    }

    public void setPsUserId(Long psUserId) {
        this.psUserId = psUserId;
    }

    public Integer getPsTotalScore() {
        return psTotalScore;
    }

    public void setPsTotalScore(Integer psTotalScore) {
        this.psTotalScore = psTotalScore;
    }

    public Long getPsUpdateTime() {
        return psUpdateTime;
    }

    public void setPsUpdateTime(Long psUpdateTime) {
        this.psUpdateTime = psUpdateTime;
    }

    @Override
    public String toString() {
        return "PradScore{" +
        "psId=" + psId +
        ", psUserId=" + psUserId +
        ", psTotalScore=" + psTotalScore +
        ", psUpdateTime=" + psUpdateTime +
        "}";
    }
}
