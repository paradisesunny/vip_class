package com.kingyee.vipclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author Administrator
 * @since 2020-03-16
 */
public class PradComment implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "pc_id", type = IdType.AUTO)
    private Long pcId;

    /**
     * 用户id
     */
    private Long pcUserId;

    /**
     * 用户名
     */
    private String pcUserName;

    /**
     * 文章主键
     */
    private Long pcNewsId;

    /**
     * 评论内容
     */
    private String pcContent;

    /**
     * 本条评论点赞量
     */
    private Integer pcHits;

    /**
     * 本条评论时间
     */
    private Long pcTime;


    public Long getPcId() {
        return pcId;
    }

    public void setPcId(Long pcId) {
        this.pcId = pcId;
    }

    public Long getPcUserId() {
        return pcUserId;
    }

    public void setPcUserId(Long pcUserId) {
        this.pcUserId = pcUserId;
    }

    public String getPcUserName() {
        return pcUserName;
    }

    public void setPcUserName(String pcUserName) {
        this.pcUserName = pcUserName;
    }

    public Long getPcNewsId() {
        return pcNewsId;
    }

    public void setPcNewsId(Long pcNewsId) {
        this.pcNewsId = pcNewsId;
    }

    public String getPcContent() {
        return pcContent;
    }

    public void setPcContent(String pcContent) {
        this.pcContent = pcContent;
    }

    public Integer getPcHits() {
        return pcHits;
    }

    public void setPcHits(Integer pcHits) {
        this.pcHits = pcHits;
    }

    public Long getPcTime() {
        return pcTime;
    }

    public void setPcTime(Long pcTime) {
        this.pcTime = pcTime;
    }

    @Override
    public String toString() {
        return "PradComment{" +
        "pcId=" + pcId +
        ", pcUserId=" + pcUserId +
        ", pcUserName=" + pcUserName +
        ", pcNewsId=" + pcNewsId +
        ", pcContent=" + pcContent +
        ", pcHits=" + pcHits +
        ", pcTime=" + pcTime +
        "}";
    }

    @TableField(exist = false)
    private String pcTimeStr;

    public String getPcTimeStr() {
        return pcTimeStr;
    }

    public void setPcTimeStr(String pcTimeStr) {
        this.pcTimeStr = pcTimeStr;
    }

}
