package com.kingyee.vipclass.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kingyee.vipclass.entity.PradNews;
import com.kingyee.vipclass.model.NewsModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 通用资讯表 Mapper 接口
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
public interface PradNewsMapper extends BaseMapper<PradNews> {
    /**
     * 如果自定义的方法还希望能够使用MP提供的Wrapper条件构造器，则需要如下写法
     *
     * @param userWrapper
     * @return
     */
    @Select("SELECT * FROM user ${ew.customSqlSegment}")
    List<Object> selectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<PradNews> userWrapper);

    /**
     * 和Mybatis使用方法一致
     *
     * @param pId
     * @return
     */

    @Select("select nc.nc_id as fileId,nc.nc_name as fileName,nc.nc_pic_path as filePic " +
            " ,count(n.ne_id) as newsNum,MAX(n.ne_date) as updateDate " +
            " from prad_news_category as nc left join prad_news as n " +
            " on (n.ne_nc_id = nc.nc_id and n.ne_is_valid = 1 ) " +
            " where nc.nc_pid = #{pId} " +
            " GROUP BY nc.nc_id")
    List<NewsModel> selectArticleTopic(@Param(value = "pId") Long pId);

    /**
     * 获取医学资源回放的文件夹
     * 根据结束时间<当前时间确定为回放状态
     *
     * @param pId
     * @return
     */
    @Select("select pnc.nc_id as fileId,pnc.nc_name as fileName,pnc.nc_pic_path as filePic ,count(pn.ne_id) as newsNum,MAX(pn.ne_date) as updateDate \n" +
            "from prad_news_category as pnc\n" +
            "left join prad_news as pn \n" +
            "on (pn.ne_nc_id = pnc.nc_id AND pn.ne_is_valid = 1 AND FROM_UNIXTIME(pn.ne_end_date/1000)<(SELECT NOW())) \n" +
            "where pnc.nc_pid = #{pId}\n" +
            "GROUP BY pnc.nc_id")
    List<NewsModel> getPlaybackFolder(@Param(value = "pId") Long pId);

    /**
     * 获取用户浏览过得所有课程
     *
     * @param userId 用户id
     * @return
     */
    @Select("SELECT pnc.nc_name as fileName,pn.ne_nc_id as fileId,pnc.nc_pic_path as filePic,COUNT(pn.ne_nc_id) as newsNum\n" +
            "FROM prad_news as pn,prad_view_record as pvr ,prad_news_category as pnc\n" +
            "WHERE pn.ne_id = pvr.vr_news_id \n" +
            "AND pn.ne_nc_id = pnc.nc_id \n" +
            "AND pvr.vr_user_id=#{userId}\n" +
            "AND FROM_UNIXTIME(pn.ne_end_date/1000)<(SELECT NOW())\n" +
            "GROUP BY pn.ne_nc_id")
    List<NewsModel> getViewedCourseList(@Param(value = "userId") Long userId);

    /**
     * 复杂分页查询
     * 这段sql作参考用
     *
     * @param
     * @return
     */
    @Select("<script>" +
            " select t.*,a.name_cn as company_name" +
            " from t_company t " +
            " join t_customer_company a on t.company_id=a.id" +
            " where <![CDATA[t.status <> 2]]>" +
            " <if test='nameCn != null and nameCn.trim() != &quot;&quot;'>" +
            " AND t.name_cn like CONCAT('%',#{nameCn},'%')" +
            " </if>" +
            " </script>")
    IPage<Object> selectListbyPage(Page<Object> page,
                                   @Param("nameCn") String nameCn);

	/**
	 * 移除资讯对应的分类
	 * @param
	 * @return
	 */
	@Select("update prad_news as t set t.ne_nc_id = null where t.ne_nc_id = #{pk}")
	String updateNewsByCateId(@Param("pk") Long pk);


    /**
     * 我的专区-前沿进展列表
     * @param
     * @return
     */
    @Select("select * from prad_news as t where t.ne_is_valid = 1 and FIND_IN_SET(#{cateId},t.ne_nc_ids) ")
    IPage<PradNews> selectNewsList(IPage<PradNews> page, @Param(value = "cateId") String cateId);


    /**
     * 我的专区-前沿进展列表
     * @param
     * @return
     */
    @Select("select * from prad_news as t where t.ne_is_valid = 1 and FIND_IN_SET(#{cateId},t.ne_nc_ids) ORDER BY t.ne_update_time desc ")
    List<PradNews> selectNewsListByCateId(@Param(value = "cateId") Long cateId);

}
