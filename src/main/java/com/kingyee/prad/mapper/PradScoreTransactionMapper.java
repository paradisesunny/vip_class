package com.kingyee.prad.mapper;

import com.kingyee.prad.entity.PradScoreTransaction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * <p>
 * 成长值记录明细表 Mapper 接口
 * </p>
 *
 * @author niumuyao
 * @since 2020-04-09
 */
public interface PradScoreTransactionMapper extends BaseMapper<PradScoreTransaction> {

    /**
     * 获取积分时间
     * @param
     * @return
     */
    @Select("SELECT FROM_UNIXTIME(t.st_create_time/ 1000,'%Y-%m') as time FROM prad_score_transaction as t " +
            " where t.st_user_id = #{userId} GROUP BY time ORDER by time desc")
    List<String> getScoreByMonth(@Param(value = "userId")Long userId);


    /**
     * 获取我今天完成的任务
     * @param
     * @return
     */
    @Select("select t.st_rule_id from prad_score_transaction as t " +
            " where t.st_user_id = #{userId} and t.st_rule_id in(#{ids}) "+
            " and t.st_create_time BETWEEN #{startTime} and #{endTime} "+
            " GROUP BY t.st_rule_id")
    List<Long> getUserScoreByToday(@Param(value = "userId")Long userId,
                                   @Param(value = "ids")List<Long> ids,
                                   @Param(value = "startTime")Long startTime,
                                   @Param(value = "endTime")Long endTime);
}
