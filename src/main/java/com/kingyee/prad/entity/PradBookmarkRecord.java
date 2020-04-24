package com.kingyee.prad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 收藏记录表
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
public class PradBookmarkRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "br_id", type = IdType.AUTO)
    private Long brId;

    /**
     * 用户主键
     */
    private Long brUserId;

    /**
     * 文章主键
     */
    private Long brNewsId;

    /**
     * 收藏时间
     */
    private Long brTime;

    /**
     * 文章缩略图
     */
    private Long brThumbPath;

    /**
     * 文章标题
     */
    private Long brTitle;

    /**
     * 文章路径
     */
    private String brArticlePath;


    public Long getBrId() {
        return brId;
    }

    public void setBrId(Long brId) {
        this.brId = brId;
    }

    public Long getBrUserId() {
        return brUserId;
    }

    public void setBrUserId(Long brUserId) {
        this.brUserId = brUserId;
    }

    public Long getBrNewsId() {
        return brNewsId;
    }

    public void setBrNewsId(Long brNewsId) {
        this.brNewsId = brNewsId;
    }

    public Long getBrTime() {
        return brTime;
    }

    public void setBrTime(Long brTime) {
        this.brTime = brTime;
    }

    public Long getBrThumbPath() {
        return brThumbPath;
    }

    public void setBrThumbPath(Long brThumbPath) {
        this.brThumbPath = brThumbPath;
    }

    public Long getBrTitle() {
        return brTitle;
    }

    public void setBrTitle(Long brTitle) {
        this.brTitle = brTitle;
    }

    public String getBrArticlePath() {
        return brArticlePath;
    }

    public void setBrArticlePath(String brArticlePath) {
        this.brArticlePath = brArticlePath;
    }

    @Override
    public String toString() {
        return "PradBookmarkRecord{" +
        "brId=" + brId +
        ", brUserId=" + brUserId +
        ", brNewsId=" + brNewsId +
        ", brTime=" + brTime +
        ", brThumbPath=" + brThumbPath +
        ", brTitle=" + brTitle +
        ", brArticlePath=" + brArticlePath +
        "}";
    }
}
