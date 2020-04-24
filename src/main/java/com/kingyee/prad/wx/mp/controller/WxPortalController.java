package com.kingyee.prad.wx.mp.controller;

import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.RandomUtil;
import com.kingyee.prad.entity.WechatUser;
import com.kingyee.prad.service.impl.WechatUserServiceImpl;
import com.kingyee.prad.wx.mp.MyWeixinHelper;
import com.kingyee.prad.wx.mp.WechatUserUtil;
import com.kingyee.prad.wx.mp.config.WxMpProperties;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.crypto.SHA1;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Controller
@RequestMapping("/wx/portal")
public class WxPortalController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxPortalController.class);

    private final WxMpService wxService;
    private final WxMpMessageRouter messageRouter;

    @Autowired
    private WxMpProperties wxMpProperties;

    @Autowired
    private MyWeixinHelper myWeixinHelper;
    @Autowired
    private WechatUserServiceImpl wechatUserService;

    public WxPortalController(WxMpService wxService, WxMpMessageRouter messageRouter) {
        this.wxService = wxService;
        this.messageRouter = messageRouter;
    }

    @GetMapping(produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String authGet(@RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        LOGGER.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    @ResponseBody
    public String post(@RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        LOGGER.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}]," + " timestamp=[{}], " +
                        "nonce=[{}], requestBody=[\n{}\n] ", openid, signature, encType, msgSignature, timestamp, nonce,
                requestBody);

        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
                    timestamp, nonce, msgSignature);
            LOGGER.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }

        LOGGER.debug("\n组装回复信息：{}", out);
        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.messageRouter.route(message);
        } catch (Exception e) {
            LOGGER.error("路由消息时出现异常！", e);
        }

        return null;
    }

    /**
     * 获取openId
     */
    @RequestMapping(value = "getOpenId", method = RequestMethod.GET)
    public String getOpenId(String code, String url) {
        try {
            LOGGER.info("getOpenId:oauth_code=" + code);
            LOGGER.info("getOpenId:url=" + url);
            WxMpOAuth2AccessToken token = wxService.oauth2getAccessToken(code);
            String openId = token.getOpenId();
            try {
                WxMpUser wxMpUser = wxService.getUserService().userInfo(openId);
                WechatUser wechatUser = wechatUserService.getWechatUserByOpenid(openId);
                //当前是否已关注
                if (wxMpUser.getSubscribe() != null && wxMpUser.getSubscribe()) {//已关注
                    if (wechatUser == null) {
                        wechatUserService.saveOrUpdateWechatUser(wxMpUser);//新增微信用户
                    }
                    LOGGER.info("getOpenId:initOpenId=" + openId);
                    WechatUserUtil.initOpenId(openId);
                } else {//未关注
                    return "wechat/subscribe";
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "redirect:" + url;
    }

    /**
     * 获取jssdk中的必要信息
     *
     * @param url code
     */
    @RequestMapping(value = "getJsSdk")
    @ResponseBody
    public String getJsSdk(String url) {
        try {
            String jsapiTicket = wxService.getJsapiTicket(false);

            long timestamp = System.currentTimeMillis() / 1000;
            String noncestr = RandomUtil.generateString(16);
            String result = SHA1.genWithAmple("jsapi_ticket=" + jsapiTicket,
                    "noncestr=" + noncestr,
                    "timestamp=" + timestamp,
                    "url=" + url);

            Map<String, String> map = new HashMap<>();
            map.put("appId", wxMpProperties.getAppId());
            map.put("timestamp", String.valueOf(timestamp));
            map.put("nonceStr", noncestr);
            map.put("signature", result);

            return JacksonMapper.newDataInstance(map).toString();
        } catch (WxErrorException e) {
            LOGGER.error("取得SDK有错误发生。", e);
            return JacksonMapper.newErrorInstance("取得SDK有错误发生。！").toString();
        }
    }

    /**
     * 生成永久带参数二维码
     *
     * @param meetId
     */
    @RequestMapping(value = "createMeetQrCode")
    public String createMeetQrCode(ModelMap mm, Long meetId) {
        try {
            String url = myWeixinHelper.buildMeetingQrCode(meetId);
            mm.addAttribute("url", url);
            LOGGER.info("生成永久带参数二维码:", url);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("生成永久带参数二维码失败", e);
        }
        return "meet";
    }

}
