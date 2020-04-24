package com.kingyee.prad.wx.mp.handler;

import com.kingyee.prad.SystemConfig;
import com.kingyee.prad.wx.mp.builder.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class ScanHandler extends AbstractSceneHandler {


    @Autowired
    private SystemConfig systemConfig;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        // 扫码事件处理
        // 获取微信用户基本信息
        String openId = wxMessage.getFromUser();
        this.logger.info("扫码 OPENID: " + openId);
        //处理带参二维码
        WxMpXmlOutMessage responseResult = null;
        String key = wxMessage.getEventKey();
        if (StringUtils.isNotEmpty(key)) {
            try {
                responseResult = this.handleSpecial(wxMessage, wxMpService, key);
            } catch (Exception e) {
                this.logger.error(e.getMessage(), e);
            }
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            StringBuffer messageContent = new StringBuffer();
            messageContent.append("欢迎关注前列腺癌治疗领域医生交流互动MDT信息平台，国际学术前沿，最新诊疗理念，尽在“探泌新天地”！\n");
            //messageContent.append("<a href='" + systemConfig.getDomain() + "/admin/index'>点击登录>></a>");
            return new TextBuilder().build(messageContent.toString(), wxMessage, wxMpService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
        return null;
    }

}
