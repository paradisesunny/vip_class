package com.kingyee.prad.wx.mp;


import com.kingyee.common.spring.mvc.WebUtil;

import javax.servlet.http.HttpSession;

/**
 * 用户工具类
 *
 * @author 李旭光
 * @version 2018年03月05日  10:43
 */
public class WechatUserUtil {

    public static final String USER_OPEN_ID = "USER_OPEN_ID";

    //翼多会议
    public static final String USER_SOURCE_EDOCTOR = "1";
    //其他来源
    public static final String USER_SOURCE_OTHER = "2";

    /**
     * 微信user id
     */
    public static final String WECHAT_USER_ID_SESSION = "WECHAT_USER_ID_SESSION";

    /**
     * 取得session
     */
    private static HttpSession getSession() {
        return WebUtil.getOrCreateSession();
    }

    /**
     * 设置微信用户的openId
     */
    public static void initOpenId(String openId) {
        HttpSession session = getSession();
        Object o = session.getAttribute(USER_OPEN_ID);
        if (o != null) {
            session.removeAttribute(USER_OPEN_ID);
        }
        session.setAttribute(USER_OPEN_ID, openId);
    }

    /**
     * 存储微信user id
     *
     * @param wechatUserId
     */
    public static void saveWechatUserId(Long wechatUserId) {
        HttpSession session = getSession();
        Object o = session.getAttribute(WECHAT_USER_ID_SESSION);
        if (o != null) {
            session.removeAttribute(WECHAT_USER_ID_SESSION);
        }
        session.setAttribute(WECHAT_USER_ID_SESSION, wechatUserId);
    }

    public static String getOpenId() {
        HttpSession session = getSession();
        Object o = session.getAttribute(USER_OPEN_ID);
        if (o != null) {
            return o.toString();
        }
        return null;
    }

    /**
     * 是否有openId
     */
    public static boolean hasOpenId() {
        HttpSession session = getSession();
        Object o = session.getAttribute(USER_OPEN_ID);
        return o != null;
    }

    public static void cleartOpenId() {
        if (hasOpenId()) {
            getSession().removeAttribute(USER_OPEN_ID);
        }
    }

}