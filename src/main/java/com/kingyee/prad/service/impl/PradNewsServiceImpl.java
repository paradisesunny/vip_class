package com.kingyee.prad.service.impl;

import com.kingyee.prad.entity.PradNews;
import com.kingyee.prad.mapper.PradNewsMapper;
import com.kingyee.prad.service.IPradNewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通用资讯表 服务实现类
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
@Service
public class PradNewsServiceImpl extends ServiceImpl<PradNewsMapper, PradNews> implements IPradNewsService {

}
