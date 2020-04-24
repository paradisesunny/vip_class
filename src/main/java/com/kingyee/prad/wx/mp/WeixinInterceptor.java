package com.kingyee.prad.wx.mp;

import com.kingyee.common.util.http.HttpUtil;
import com.kingyee.prad.SystemConfig;
import com.kingyee.prad.common.security.UserModel;
import com.kingyee.prad.common.security.UserUtil;
import com.kingyee.prad.entity.PradUser;
import com.kingyee.prad.entity.WechatUser;
import com.kingyee.prad.service.impl.PradUserServiceImpl;
import com.kingyee.prad.service.impl.WechatUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信授权拦截器
 *
 * @author 李旭光
 * @version 2018年04月16日  15:39
 */
@Component
public class WeixinInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private MyWeixinHelper myWeixinHelper;

    @Autowired
    protected WechatUserServiceImpl wechatUserService;
    @Autowired
    protected PradUserServiceImpl userService;

    @Autowired
    private SystemConfig systemConfig;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        /*if (systemConfig.isDebug()) {
            WechatUserUtil.initOpenId("");
            return super.preHandle(request, response, handler);
        }*/
		String requestUri = request.getRequestURI();
        if (!WechatUserUtil.hasOpenId()) {
            String url = myWeixinHelper.oauthUrl(HttpUtil.getBasePath(request) + "wx/portal/getOpenId?url=" + HttpUtil.getFullUrl(request));
            // 微信用户调用oauth登录
            response.sendRedirect(url);
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}