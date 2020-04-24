package com.kingyee.prad.model;

import com.kingyee.common.util.TimeCountUtil;
import com.kingyee.common.util.TimeUtil;

public class NewsCommentModel {
    private Long commentId;
    private String userName;
    private Long newsId;
    private String content;
    private Integer likeNum;
	private Long commentTime;
	private Long likeCommentId;
	private Long myUserId;
	private String commentTimeStr;

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Long getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Long commentTime) {
		this.commentTime = commentTime;
	}

	public Long getLikeCommentId() {
		return likeCommentId;
	}

	public void setLikeCommentId(Long likeCommentId) {
		this.likeCommentId = likeCommentId;
	}

	public Long getMyUserId() {
		return myUserId;
	}

	public void setMyUserId(Long myUserId) {
		this.myUserId = myUserId;
	}

	public String getCommentTimeStr() {
		commentTimeStr = TimeCountUtil.format(TimeUtil.longToDate(getCommentTime()));
		return commentTimeStr;
	}

	public void setCommentTimeStr(String commentTimeStr) {
		this.commentTimeStr = commentTimeStr;
	}
}
