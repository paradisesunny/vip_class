package com.kingyee.vipclass.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.vipclass.common.ServletUtil;
import com.kingyee.vipclass.entity.PradUser;
import com.kingyee.vipclass.entity.SmsCode;
import com.kingyee.vipclass.service.IPradUserService;
import com.kingyee.vipclass.service.impl.SmsCodeServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("common")
public class CommonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private IPradUserService userService;

    @Autowired
    private SmsCodeServiceImpl smsCodeService;

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @param type   ：smsCodeService.MOBILE_REGISTER 注册验证码
     *               ： smsCodeService.MOBILE_LOGIN 登录验证码
     * @return
     */
    @ResponseBody
    @RequestMapping("sendEmailCode")
    public JsonNode sendEmailCode(String email, String type, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty(email)) {
                return JacksonMapper.newErrorInstance("邮箱不能为空，请重试！");
            }
            if (StringUtils.isEmpty(type)) {
                return JacksonMapper.newErrorInstance("type参数为空，请重试！");
            }
            QueryWrapper<PradUser> registerWrapper = new QueryWrapper<>();
            registerWrapper.lambda().eq(PradUser::getPuEmail, email);
            PradUser user = userService.getOne(registerWrapper, false);

            if (smsCodeService.EMAIL_REGISTER.equals(type)) {
                //注册类型时 验证邮箱是否已经使用过
                if (user != null) {
                    return JacksonMapper.newErrorInstance("此邮箱已被注册过，请重试！");
                }
            } else if (smsCodeService.EMAIL_LOGIN.equals(type)) {
                //登录时 验证手机号是否存在
                if (user == null) {
                    return JacksonMapper.newErrorInstance("此邮箱不存在或未注册，请重试！");
                }
            } else if (smsCodeService.MOBILE_MODIFY.equals(type)) {
                //修改手机号时 验证手机号是否存在
                if (user != null) {
                    return JacksonMapper.newErrorInstance("此邮箱已被注册过，请重试！");
                }
            } else {
                //其他类型不能发送验证码
                return JacksonMapper.newErrorInstance("type参数类型不存在，请重试！");
            }
            return smsCodeService.sendToMail(request, email, type);
        } catch (Exception e) {
            String errMsg = "邮箱验证码发送异常";
            LOGGER.error(e.getMessage(), e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * 检验验证码是否正确
     *
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping("checkEmailCode")
    public JsonNode checkEmailCode(String code) {
        QueryWrapper<SmsCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SmsCode::getScCode, code)
                .eq(SmsCode::getScType, smsCodeService.EMAIL_MODIFY)
                .eq(SmsCode::getScIsUsed, 0)
                .le(SmsCode::getScCreateDate, TimeUtil.dateTolong())
                .ge(SmsCode::getScInvalidDate, TimeUtil.dateTolong());
        SmsCode dbCode = smsCodeService.getOne(queryWrapper, false);
        if (dbCode == null) {
            return JacksonMapper.newErrorInstance("验证码失效或不存在");
        }
        return JacksonMapper.newSuccessInstance();
    }

    /**
     * 发送验证码
     *
     * @param mobile 手机号
     * @param type   ：smsCodeService.MOBILE_REGISTER 注册验证码
     *               ： smsCodeService.MOBILE_LOGIN 登录验证码
     *               :smsCodeService.MOBILE_MODIFY 修改验证码
     * @return
     */
    @ResponseBody
    @RequestMapping("sendCode")
    public JsonNode sendCode(String mobile, String type, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty(mobile)) {
                return JacksonMapper.newErrorInstance("手机号不能为空，请重试！");
            }
            if (StringUtils.isEmpty(type)) {
                return JacksonMapper.newErrorInstance("type参数为空，请重试！");
            }
            if (smsCodeService.MOBILE_REGISTER.equals(type)) {
                //注册类型时 验证手机号是否已经使用过
                QueryWrapper<PradUser> registerWrapper = new QueryWrapper<>();
                registerWrapper.lambda().eq(PradUser::getPuPhone, mobile);
                PradUser user = userService.getOne(registerWrapper, false);
                if (user != null) {
                    return JacksonMapper.newErrorInstance("此手机号已被注册过，请重试！");
                }
            } else if (smsCodeService.MOBILE_LOGIN.equals(type)) {
                //登录时 验证手机号是否存在
                QueryWrapper<PradUser> loginWrapper = new QueryWrapper<>();
                loginWrapper.lambda().eq(PradUser::getPuPhone, mobile);
                PradUser user = userService.getOne(loginWrapper, false);
                if (user == null) {
                    return JacksonMapper.newErrorInstance("此手机号不存在或未注册，请重试！");
                }
            } else if (smsCodeService.MOBILE_MODIFY.equals(type)) {
                //修改手机号时 验证手机号是否存在
                QueryWrapper<PradUser> modifyWrapper = new QueryWrapper<>();
                modifyWrapper.lambda().eq(PradUser::getPuPhone, mobile);
                PradUser user = userService.getOne(modifyWrapper, false);
                if (user != null) {
                    return JacksonMapper.newErrorInstance("此手机号已被注册过，请重试！");
                }
            } else {
                //除微信绑定外的其他类型不能发送验证码
                return JacksonMapper.newErrorInstance("type参数类型不存在，请重试！");
            }
            return smsCodeService.saveCode(ServletUtil.getIpAddress(request), mobile, type, null);
        } catch (Exception e) {
            String errMsg = "手机号登录验证码发送异常";
            LOGGER.error(e.getMessage(), e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

    /**
     * 检验验证码是否正确
     *
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping("checkCode")
    public JsonNode checkCode(String code) {
        QueryWrapper<SmsCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SmsCode::getScCode, code)
                .eq(SmsCode::getScType, smsCodeService.MOBILE_MODIFY)
                .eq(SmsCode::getScIsUsed, 0)
                .le(SmsCode::getScCreateDate, TimeUtil.dateTolong())
                .ge(SmsCode::getScInvalidDate, TimeUtil.dateTolong());
        SmsCode dbCode = smsCodeService.getOne(queryWrapper, false);
        if (dbCode == null) {
            return JacksonMapper.newErrorInstance("验证码失效或不存在");
        }
        return JacksonMapper.newSuccessInstance();
    }

}
