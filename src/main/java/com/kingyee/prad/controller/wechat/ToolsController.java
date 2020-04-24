package com.kingyee.prad.controller.wechat;


import com.kingyee.common.util.EncryptUtil;
import com.kingyee.prad.common.security.UserUtil;
import com.kingyee.prad.wx.mp.WechatUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author nmy
 * @date 2020/3/10
 * @desc 会议链接
 */
@Controller
@RequestMapping(value = "/wechat/tools")
public class ToolsController {
    private static final Logger logger = LoggerFactory.getLogger(ToolsController.class);


	/**
	 * 指南url
	 */
	@Value("${guide.url}")
	private String guideUrl;

	/**
	 * 会议url
	 */
	@Value("${meeting.url}")
	private String meetingUrl;


	/**
	 * 医学工具
	 */
	@RequestMapping("index")
	public String index(){
		return "wechat/tools";
	}

	/**
	 * 指南
	 */
	@RequestMapping("guide")
	public String guide(HttpServletResponse response){
		String url = "";
		String parm = "projectId=5userId="+UserUtil.getOpenid()+"email=dept=userName=key=OKKNECZKImdLFPdW";
		try {
			String token = EncryptUtil.getMD5Value(parm);
			System.out.println(token);
			url = guideUrl+"?projectId=5&userId="+UserUtil.getOpenid()+"&email=&dept=&userName=&token="+token;
			response.sendRedirect(url);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("跳转指南时出错。" , e);
			return "error/error";
		}
		return null;
	}



	/**
	 * 会议
	 */
	@RequestMapping("meeting")
	public String meeting(){
		logger.info(meetingUrl + "?OpenID="+ WechatUserUtil.getOpenId());
		return "redirect:"+meetingUrl + "?OpenID="+WechatUserUtil.getOpenId();
	}
}
