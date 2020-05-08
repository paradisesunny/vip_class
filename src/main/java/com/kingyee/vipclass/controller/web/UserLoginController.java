package com.kingyee.vipclass.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.EncryptUtil;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.vipclass.common.security.UserModel;
import com.kingyee.vipclass.common.security.UserUtil;
import com.kingyee.vipclass.entity.PradUser;
import com.kingyee.vipclass.entity.SmsCode;
import com.kingyee.vipclass.service.IPradUserService;
import com.kingyee.vipclass.service.impl.SmsCodeServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/web/userLogin")
public class UserLoginController {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    @Autowired
    private IPradUserService userService;

    @Autowired
    private SmsCodeServiceImpl smsCodeService;

	/**
	 * domain
	 */
	@Value("${system.domain}")
	private String domain;

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping("doLogin")
    public String doLogin() {
        return "web/login";
    }
    /**
     * 注册页面
     *
     * @return
     */
    @RequestMapping("doReg")
    public String toRegister() {
        return "web/register";
    }

    /**
     * 邮箱 密码 登录
     * @param email 邮箱
     * @param pwd   密码
     */
    @ResponseBody
    @RequestMapping("login")
    public JsonNode login4Mobile(String email, String pwd) {
        try {
            if (StringUtils.isEmpty(email) || StringUtils.isEmpty(pwd)) {
                return JacksonMapper.newErrorInstance("邮箱或者密码不能为空，请重试！");
            }
            QueryWrapper<PradUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PradUser::getPuEmail, email)
                                .eq(PradUser::getPuPwd, EncryptUtil.getSHA256Value(pwd))
								.isNull(PradUser::getPuMedliveUserId);
            PradUser user = userService.getOne(queryWrapper, false);
            if (user == null) {
                return JacksonMapper.newErrorInstance("此邮箱不存在或未注册，请重试！");
            }
            UserModel userModel = new UserModel();
            userModel.setUser(user);
            UserUtil.login(userModel);
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            String errMsg = "手机号验证码登录异常，请重试！";
            logger.error(e.getMessage(), e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * 注册
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("register")
    public JsonNode register(PradUser user, String code) {
        try {
			QueryWrapper<PradUser> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(PradUser::getPuEmail, user.getPuEmail());
			PradUser userDb = userService.getOne(queryWrapper, false);
			if (userDb != null) {
				return JacksonMapper.newErrorInstance("此邮箱已经注册，请直接登录");
			}
            QueryWrapper<SmsCode> smsWrapper = new QueryWrapper<>();
            smsWrapper.lambda().eq(SmsCode::getScEmail, user.getPuEmail())
                    .eq(SmsCode::getScCode, code)
                    .eq(SmsCode::getScType, smsCodeService.EMAIL_REGISTER)
                    .eq(SmsCode::getScIsUsed, 0)
                    .le(SmsCode::getScCreateDate, TimeUtil.dateTolong())
                    .ge(SmsCode::getScInvalidDate, TimeUtil.dateTolong());
            SmsCode dbCode = smsCodeService.getOne(smsWrapper, false);
            if (dbCode == null) {
                return JacksonMapper.newErrorInstance("验证码已失效或不存在！");
            }
            user.setPuPwd(EncryptUtil.getSHA256Value(user.getPuPwd()));
            user.setPuCreateTime(TimeUtil.dateTolong());
            userService.save(user);

            dbCode.setScUserId(user.getPuId());
            dbCode.setScIsUsed(1);
            smsCodeService.updateById(dbCode);

            UserModel userModel = new UserModel();
            userModel.setUser(user);
            UserUtil.login(userModel);
            return JacksonMapper.newSuccessInstance();
        } catch (Exception e) {
            String errMsg = "注册异常，请重试";
            logger.error(e.getMessage(), e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * 用户知情同意书
     * @return
     */
    @RequestMapping("userConsent")
    public String userConsent() {
        return "web/userConsent";
    }

	/**
	 * 登出
	 * @return
	 */
	@RequestMapping("logout")
	public String logout() {
		try {
			UserUtil.logout();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("前台用户退出失败，userId="+UserUtil.getUserId() , e);
		}
		return "redirect:index";
	}

}
