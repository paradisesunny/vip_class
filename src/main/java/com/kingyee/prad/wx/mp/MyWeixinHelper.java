package com.kingyee.prad.wx.mp;

import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * 微信工具类<br>
 * 生成二维码 推送消息 ... ...
 *
 * @author 李旭光
 * @version 2018年04月10日  16:14
 */
@Component
public class MyWeixinHelper {
    private Logger logger = LoggerFactory.getLogger(MyWeixinHelper.class);

    @Autowired
    private WxMpService wxService;

    /**
     * 生成会议的临时字符串二维码
     *
     * @return 二维码地址
     */
    public String buildMeetingTmpStrQrCode(Long id) throws Exception {
        WxMpQrCodeTicket ticket = wxService.getQrcodeService().qrCodeCreateTmpTicket("meeting_" + id, 3600 * 10);
        return wxService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
    }

    /**
     * 生成会议的临时ID二维码
     *
     * @return 二维码地址
     */
    public String buildMeetingTmpQrCode(Long id) throws Exception {
        WxMpQrCodeTicket ticket = wxService.getQrcodeService().qrCodeCreateTmpTicket(id.intValue(), 3600 * 10);
        return wxService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
    }

    /**
     * 生成会议的永久二维码
     *
     * @return 二维码地址
     */
    public String buildMeetingQrCode(Long id) throws Exception {
        WxMpQrCodeTicket ticket = wxService.getQrcodeService().qrCodeCreateLastTicket("meeting_" + id);
        return wxService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
    }

    /**
     * 生成会议的二维码图片文件
     */
    public File buildMeetingQrCodeImg(Long id) throws WxErrorException {
        WxMpQrCodeTicket ticket = wxService.getQrcodeService().qrCodeCreateLastTicket("meeting_" + id);
        return wxService.getQrcodeService().qrCodePicture(ticket);
    }

    /**
     * 获取标签的ID
     *
     * @param tag 标签名称
     * @return 标签ID；0如果没找到对应的标签
     */
    public Long getTag(String tag) throws Exception {
        List<WxUserTag> tags = wxService.getUserTagService().tagGet();
        if (tags == null || tags.size() == 0) {
            return 0L;
        }
        for (WxUserTag wxUserTag : tags) {
            if (wxUserTag.getName().equals(tag)) {
                return wxUserTag.getId();
            }
        }
        return 0L;
    }

    /**
     * 添加标签<br>
     * 如果标签存在，直接返回标签ID
     *
     * @param tag 标签名称
     * @return 标签ID
     */
    public Long addTag(String tag) throws Exception {
        Long tagId = getTag(tag);
        if (tagId > 0) {
            return tagId;
        }
        return wxService.getUserTagService().tagCreate(tag).getId();
    }

    /**
     * 获取微信菜单
     */
    public String getMenu() throws Exception {
        WxMpMenu menu = wxService.getMenuService().menuGet();
        if (menu != null) {
            return menu.toJson();
        }
        return "没有菜单";
    }

    /**
     * 创建微信菜单
     */
    public void createMenu(WxMenu menu) throws Exception {
        wxService.getMenuService().menuCreate(menu);
    }

    /**
     * 删除菜单
     */
    public void delMenu() throws Exception {
        wxService.getMenuService().menuDelete();
    }

    /**
     * 构造oauth回调url
     *
     * @param url 回调的url
     */
    public String oauthUrl(String url) {
        return oauthUrl(url, "snsapi_base");
    }

    public String oauthUrl(String url, String scope) {
        return wxService.oauth2buildAuthorizationUrl(url, scope, "");
    }

    /**
     * 通过code获取openId
     */
    public String openId(String code) throws Exception {
        return wxService.oauth2getAccessToken(code).getOpenId();
    }

    //    /**
    ////     * 为指定的微信用户发送模版消息
    ////     *
    ////     * @param to          要发送的微信用户
    ////     * @param templateMsg 模版消息
    ////     */
    //    public void send(String to, ITemplateMsg templateMsg) {
    //        WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage.builder()
    //                .toUser(to)
    //                .templateId(templateMsg.getTemplateId())
    //                .data(templateMsg.getDataList())
    //                .url(templateMsg.getUrl())
    //                .build();
    //        try {
    //            wxService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
    //        } catch (WxErrorException e) {
    //            logger.error("send template msg error:" + e.getMessage());
    //        }
    //    }

    public WxMpService getWxService() {
        return wxService;
    }

    public void setWxService(WxMpService wxService) {
        this.wxService = wxService;
    }
}
