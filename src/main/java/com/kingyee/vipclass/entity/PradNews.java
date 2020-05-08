package com.kingyee.vipclass.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kingyee.common.util.TimeUtil;

import java.io.Serializable;

/**
 * <p>
 * 通用资讯表
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
public class PradNews implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ne_id", type = IdType.AUTO)
    private Long neId;

    /**
     * 分类id 因为mybatis-plus在更新的时候做了null判断，默认不更新为null的传参，此处设置忽略null值的判断
     */
    @TableField(value = "ne_nc_id", updateStrategy = FieldStrategy.IGNORED)
    private Long neNcId;

    /**
     * 标题
     */
    private String neTitle;

    /**
     * 缩略图路径
     */
    private String neThumbnailPath;

    /**
     * 轮播图路径
     */
    private String neCarouselPath;

    /**
     * 内容
     */
    private String neContent;

    /**
     * 视频路径
     */
    private String neVideoPath;

    /**
     * 点击量
     */
    private Integer neHits;

    /**
     * 点赞量
     */
    private Integer neLikeNum;

    /**
     * 收藏量
     */
    private Integer neBookNum;

    /**
     * 是否是头图(0:否，1：是)
     */
    private Integer neIsBanner;

    /**
     * 头部路径
     */
    private String neBannerPicPath;

    /**
     * 关联专家id
     */
    private Long neExpertId;

    /**
     * 显示用发布日期
     */
    private Long neDate;

    /**
     * 来源
     */
    private String neFrom;

    /**
     * 发布人id
     */
    private Long nePublishUserId;

    /**
     * 发布人姓名
     */
    private String nePublishUserName;

    /**
     * 发布时间
     */
    private Long neCreateTime;

    /**
     * 更新人id
     */
    private Long neUpdateUserId;

    /**
     * 更新人姓名
     */
    private String neUpdateUserName;

    /**
     * 更新时间
     */
    private Long neUpdateTime;

    /**
     * 是否有效(0：无效；1：有效)
     */
    private Integer neIsValid;

    /**
     * 是否显示轮播图,(0,不显示,1:显示)
     */
    private Integer neIsCarousel;

    /**
     * 分类id（可选多个）
     */
    private String neNcIds;

    /**
     * 直播结束时间
     */
    private Long neEndDate;

    /**
     * 内容标签id（可选多个）
     */
    private String neTagIds;

    public Long getNeEndDate() {
        return neEndDate;
    }

    public void setNeEndDate(Long neEndDate) {
        this.neEndDate = neEndDate;
    }

    public Long getNeId() {
        return neId;
    }

    public void setNeId(Long neId) {
        this.neId = neId;
    }

    public Long getNeNcId() {
        return neNcId;
    }

    public void setNeNcId(Long neNcId) {
        this.neNcId = neNcId;
    }

    public String getNeTitle() {
        return neTitle;
    }

    public void setNeTitle(String neTitle) {
        this.neTitle = neTitle;
    }

    public String getNeThumbnailPath() {
        return neThumbnailPath;
    }

    public void setNeThumbnailPath(String neThumbnailPath) {
        this.neThumbnailPath = neThumbnailPath;
    }

    public String getNeCarouselPath() {
        return neCarouselPath;
    }

    public void setNeCarouselPath(String neCarouselPath) {
        this.neCarouselPath = neCarouselPath;
    }

    public String getNeContent() {
        return neContent;
    }

    public void setNeContent(String neContent) {
        this.neContent = neContent;
    }

    public String getNeVideoPath() {
        return neVideoPath;
    }

    public void setNeVideoPath(String neVideoPath) {
        this.neVideoPath = neVideoPath;
    }

    public Integer getNeHits() {
        return neHits;
    }

    public void setNeHits(Integer neHits) {
        this.neHits = neHits;
    }

    public Integer getNeLikeNum() {
        return neLikeNum;
    }

    public void setNeLikeNum(Integer neLikeNum) {
        this.neLikeNum = neLikeNum;
    }

    public Integer getNeBookNum() {
        return neBookNum;
    }

    public void setNeBookNum(Integer neBookNum) {
        this.neBookNum = neBookNum;
    }

    public Integer getNeIsBanner() {
        return neIsBanner;
    }

    public void setNeIsBanner(Integer neIsBanner) {
        this.neIsBanner = neIsBanner;
    }

    public String getNeBannerPicPath() {
        return neBannerPicPath;
    }

    public void setNeBannerPicPath(String neBannerPicPath) {
        this.neBannerPicPath = neBannerPicPath;
    }

    public Long getNeExpertId() {
        return neExpertId;
    }

    public void setNeExpertId(Long neExpertId) {
        this.neExpertId = neExpertId;
    }

    public Long getNeDate() {
        return neDate;
    }

    public void setNeDate(Long neDate) {
        this.neDate = neDate;
    }

    public String getNeFrom() {
        return neFrom;
    }

    public void setNeFrom(String neFrom) {
        this.neFrom = neFrom;
    }

    public Long getNePublishUserId() {
        return nePublishUserId;
    }

    public void setNePublishUserId(Long nePublishUserId) {
        this.nePublishUserId = nePublishUserId;
    }

    public String getNePublishUserName() {
        return nePublishUserName;
    }

    public void setNePublishUserName(String nePublishUserName) {
        this.nePublishUserName = nePublishUserName;
    }

    public Long getNeCreateTime() {
        return neCreateTime;
    }

    public void setNeCreateTime(Long neCreateTime) {
        this.neCreateTime = neCreateTime;
    }

    public Long getNeUpdateUserId() {
        return neUpdateUserId;
    }

    public void setNeUpdateUserId(Long neUpdateUserId) {
        this.neUpdateUserId = neUpdateUserId;
    }

    public String getNeUpdateUserName() {
        return neUpdateUserName;
    }

    public void setNeUpdateUserName(String neUpdateUserName) {
        this.neUpdateUserName = neUpdateUserName;
    }

    public Long getNeUpdateTime() {
        return neUpdateTime;
    }

    public void setNeUpdateTime(Long neUpdateTime) {
        this.neUpdateTime = neUpdateTime;
    }

    public Integer getNeIsValid() {
        return neIsValid;
    }

    public void setNeIsValid(Integer neIsValid) {
        this.neIsValid = neIsValid;
    }

    public Integer getNeIsCarousel() {
        return neIsCarousel;
    }

    public void setNeIsCarousel(Integer neIsCarousel) {
        this.neIsCarousel = neIsCarousel;
    }

    public String getNeNcIds() {
        return neNcIds;
    }

    public void setNeNcIds(String neNcIds) {
        this.neNcIds = neNcIds;
    }

    public String getNeTagIds() {
        return neTagIds;
    }

    public void setNeTagIds(String neTagIds) {
        this.neTagIds = neTagIds;
    }

    @Override
    public String toString() {
        return "PradNews{" +
                "neId=" + neId +
                ", neNcId=" + neNcId +
                ", neTitle='" + neTitle + '\'' +
                ", neThumbnailPath='" + neThumbnailPath + '\'' +
                ", neCarouselPath='" + neCarouselPath + '\'' +
                ", neContent='" + neContent + '\'' +
                ", neVideoPath='" + neVideoPath + '\'' +
                ", neHits=" + neHits +
                ", neLikeNum=" + neLikeNum +
                ", neBookNum=" + neBookNum +
                ", neIsBanner=" + neIsBanner +
                ", neBannerPicPath='" + neBannerPicPath + '\'' +
                ", neExpertId=" + neExpertId +
                ", neDate=" + neDate +
                ", neFrom='" + neFrom + '\'' +
                ", nePublishUserId=" + nePublishUserId +
                ", nePublishUserName='" + nePublishUserName + '\'' +
                ", neCreateTime=" + neCreateTime +
                ", neUpdateUserId=" + neUpdateUserId +
                ", neUpdateUserName='" + neUpdateUserName + '\'' +
                ", neUpdateTime=" + neUpdateTime +
                ", neIsValid=" + neIsValid +
                ", neIsCarousel=" + neIsCarousel +
                ", neEndDate=" + neEndDate +
                ", neNcIds=" + neNcIds +
                ", neTagIds=" + neTagIds +
                '}';
    }



    @TableField(exist = false)
    private String nePublishDateStr;

    public String getNePublishDateStr() {
        nePublishDateStr = TimeUtil.longToString(this.getNeDate(), TimeUtil.FORMAT_DATE);
        return nePublishDateStr;
    }

    public void setNePublishDateStr(String nePublishDateStr) {
        this.nePublishDateStr = nePublishDateStr;
        this.neDate = TimeUtil.stringToLong(nePublishDateStr,TimeUtil.FORMAT_DATE);
    }


    //医学资源时间转换字段
    @TableField(exist = false)
    private String neStartDateStr;
    @TableField(exist = false)
    private String neEndDateStr;

    public String getNeStartDateStr() {
        neStartDateStr = TimeUtil.longToString(this.getNeDate(), TimeUtil.FORMAT_DATETIME_FULL);
        return neStartDateStr;
    }

    public void setNeStartDateStr(String neStartDateStr) {
        this.neStartDateStr = neStartDateStr;
    }

    public String getNeEndDateStr() {
        neStartDateStr = TimeUtil.longToString(this.getNeEndDate(), TimeUtil.FORMAT_DATETIME_FULL);
        return neEndDateStr;
    }

    public void setNeEndDateStr(String neEndDateStr) {
        this.neEndDateStr = neEndDateStr;
    }



	@TableField(exist = false)
	private String categoryName;
	@TableField(exist = false)
	private String expertName;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getExpertName() {
		return expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}
}
