// ======================================
// Project Name:ninlaro
// Package Name:com.kingyee.isa.common.security
// File Name:UserUtil.java
// Create Date:2018年03月05日  10:43
// ======================================
package com.kingyee.prad.common.security;

import com.kingyee.common.spring.mvc.WebUtil;
import com.kingyee.common.util.CookieUtil;
import com.kingyee.common.util.RC4Util;
import com.kingyee.prad.entity.PradUser;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

/**
 * 微信用户工具类
 *
 * @author 李旭光
 * @version 2018年03月05日  10:43
 */
public class UserUtil {

    // 用户登录后，用户信息
    public static final String USER_LOGIN_SESSION = "USER_LOGIN_SESSION";
    // 用户未登录时记录身份
    public static final String USER_TYPE_SESSION = "USER_TYPE_SESSION";
    public static final String USER_SESSION_NAME = "SEC_USER_SESSION_NAME";
    public static final String COOKIE_AUTOLOGIN = "SEC_COOKIE_AUTOLOGIN";
    public static final String EMT_USER_COOKIE_NAME = "EMT_USER_COOKIE_NAME";
    public static final int COOKIE_TIME = 30 * 24 * 60 * 60;
    public static final String REFERER = "SEC_REFERER";
    public static final String SEC_USER_SOURCE = "SEC_USER_SOURCE";
    //医生身份
    public static final String DOCTOR = "doctor";
    //代表身份
    public static final String SALESMAN = "salesman";

    /**
     * 判断用户是否登录
     */
    public static boolean hasLogin() {
        return getSession().getAttribute(USER_SESSION_NAME) != null;
    }


    /**
     * 取得session
     */
    private static HttpSession getSession() {
        return WebUtil.getOrCreateSession();
    }

    /**
     * 取得user id
     */
    public static Long getUserId() {
        if (hasLogin()) {
            UserModel userModel = (UserModel) getSession().getAttribute(USER_SESSION_NAME);
            return userModel.getUser().getPuId();
        }
        return null;
    }

	/**
	 * 取得openid
	 */
	public static String getOpenid() {
		if (hasLogin()) {
			UserModel userModel = (UserModel) getSession().getAttribute(USER_SESSION_NAME);
			return userModel.getWechatUser().getWuOpenid();
		}
		return "游客";
	}

    /**
     * 取得用户姓名
     */
    public static String getUserName() {
        if (hasLogin()) {
            UserModel userModel = (UserModel) getSession().getAttribute(USER_SESSION_NAME);
            return userModel.getUser().getPuName();
        }
        return "游客";
    }

    /**
     * 取得用户微信昵称
     */
    public static String getWechatUserName() {
        if (hasLogin()) {
            UserModel userModel = (UserModel) getSession().getAttribute(USER_SESSION_NAME);
            return userModel.getWechatUser().getWuNickname();
        }
        return "游客";
    }

    /**
     * 取得用户头像
     *
     * @return
     */
    public static String getHeadImg() {
        if (hasLogin()) {
            UserModel userModel = (UserModel) getSession().getAttribute(USER_SESSION_NAME);
            return userModel.getWechatUser().getWuHeadimgurl();
        }
        return "游客";
    }

    /**
     * 取得用户model
     */
    public static UserModel getUserModel() {
        if (hasLogin()) {
            return (UserModel) getSession().getAttribute(USER_SESSION_NAME);
        }
        return null;
    }


    /**
     * 引用页
     *
     * @return
     */
    public static String getReferer() {
        return (String) getSession().getAttribute(REFERER);
    }

    /**
     * 引用页
     *
     * @return
     */
    public static void removeReferer() {
        getSession().removeAttribute(REFERER);
    }

    public static String getLoginReferer() {
        String url = (String) getSession().getAttribute(REFERER);
        if (StringUtils.isEmpty(url)) {
            url = "/index";
        }
        return url;
    }

    public static String getLoginReferer(String defaultUrl) {
        String url = (String) getSession().getAttribute(REFERER);
        if (StringUtils.isEmpty(url)) {
            if (StringUtils.isNotEmpty(defaultUrl)) {
                url = defaultUrl;
            } else {
                url = "/index";
            }
        }
        return url;
    }

    /**
     * 用户来源
     *
     * @return
     */
    public static String getUserSource() {
        String source = (String) getSession().getAttribute(SEC_USER_SOURCE);
        if (StringUtils.isEmpty(source)) {
            source = "";
        }
        return source;
    }

    /**
     * 做登录时，需要的一些操作
     */
    public static void login(UserModel userModel) {
        HttpSession session = getSession();
        Object o = session.getAttribute(USER_SESSION_NAME);
        if (o != null) {
            session.removeAttribute(USER_SESSION_NAME);
        }
        session.setAttribute(USER_SESSION_NAME, userModel);
    }

    /**
     * 做logout时，需要的一些操作
     */
    public static void logout() throws UnsupportedEncodingException {
        getSession().removeAttribute(USER_SESSION_NAME);
        setCookie(WebUtil.getRequest(), WebUtil.getResponse(), COOKIE_AUTOLOGIN, "", -1);
    }

    /**
     * 创造cookie.
     *
     * @throws UnsupportedEncodingException
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName, String userName, int maxAge) throws UnsupportedEncodingException {
        /* 自动登录 cookie设定 */
        String autoLogin = RC4Util.encode(userName);
        CookieUtil.setCookie(request, response, cookieName, autoLogin, maxAge);
    }

    /**
     * 获得cookie对应的内容
     *
     * @param request
     * @param key
     * @return Cookie
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie cookie = CookieUtil.getCookie(request, key);
        if (cookie != null) {
            String autoLogin = cookie.getValue();
            autoLogin = RC4Util.decode(autoLogin);
            return autoLogin;
        } else {
            return null;
        }
    }

    /**
     * 更新用户后更新session
     *
     * @param user
     */
    public static void updateSession(PradUser user) {
        if (hasLogin()) {
            getUserModel().setUser(user);
        }
    }
}