package com.kingyee.vipclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.mail.Mail;
import com.kingyee.common.mail.MailHelper;
import com.kingyee.common.util.RandomUtil;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.common.util.ValidateUtil;
import com.kingyee.vipclass.common.security.UserUtil;
import com.kingyee.vipclass.common.sms.SmsTplUtil;
import com.kingyee.vipclass.entity.SmsCode;
import com.kingyee.vipclass.mapper.SmsCodeMapper;
import com.kingyee.vipclass.service.ISmsCodeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /**
     * emailSmtp
     */
    @Value("${system.email.smtp}")
    private String emailSmtp;
    /**
     * emailFromAddress
     */
    @Value("${system.email.fromAddress}")
    private String emailFromAddress;
    /**
     * emailFromPwd
     */
    @Value("${system.emai.fromPwd}")
    private String emailFromPwd;
    /**
     * emailSwich
     */
    @Value("${system.email.swich}")
    private String emailSwich;


    /***************type 类型  start*******************************/
    //邮箱登录用验证码
    public static String EMAIL_LOGIN = "email_login";
    //邮箱注册用验证码
    public static String EMAIL_REGISTER = "email_register";
    //修改个人信息用验证码
    public static String EMAIL_MODIFY = "email_modify";
    /***************type 类型  end*******************************/


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


    /**
     * 发送至邮箱
     */
    public JsonNode sendToMail(HttpServletRequest request,String email,String type) throws UnsupportedEncodingException {
        SmsCode smsCode = new SmsCode();
        smsCode.setScEmail(email);
        //生成验证码
        String code = RandomUtil.generateIntString(6);
        smsCode.setScCode(code);
        smsCode.setScCreateDate(System.currentTimeMillis());
        // 有效期10分钟
        smsCode.setScInvalidDate(System.currentTimeMillis() + 10 * 60 * 1000L);
        // 设置使用标志：未使用
        smsCode.setScIsUsed(0);
        // 发送验证码类型
        smsCode.setScType(type);
        // 验证码使用用户id 可能为空
        smsCode.setScUserId(Optional.ofNullable(UserUtil.getUserId()).orElse(null));

        // 邮件内容
        StringBuffer content = new StringBuffer();
        content.append("<p>亲，感谢您注册VIPCLASS，验证码为：</p>");
        content.append("<h3>"+code+"。</h3>");

        // 发邮件
        Mail mail = MailHelper.buildHtmlMail(emailSmtp, emailFromAddress,emailFromPwd);
        try {
            // 发件地址
            mail.setFromAddress(emailFromAddress);
            // 收件地址
            mail.setToAddress(new String[] { email });
            // 标题
            mail.setSubject("VIPCLASS验证码");
            // 内容
            mail.setContent(content.toString());
            if (emailSwich.equalsIgnoreCase("on")) {
                // 发送邮件
                MailHelper.sendMail(mail, "utf-8");
                smsCode.setScSendPrompt("邮件发送成功");
                smsCode.setScSendFlag(1);
                smsCodeMapper.insert(smsCode);
                return JacksonMapper.newSuccessInstance();
            } else {
                logger.info("发送提醒邮件的开关已关闭！如需重新打开，请修改配置文件application-xxxxx.properties！");
                return JacksonMapper.newErrorInstance("发送提醒邮件的开关已关闭！如需重新打开，请修改配置文件configConst.properties！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errmsg ="给" + email + "发送邮件失败！ type="+type;
            logger.error(errmsg);
            return JacksonMapper.newErrorInstance("发送失败");
        }
    }
}
