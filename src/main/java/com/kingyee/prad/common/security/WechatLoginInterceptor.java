package com.kingyee.prad.common.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.spring.mvc.WebUtil;
import com.kingyee.prad.SystemConfig;
import com.kingyee.prad.entity.PradUser;
import com.kingyee.prad.entity.WechatUser;
import com.kingyee.prad.service.impl.PradUserServiceImpl;
import com.kingyee.prad.service.impl.WechatUserServiceImpl;
import com.kingyee.prad.wx.mp.WechatUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringReader;

/**
 * 医生登录拦截器
 * Created by lm on 2019/1/24.
 */
@Component
public class WechatLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    protected WechatUserServiceImpl wechatUserService;
    @Autowired
    protected PradUserServiceImpl userService;
    @Autowired
    private SystemConfig systemConfig;

    @Value("${system.debug}")
    private String debug;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        // 开发模式下
        /*if (debug.equals("true")){
            // 本地测试
            WechatUser wechatUser = wechatUserService.getWechatUserByOpenid("odqj6spyu1MZPhBiplaDO8uQ6THs");
            WechatUserUtil.saveWechatUserId(wechatUser.getWuId());
            UserUtil.login(userService.getById(wechatUser.getWuUserId()));
        }else {*/
            if (UserUtil.hasLogin()){
                WechatUser wechatUser = wechatUserService.getWechatUserByOpenid(WechatUserUtil.getOpenId());
                if (wechatUser != null && wechatUser.getWuUserId() != null) {
                    UserModel userModel = new UserModel();
                    userModel.setWechatUser(wechatUser);
                    if(wechatUser.getWuUserId() != null){
                        PradUser user = userService.getById(wechatUser.getWuUserId());
                        if(user != null){
                            userModel.setUser(user);
                        }
                    }
                    UserUtil.login(userModel);
                }
            }else{
				if (isAjaxRequest(request)) {
					// 如果是异步请求 返回权限不足信息
					response.setContentType("text/plain;");
					response.setCharacterEncoding("UTF-8");
					response.setHeader("Content-Disposition", "inline");
					JsonNode json =  JacksonMapper.newErrorInstance("请登录后再操作");
					PrintWriter writer = response.getWriter();
					StringReader reader = null;
					try {
						reader = new StringReader(json.toString());
						char[] buffer = new char[1024];
						int charRead = 0;
						while ((charRead = reader.read(buffer)) != -1) {
							writer.write(buffer, 0, charRead);
						}
					} finally {
						if (reader != null)
							reader.close();
						if (writer != null) {
							writer.flush();
							writer.close();
						}
					}
				}else{
					//未登录
					WebUtil.setSessionAttribute(UserUtil.REFERER, requestUri);
					System.out.println(requestUri);
					if(StringUtils.isNotEmpty(requestUri) && requestUri.indexOf("userCenter") >= 0){
						response.sendRedirect("/wechat/userLogin/centerLogin");
					}else{
						response.sendRedirect("/wechat/userLogin/doLogin");
					}
				}
				return false;
            }
//        }
        return super.preHandle(request, response, handler);
    }

	/**
	 * 判断请求是否是Ajax
	 * @param request
	 * @return
	 */
	private boolean isAjaxRequest(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");
		if (header != null && "XMLHttpRequest".equals(header)){
			return true;
		}else{
			return false;
		}
	}
}