package com.kingyee.vipclass.mapper;

import com.kingyee.vipclass.entity.PradNewsCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kingyee.vipclass.model.NewsCategoryModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 资讯分类表 Mapper 接口
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
public interface PradNewsCategoryMapper extends BaseMapper<PradNewsCategory> {
    /**
     * 根据模板id查找所该模板下的所有专题(主题)
     *
     * @param catePk
     * @return
     */
    @Select("select nc_id " +
            " from prad_news_category " +
            " where nc_pid = #{catePk} ")
    List<PradNewsCategory> getChildList(@Param(value = "catePk") Long catePk);

    /**
     * 根据ne_nc_id查找所属的模板
     *
     * @param childCatePk
     * @return
     */
    @Select("select * " +
            " from prad_news_category " +
            " where nc_id =(" +
            " select nc_pid " +
            " from prad_news_category " +
            " where nc_id = #{childCatePk})")
    PradNewsCategory getCategory(@Param(value = "childCatePk") Long childCatePk);

    /**
     * 根据catePk获取指定格式的树列表
     *
     * @param parentId
	 * @param type
     * @return
     */
    @Select("SELECT pnc.nc_id as id,pnc.nc_name as title \n" +
            "FROM `prad_news_category` as pnc\n" +
            "WHERE pnc.nc_pid = #{parentId} and pnc.nc_add1 = #{type}")
    List<NewsCategoryModel> getCategoryModelList(@Param(value = "parentId") Long parentId, @Param(value = "type")String type);
}
