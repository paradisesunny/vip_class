package com.kingyee.prad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.RandomUtil;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.common.util.ValidateUtil;
import com.kingyee.prad.common.sms.SmsTplUtil;
import com.kingyee.prad.entity.SmsCode;
import com.kingyee.prad.mapper.SmsCodeMapper;
import com.kingyee.prad.service.ISmsCodeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 短信验证码 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-11-08
 */
@Service
public class SmsCodeServiceImpl extends ServiceImpl<SmsCodeMapper, SmsCode> implements ISmsCodeService {

    private final static Logger logger = LoggerFactory.getLogger(SmsCodeServiceImpl.class);

    @Autowired
    private SmsCodeMapper smsCodeMapper;



    /***************type 类型  start*******************************/
    //手机号登录用验证码
    public static String MOBILE_LOGIN = "mobile_login";
    //手机号注册用验证码
    public static String MOBILE_REGISTER = "mobile_register";
    //修改个人信息用验证码
    public static String MOBILE_MODIFY = "mobile_modify";
    /***************type 类型  end*******************************/


    /**
     * 保存验证码功能
     * 10001 您的验证码是【Value1】，在【Value2】分钟内输入有效。【Value3】
     * 1.一个手机每天最多三回
     * 2.60秒内，不允许重复发送
     *
     * @param ip
     * @param phone
     */
    public JsonNode saveCode(String ip, String phone, String type, Long userId) throws Exception {
        if (StringUtils.isEmpty(phone)) {
            return JacksonMapper.newErrorInstance("手机号码不存在");
        }
        // 如果不为手机号
        if (!ValidateUtil.isMobile(phone)) {
            return JacksonMapper.newErrorInstance("请输入正确的手机号码");
        }
        // 1.一个手机每天最多三回
        Long count = getTodayCodeByPhone(phone, type);
        if (count != null && count >= 3) {
            return JacksonMapper.newErrorInstance("发送次数已达上限，请明日再试。");
        }
        // 2.60秒内，不允许重复发送
        QueryWrapper<SmsCode> lastSmsWrapper =  new QueryWrapper<SmsCode>()
                .eq("sc_phone", phone)
                .eq("sc_type",type)
                .orderByDesc("sc_create_date")
                .last("limit 1");
        SmsCode smsCode = smsCodeMapper.selectOne(lastSmsWrapper);
        if (smsCode != null && smsCode.getScCode() != null) {
            Long date = System.currentTimeMillis() - smsCode.getScCreateDate();
            if (date == null || date < 30000) {
                return JacksonMapper.newErrorInstance("您发送的太频繁了，请稍后再试。");
            }
        }
        // 3.一个ip每天最多10回
        count = getTodayCodeByIp(ip, type);
        if (count != null && count >= 10) {
            return JacksonMapper.newErrorInstance("此IP已超过了最大限制，请明日再试。");
        }

        smsCode = new SmsCode();
        smsCode.setScPhone(phone);
        //生成验证码
        String code = RandomUtil.generateIntString(6);
        smsCode.setScCode(code);
        smsCode.setScCreateDate(System.currentTimeMillis());
        // 有效期30分钟
        smsCode.setScInvalidDate(System.currentTimeMillis() + 30 * 60 * 1000L);
        // 设置使用标志：未使用
        smsCode.setScIsUsed(0);
        // 发送验证码类型
        smsCode.setScType(type);
        // 验证码使用用户id 可能为空
        smsCode.setScUserId(userId);

        List<String> values = new ArrayList<>();
        values.add(smsCode.getScCode());
        values.add("30");
        values.add("【探泌新天地平台】");
        JsonNode obj = SmsTplUtil.sendSms(phone, "10000", values);

        if (obj.get("success").asText("").equals("true")) {
            smsCode.setScSendPrompt("短信发送成功");
            smsCode.setScSendFlag(1);
            smsCodeMapper.insert(smsCode);
            return JacksonMapper.newSuccessInstance();
        } else {
            String errorMsg = null;
            if (obj != null && obj.has("msg")) {
                errorMsg = obj.get("msg").asText("");
                logger.error("发送验证码失败==========="+errorMsg);
            }
            smsCode.setScSendPrompt("短信发送失败" + (errorMsg != null ? "," + errorMsg : ""));
            smsCode.setScSendFlag(0);
            smsCodeMapper.insert(smsCode);
            return JacksonMapper.newErrorInstance("短信发送失败，请稍后再试。");
        }
    }

    /**
     * 取得当天ip的短信数据
     *
     * @param ip
     * @return
     */
    private Long getTodayCodeByIp(String ip, String type) throws Exception {
        Long now = TimeUtil.dateToLong("yyyy-MM-dd");
        Long startDate = now;
        Long endDate = now + 24 * 60 * 60 * 1000L;
        QueryWrapper<SmsCode> ipSmsWrapper =  new QueryWrapper<SmsCode>()
                .eq("sc_ip_address", ip)
                .eq("sc_type",type)
                .ge("sc_create_date",startDate )
                .le("sc_create_date",endDate );
        Integer count = smsCodeMapper.selectCount(ipSmsWrapper);
        return count.longValue();
    }

    /**
     * 取得当天手机的短信数据
     *
     * @param phone
     * @return
     */
    private Long getTodayCodeByPhone(String phone, String type) throws Exception {
        Long now = TimeUtil.dateToLong("yyyy-MM-dd");
        Long startDate = now;
        Long endDate = now + 24 * 60 * 60 * 1000L;
        QueryWrapper<SmsCode> phoneSmsWrapper =  new QueryWrapper<SmsCode>()
                .eq("sc_phone", phone)
                .eq("sc_type",type)
                .ge("sc_create_date",startDate )
                .le("sc_create_date",endDate );
        Integer count = smsCodeMapper.selectCount(phoneSmsWrapper);
        return count.longValue();
    }
}
