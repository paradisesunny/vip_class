package com.kingyee.prad.wx.mp.handler;

import com.kingyee.prad.SystemConfig;
import com.kingyee.prad.service.impl.WechatUserServiceImpl;
import com.kingyee.prad.wx.mp.WechatUserUtil;
import com.kingyee.prad.wx.mp.builder.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class SubscribeHandler extends AbstractSceneHandler {

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private WechatUserServiceImpl wechatUserService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        String openId = wxMessage.getFromUser();
        this.logger.info("新关注用户 OPENID: " + openId);

        //处理带参二维码
        WxMpXmlOutMessage responseResult = null;
        String key = wxMessage.getEventKey();
        String source = WechatUserUtil.USER_SOURCE_OTHER;
        if (key.startsWith("qrscene_")) {
            try {
                source = WechatUserUtil.USER_SOURCE_EDOCTOR;
                responseResult = this.handleSpecial(wxMessage, wxMpService, key.substring(8));
            } catch (Exception e) {
                this.logger.error(e.getMessage(), e);
            }
        }
        // 获取微信用户基本信息
        try {
            WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId, null);
            if (wxMpUser != null) {
                //保存或更新用户
                wechatUserService.saveOrUpdateWechatUser(wxMpUser);
            }
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == 48001) {
                this.logger.info("该公众号没有获取用户信息权限！");
            }
        } catch (Exception e) {
            this.logger.error("添加关注用户到本地数据库出错！");
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            StringBuffer messageContent = new StringBuffer();
            messageContent.append("欢迎关注前列腺癌治疗领域医生交流互动MDT信息平台，国际学术前沿，最新诊疗理念，尽在“探泌新天地”！\n");
            return new TextBuilder().build(messageContent.toString(), wxMessage, wxMpService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

}
