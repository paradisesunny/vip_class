package com.kingyee.prad.mapper;

import com.kingyee.prad.entity.PradComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kingyee.prad.model.NewsCommentModel;
import com.kingyee.prad.model.NewsModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
public interface PradCommentMapper extends BaseMapper<PradComment> {

	/**
	 * 查询本篇文章的评论列表和我的点赞记录
	 * @param newsId
	 * @return
	 */
	@Select("select c.pc_id as commentId,c.pc_user_name as userName,c.pc_news_id as newsId,c.pc_content as content," +
			"c.pc_hits as likeNum,c.pc_time as commentTime,l.lr_comment_id as likeCommentId,l.lr_user_id as myUserId " +
			" FROM prad_comment as c left join prad_user as u on c.pc_user_id = u.pu_id " +
			" left join prad_like_record as l on c.pc_id = l.lr_comment_id and l.lr_user_id = #{userId} " +
			" where c.pc_news_id = #{newsId} " +
			" GROUP BY c.pc_id "+
			" ORDER by c.pc_time desc")
	List<NewsCommentModel> getCommentList(@Param(value = "newsId") Long newsId,@Param(value = "userId") Long userId);
}
