package com.kingyee.prad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 用户扫码记录
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
public class PrScan implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "sc_pk", type = IdType.AUTO)
    private Long scPk;

    /**
     * openId
     */
    private String scOpenId;

    /**
     * 扫码参数
     */
    private String scEventKey;

    /**
     * 扫码时间
     */
    private Long scCreateDate;

    /**
     * 事件类型(subscribe/SCAN)
     */
    private String scEvent;


    public Long getScPk() {
        return scPk;
    }

    public void setScPk(Long scPk) {
        this.scPk = scPk;
    }

    public String getScOpenId() {
        return scOpenId;
    }

    public void setScOpenId(String scOpenId) {
        this.scOpenId = scOpenId;
    }

    public String getScEventKey() {
        return scEventKey;
    }

    public void setScEventKey(String scEventKey) {
        this.scEventKey = scEventKey;
    }

    public Long getScCreateDate() {
        return scCreateDate;
    }

    public void setScCreateDate(Long scCreateDate) {
        this.scCreateDate = scCreateDate;
    }

    public String getScEvent() {
        return scEvent;
    }

    public void setScEvent(String scEvent) {
        this.scEvent = scEvent;
    }

    @Override
    public String toString() {
        return "PrScan{" +
        "scPk=" + scPk +
        ", scOpenId=" + scOpenId +
        ", scEventKey=" + scEventKey +
        ", scCreateDate=" + scCreateDate +
        ", scEvent=" + scEvent +
        "}";
    }
}
