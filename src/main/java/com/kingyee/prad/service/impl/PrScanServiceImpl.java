package com.kingyee.prad.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.entity.PrScan;
import com.kingyee.prad.mapper.PrScanMapper;
import com.kingyee.prad.service.IPrScanService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户扫码记录 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-10
 */
@Service
public class PrScanServiceImpl extends ServiceImpl<PrScanMapper, PrScan> implements IPrScanService {

    @Autowired
    private PrScanMapper prScanMapper;

    /**
     * 保存扫码记录
     *
     * @param wxMessage
     */
    public void saveScan(WxMpXmlMessage wxMessage, String scene) {
        PrScan db = new PrScan();
        db.setScCreateDate(TimeUtil.dateToLong());
        db.setScOpenId(wxMessage.getFromUser());
        db.setScEventKey(scene);
        db.setScEvent(wxMessage.getEvent());
        prScanMapper.insert(db);
    }

}
