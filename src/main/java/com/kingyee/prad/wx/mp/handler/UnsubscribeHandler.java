package com.kingyee.prad.wx.mp.handler;

import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.entity.WechatUser;
import com.kingyee.prad.service.impl.WechatUserServiceImpl;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class UnsubscribeHandler extends AbstractHandler {

    @Autowired
    private WechatUserServiceImpl wechatUserService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        this.logger.info("取消关注用户 OPENID: " + openId);
        WechatUser wechatUser = wechatUserService.getWechatUserByOpenid(openId);
        Long now = TimeUtil.dateToLong();
        if (wechatUser != null) {
            wechatUser.setWuUpdateTime(now);
            wechatUser.setWuUserId(null);
            wechatUser.setWuUserType(null);
            wechatUser.setWuSubscribe(0);
            wechatUserService.updateById(wechatUser);
        }
        return null;
    }

}
