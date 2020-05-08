package com.kingyee.vipclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 评论记录id
 * </p>
 *
 * @author Administrator
 * @since 2020-03-16
 */
public class PradLikeRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "lr_id", type = IdType.AUTO)
    private Long lrId;

    /**
     * 用户主键
     */
    private Long lrUserId;

    /**
     * 资讯主键
     */
    private Long lrNewsId;

    /**
     * 点赞时间
     */
    private Long lrTime;

    /**
     * 评论记录id
     */
    private Long lrCommentId;
	/**
	 * 类型（0：资讯点赞，1：评论点赞）
	 */
	private Integer lrType;


    public Long getLrId() {
        return lrId;
    }

    public void setLrId(Long lrId) {
        this.lrId = lrId;
    }

    public Long getLrUserId() {
        return lrUserId;
    }

    public void setLrUserId(Long lrUserId) {
        this.lrUserId = lrUserId;
    }

    public Long getLrNewsId() {
        return lrNewsId;
    }

    public void setLrNewsId(Long lrNewsId) {
        this.lrNewsId = lrNewsId;
    }

    public Long getLrTime() {
        return lrTime;
    }

    public void setLrTime(Long lrTime) {
        this.lrTime = lrTime;
    }

    public Long getLrCommentId() {
        return lrCommentId;
    }

    public void setLrCommentId(Long lrCommentId) {
        this.lrCommentId = lrCommentId;
    }

	public Integer getLrType() {
		return lrType;
	}

	public void setLrType(Integer lrType) {
		this.lrType = lrType;
	}

	@Override
    public String toString() {
        return "PradLikeRecord{" +
        "lrId=" + lrId +
        ", lrUserId=" + lrUserId +
        ", lrNewsId=" + lrNewsId +
        ", lrTime=" + lrTime +
        ", lrCommentId=" + lrCommentId +
		", lrType=" + lrType +
        "}";
    }
}
