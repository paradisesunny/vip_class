package com.kingyee.prad.wx.mp.config;

import com.kingyee.prad.wx.mp.handler.*;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static me.chanjar.weixin.common.api.WxConsts.EventType;
import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.EventType.UNSUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.CustomerService.*;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.POI_CHECK_NOTIFY;

/**
 * wechat mp configuration
 *
 * @author Binary Wang(https://github.com/binarywang)
 */

@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfiguration {

    private final LogHandler logHandler;
    private final NullHandler nullHandler;
    private final KfSessionHandler kfSessionHandler;
    private final StoreCheckNotifyHandler storeCheckNotifyHandler;
    private final LocationHandler locationHandler;
    private final MenuHandler menuHandler;
    private final MsgHandler msgHandler;
    private final UnsubscribeHandler unsubscribeHandler;
    private final SubscribeHandler subscribeHandler;
    private final ScanHandler scanHandler;
    private final WxMpProperties properties;

    public WxMpConfiguration(LogHandler logHandler, NullHandler nullHandler, KfSessionHandler kfSessionHandler,
                             StoreCheckNotifyHandler storeCheckNotifyHandler, LocationHandler locationHandler, MenuHandler menuHandler,
                             MsgHandler msgHandler, UnsubscribeHandler unsubscribeHandler, SubscribeHandler subscribeHandler,
                             ScanHandler scanHandler, WxMpProperties properties) {
        this.logHandler = logHandler;
        this.nullHandler = nullHandler;
        this.kfSessionHandler = kfSessionHandler;
        this.storeCheckNotifyHandler = storeCheckNotifyHandler;
        this.locationHandler = locationHandler;
        this.menuHandler = menuHandler;
        this.msgHandler = msgHandler;
        this.unsubscribeHandler = unsubscribeHandler;
        this.subscribeHandler = subscribeHandler;
        this.scanHandler = scanHandler;
        this.properties = properties;
    }

    @Bean
    public WxMpService wxMpService() {
        WxMpService service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        configStorage.setAppId(this.properties.getAppId());
        configStorage.setSecret(this.properties.getSecret());
        configStorage.setToken(this.properties.getToken());
        configStorage.setAesKey(this.properties.getAesKey());
        service.setWxMpConfigStorage(configStorage);
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
                .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
                .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
                .handler(this.kfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(EVENT).event(EventType.CLICK).handler(this.menuHandler).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(EVENT).event(EventType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(this.unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(EVENT).event(EventType.LOCATION).handler(this.locationHandler).end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }

}
