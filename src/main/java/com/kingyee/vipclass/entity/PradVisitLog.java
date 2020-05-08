package com.kingyee.vipclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 访问记录日志
 * </p>
 *
 * @author dxl
 * @since 2020-03-24
 */
public class PradVisitLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "vl_id", type = IdType.AUTO)
    private Long vlId;

    /**
     * 用户主键
     */
    private Long vlUserId;

    /**
     * 访问页面开始时间
     */
    private Long vlStartTime;

    /**
     * 访问页面结束时间
     */
    private Long vlEndTime;

    /**
     * 访问页面时长(秒)
     */
    private Long vlDuration;

    /**
     * 观看模块
     */
    private String vlVisitModule;

    /**
     * 资讯所属模块
     */
    private String vlNewsModule;

    /**
     * 资讯主键
     */
    private Long vlNewsId;

    /**
     * 是否包含视频(0：不包含，1：包含)
     */
    private Integer vlIncludeVideo;

    /**
     * ip地址
     */
    private String vlIp;

    /**
     * 是否点赞(0：未点赞；1: 点赞；2：取消点赞)
     */
    private Integer vlLike;

    /**
     * 点赞时间/取消点赞时间
     */
    private Long vlLikeTime;

    /**
     * 是否收藏(0：未收藏；1: 收藏；2：取消收藏)
     */
    private Integer vlFavorite;

    /**
     * 收藏时间/取消收藏时间
     */
    private Long vlFavoriteTime;

    /**
     * 视频观看记录主键
     */
    private Long vlVrId;


    public Long getVlId() {
        return vlId;
    }

    public void setVlId(Long vlId) {
        this.vlId = vlId;
    }

    public Long getVlUserId() {
        return vlUserId;
    }

    public void setVlUserId(Long vlUserId) {
        this.vlUserId = vlUserId;
    }

    public Long getVlStartTime() {
        return vlStartTime;
    }

    public void setVlStartTime(Long vlStartTime) {
        this.vlStartTime = vlStartTime;
    }

    public Long getVlEndTime() {
        return vlEndTime;
    }

    public void setVlEndTime(Long vlEndTime) {
        this.vlEndTime = vlEndTime;
    }

    public Long getVlDuration() {
        return vlDuration;
    }

    public void setVlDuration(Long vlDuration) {
        this.vlDuration = vlDuration;
    }

    public String getVlVisitModule() {
        return vlVisitModule;
    }

    public void setVlVisitModule(String vlVisitModule) {
        this.vlVisitModule = vlVisitModule;
    }

    public String getVlNewsModule() {
        return vlNewsModule;
    }

    public void setVlNewsModule(String vlNewsModule) {
        this.vlNewsModule = vlNewsModule;
    }

    public Long getVlNewsId() {
        return vlNewsId;
    }

    public void setVlNewsId(Long vlNewsId) {
        this.vlNewsId = vlNewsId;
    }

    public Integer getVlIncludeVideo() {
        return vlIncludeVideo;
    }

    public void setVlIncludeVideo(Integer vlIncludeVideo) {
        this.vlIncludeVideo = vlIncludeVideo;
    }

    public String getVlIp() {
        return vlIp;
    }

    public void setVlIp(String vlIp) {
        this.vlIp = vlIp;
    }

    public Integer getVlLike() {
        return vlLike;
    }

    public void setVlLike(Integer vlLike) {
        this.vlLike = vlLike;
    }

    public Long getVlLikeTime() {
        return vlLikeTime;
    }

    public void setVlLikeTime(Long vlLikeTime) {
        this.vlLikeTime = vlLikeTime;
    }

    public Integer getVlFavorite() {
        return vlFavorite;
    }

    public void setVlFavorite(Integer vlFavorite) {
        this.vlFavorite = vlFavorite;
    }

    public Long getVlFavoriteTime() {
        return vlFavoriteTime;
    }

    public void setVlFavoriteTime(Long vlFavoriteTime) {
        this.vlFavoriteTime = vlFavoriteTime;
    }

    public Long getVlVrId() {
        return vlVrId;
    }

    public void setVlVrId(Long vlVrId) {
        this.vlVrId = vlVrId;
    }

    @Override
    public String toString() {
        return "PradVisitLog{" +
        "vlId=" + vlId +
        ", vlUserId=" + vlUserId +
        ", vlStartTime=" + vlStartTime +
        ", vlEndTime=" + vlEndTime +
        ", vlDuration=" + vlDuration +
        ", vlVisitModule=" + vlVisitModule +
        ", vlNewsModule=" + vlNewsModule +
        ", vlNewsId=" + vlNewsId +
        ", vlIncludeVideo=" + vlIncludeVideo +
        ", vlIp=" + vlIp +
        ", vlLike=" + vlLike +
        ", vlLikeTime=" + vlLikeTime +
        ", vlFavorite=" + vlFavorite +
        ", vlFavoriteTime=" + vlFavoriteTime +
        ", vlVrId=" + vlVrId +
        "}";
    }
}
