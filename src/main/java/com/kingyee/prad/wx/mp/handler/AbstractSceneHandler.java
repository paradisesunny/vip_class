package com.kingyee.prad.wx.mp.handler;

import com.kingyee.prad.EdoctorConfig;
import com.kingyee.prad.common.EDoctorUtil;
import com.kingyee.prad.service.impl.PrScanServiceImpl;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.util.xml.XStreamTransformer;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSceneHandler extends AbstractHandler {
    @Autowired
    private EdoctorConfig edoctorConfig;

    @Autowired
    private EDoctorUtil eDoctorUtil;

    @Autowired
    private PrScanServiceImpl prScanService;

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     * 将扫码数据转发给翼多
     * 保存扫码记录
     */
    protected WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage, WxMpService wxMpService, String scene) throws Exception {
        String openId = wxMessage.getFromUser();
        logger.info(scene + ", " + openId + ":扫描带参二维码事件");
        //调用翼多接口,原样返回从微信接收的xml信息
        wxMessage.setAllFieldsMap(null);
        wxMessage.setScanCodeInfo(null);
        wxMessage.setSendPicsInfo(null);
        wxMessage.setSendLocationInfo(null);
        wxMessage.setHardWare(null);
        String xml = XStreamTransformer.toXml(WxMpXmlMessage.class, wxMessage);
        //异步发送数据
        eDoctorUtil.sendScanData(edoctorConfig.getScanUrl(), xml);
        //保存扫码记录
        prScanService.saveScan(wxMessage,scene);
        logger.info(scene + ", " + openId + ":扫码成功:\r\n" + xml);
        //返回消息给用户
        return WxMpXmlOutMessage.TEXT().content("扫码成功\n").
                fromUser(wxMessage.getToUser()).
                toUser(wxMessage.getFromUser()).build();
    }
}