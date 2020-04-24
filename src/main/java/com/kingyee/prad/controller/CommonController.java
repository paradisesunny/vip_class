package com.kingyee.prad.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.common.ServletUtil;
import com.kingyee.prad.entity.Hospital;
import com.kingyee.prad.entity.PradUser;
import com.kingyee.prad.entity.SmsCode;
import com.kingyee.prad.service.IPradUserService;
import com.kingyee.prad.service.impl.HospitalServiceImpl;
import com.kingyee.prad.service.impl.SmsCodeServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("common")
public class CommonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private IPradUserService userService;

    @Autowired
    private SmsCodeServiceImpl smsCodeService;

    @Autowired
    private HospitalServiceImpl hospitalService;

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

    /**
     * 根据父id查找县市级医院
     *
     * @param fatherId:当fatherId为0时显示所有县 不为0时逐级查询
     * @return
     */
    @ResponseBody
    @RequestMapping("getChild")
    public JsonNode getChild(Long fatherId) {
        try {
            QueryWrapper<Hospital> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Hospital::getParentId, fatherId);
            List<Hospital> list = hospitalService.list(queryWrapper);
            return JacksonMapper.newDataInstance(list);
        } catch (Exception e) {
            String errMsg = "获取子节点失败";
            e.printStackTrace();
            LOGGER.error(errMsg, e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }

}
