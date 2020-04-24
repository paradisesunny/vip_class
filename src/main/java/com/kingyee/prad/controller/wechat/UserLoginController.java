package com.kingyee.prad.controller.wechat;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.common.EmtUtil;
import com.kingyee.prad.common.MedliveUtil;
import com.kingyee.prad.common.security.UserModel;
import com.kingyee.prad.common.security.UserUtil;
import com.kingyee.prad.entity.*;
import com.kingyee.prad.model.MedliveUserBean;
import com.kingyee.prad.service.IPradUserService;
import com.kingyee.prad.service.IWechatUserService;
import com.kingyee.prad.service.impl.HospitalServiceImpl;
import com.kingyee.prad.service.impl.SmsCodeServiceImpl;
import com.kingyee.prad.wx.mp.WechatUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/wechat/userLogin")
public class UserLoginController {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    @Autowired
    private IPradUserService userService;

    @Autowired
    private SmsCodeServiceImpl smsCodeService;

    @Autowired
    private HospitalServiceImpl hospitalService;

    @Autowired
    private IWechatUserService wechatUserService;

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
    public String doLogin(ModelMap mm) {
    	//医脉通登录
    	String callBackUrl = domain+"/wechat/userLogin/medlive_login_init";
		callBackUrl = EmtUtil.unicodeToString(callBackUrl);
		String medLoginUrl = MedliveUtil.getMedLoginUrlInit(callBackUrl);
		mm.addAttribute("medliveLoginUrl",medLoginUrl);
        mm.addAttribute("goToReg",true);
		mm.addAttribute("isIndex", true);
        return "wechat/login";
    }

	/**
	 * 个人中心和登录页面之间的页面
	 *
	 * @return
	 */
	@RequestMapping("centerLogin")
	public String betweenCenterAndLogin() {
		return "wechat/centerLogin";
	}

    /**
     * 注册页面
     *
     * @return
     */
    @RequestMapping("toRegister")
    public String toRegister(ModelMap mm) {
        mm.addAttribute("goToReg",false);
		mm.addAttribute("isIndex", true);
        return "wechat/register";
    }

	/**
	 * 注册测试页面
	 *
	 * @return
	 */
	@RequestMapping("toReg")
	public String toReg(ModelMap mm) {
		return "wechat/regtest";
	}

    /**
     * 手机号 验证码 登录
     * @param mobile 手机号
     * @param code   验证码
     */
    @ResponseBody
    @RequestMapping("login")
    public JsonNode login4Mobile(String mobile, String code) {
        try {
            if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)) {
                return JacksonMapper.newErrorInstance("用户名或者验证码不能为空，请重试！");
            }
            QueryWrapper<PradUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PradUser::getPuPhone, mobile)
								.isNull(PradUser::getPuMedliveUserId);
            PradUser user = userService.getOne(queryWrapper, false);
            if (user == null) {
                return JacksonMapper.newErrorInstance("此手机号不存在或未注册，请重试！");
            }
            QueryWrapper<SmsCode> smsWrapper = new QueryWrapper<>();
            smsWrapper.lambda().eq(SmsCode::getScPhone, mobile)
                    .eq(SmsCode::getScCode, code)
                    .eq(SmsCode::getScType, smsCodeService.MOBILE_LOGIN)
                    .eq(SmsCode::getScIsUsed, 0)
                    .le(SmsCode::getScCreateDate, TimeUtil.dateTolong())
                    .ge(SmsCode::getScInvalidDate, TimeUtil.dateTolong());
            SmsCode dbCode = smsCodeService.getOne(smsWrapper, false);
            if (dbCode == null) {
                return JacksonMapper.newErrorInstance("验证码已失效或不存在！");
            }

            //取登录的用户的微信号与当前微信号判断
            QueryWrapper<WechatUser> listWrapper = new QueryWrapper<>();
            listWrapper.lambda().eq(WechatUser::getWuUserId, user.getPuId());
            WechatUser wu = wechatUserService.getOne(listWrapper, false);
            //如果微信号不同重新进行绑定
            if (wu != null) {
                if (!wu.getWuOpenid().equals(WechatUserUtil.getOpenId())) {
                    wu.setWuUserId(null);
                    wechatUserService.updateById(wu);
                    //用户和微信号绑定
                    listWrapper = new QueryWrapper<>();
                    listWrapper.lambda().eq(WechatUser::getWuOpenid, WechatUserUtil.getOpenId());
                    WechatUser wechatUser = wechatUserService.getOne(listWrapper, false);
                    wechatUser.setWuUserId(user.getPuId());
                    wechatUser.setWuUpdateTime(TimeUtil.dateTolong());
                    wechatUserService.updateById(wechatUser);
                }
            } else {
                //用户和微信号绑定
                QueryWrapper<WechatUser> wechatWrapper = new QueryWrapper<>();
                wechatWrapper.lambda().eq(WechatUser::getWuOpenid, WechatUserUtil.getOpenId());
                WechatUser wechatUser = wechatUserService.getOne(wechatWrapper, false);
                wechatUser.setWuUserId(user.getPuId());
                wechatUser.setWuCreateTime(TimeUtil.dateTolong());
                wechatUserService.updateById(wechatUser);
            }

            UserModel userModel = new UserModel();
            userModel.setWechatUser(wu);
            userModel.setUser(user);
            UserUtil.login(userModel);

            dbCode.setScIsUsed(1);
            smsCodeService.updateById(dbCode);
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
			queryWrapper.lambda().eq(PradUser::getPuPhone, user.getPuPhone());
			PradUser userDb = userService.getOne(queryWrapper, false);
			if (userDb != null) {
				return JacksonMapper.newErrorInstance("此手机号已经注册，请直接登录");
			}
            QueryWrapper<SmsCode> smsWrapper = new QueryWrapper<>();
            smsWrapper.lambda().eq(SmsCode::getScPhone, user.getPuPhone())
                    .eq(SmsCode::getScCode, code)
                    .eq(SmsCode::getScType, smsCodeService.MOBILE_REGISTER)
                    .eq(SmsCode::getScIsUsed, 0)
                    .le(SmsCode::getScCreateDate, TimeUtil.dateTolong())
                    .ge(SmsCode::getScInvalidDate, TimeUtil.dateTolong());
            SmsCode dbCode = smsCodeService.getOne(smsWrapper, false);
            if (dbCode == null) {
                return JacksonMapper.newErrorInstance("验证码已失效或不存在！");
            }
            user.setPuCreateTime(TimeUtil.dateTolong());
            userService.save(user);
            //用户和微信号绑定
            QueryWrapper<WechatUser> wechatWrapper = new QueryWrapper<>();
            wechatWrapper.lambda().eq(WechatUser::getWuOpenid, WechatUserUtil.getOpenId());
            WechatUser wechatUser = wechatUserService.getOne(wechatWrapper, false);
            wechatUser.setWuUserId(user.getPuId());
            wechatUser.setWuUpdateTime(TimeUtil.dateTolong());
            wechatUserService.updateById(wechatUser);
            dbCode.setScIsUsed(1);
            smsCodeService.updateById(dbCode);

            UserModel userModel = new UserModel();
            userModel.setWechatUser(wechatUser);
            userModel.setUser(user);
            UserUtil.login(userModel);

            return JacksonMapper.newDataInstance("");
        } catch (Exception e) {
            String errMsg = "注册异常，请重试";
            logger.error(e.getMessage(), e);
            return JacksonMapper.newErrorInstance(errMsg);
        }
    }


    /**
     * 完善个人信息 取医院
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getHospital", method = {RequestMethod.POST})
    public JsonNode getHospital(String provinces) {
        try {
            if (StringUtils.isEmpty(provinces)) {
                return JacksonMapper.newErrorInstance("省份为空");
            }
            String province = provinces.split(" ")[0];
            //取省份对应的code
            QueryWrapper<Hospital> provincesWrapper = new QueryWrapper<>();
            provincesWrapper.lambda().eq(Hospital::getName, province);
            Hospital hos = hospitalService.getOne(provincesWrapper, false);
            if (hos != null) {
                Long minPCode = hos.getCode();
                Long maxPCode = hos.getCode() + 9999;
                QueryWrapper<Hospital> hosWrapper = new QueryWrapper<>();
                hosWrapper.lambda().ge(Hospital::getParentId, minPCode)
                        .le(Hospital::getParentId, maxPCode)
                        .ge(Hospital::getId, 10000000);
                List<Hospital> hosList = hospitalService.list(hosWrapper);
                List<String> hosNames = new ArrayList<>();

                for (Hospital h : hosList) {
                    String name = h.getName();
                    hosNames.add(name);
                }
                return JacksonMapper.newDataInstance(hosNames);
            } else {
                return JacksonMapper.newDataInstance("");
            }
        } catch (Exception e) {
            String errMsg = "取医院异常";
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
        return "wechat/userConsent";
    }

	/**
	 * 登出
	 * @return
	 */
	@RequestMapping("loginout")
	public String loginout() {
		try {
			UserUtil.logout();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("前台用户退出失败，openid="+UserUtil.getOpenid() , e);
		}
		return "msg";
	}



	/**
	 * 医脉通登录init
	 * @return
	 */
	@RequestMapping(value = {"/medlive_login_init"})
	public String medliveLoginInit(HttpServletRequest request, RedirectAttributes model) {
		String ticket = request.getParameter("ticket");
		logger.info("ticket" + ticket);
		if (StringUtils.isEmpty(ticket)) {
			return "redirect:" + MedliveUtil.getMedLoginUrlInit(domain + "/wechat/userLogin/medlive_login_init");
		} else {
			model.addFlashAttribute("ticket", ticket);
			return "redirect:" + MedliveUtil.getMedLoginUrlSuccess(domain + "/wechat/userLogin/medlive_login");
		}
	}

	/**
	 * 医脉通登录
	 * @return
	 */
	@RequestMapping(value = {"/medlive_login"})
	public String medliveLogin(ModelMap mm, String userinfo, HttpServletRequest request, HttpServletResponse response) {
		String userInfoStr = request.getParameter("userinfo");
		Long provinceId = null;
		Long cityId = null;
		Long regionId = null;
		Long userId = null;
		Long now = System.currentTimeMillis();
		try {
			userinfo = MedliveUtil.decryptAES(userinfo);
			logger.info("=========userInfoStr=" + userinfo);
			MedliveUserBean userBean = new Gson().fromJson(userinfo, MedliveUserBean.class);
			if (!"0".equalsIgnoreCase(userBean.getUser_id())) {
				//用户微信信息
				QueryWrapper<WechatUser> wechatWrapper = new QueryWrapper<>();
				wechatWrapper.lambda().eq(WechatUser::getWuOpenid, WechatUserUtil.getOpenId());
				WechatUser wechatUser = wechatUserService.getOne(wechatWrapper, false);

				//获取本地用户信息
				QueryWrapper<PradUser> userWrapper = new QueryWrapper<>();
				userWrapper.lambda().eq(PradUser::getPuMedliveUserId, userBean.getUser_id());
				PradUser user = userService.getOne(userWrapper);

				//获取医脉通用户信息
				Long medliveUserId = Long.valueOf(userBean.getUser_id());
				PradUser medliveUser = userService.getUserByMedliveId(medliveUserId);
				if(medliveUser != null && medliveUser.getPuMedliveUserId() != null){
					QueryWrapper<Hospital> hospitalWrapper = new QueryWrapper<>();
					hospitalWrapper.lambda().eq(Hospital::getParentId,0)
							.like(Hospital::getName, medliveUser.getPuProvince());
					//查询省的id
					Hospital province = hospitalService.getOne(hospitalWrapper);
					if(province != null){
						provinceId = province.getId();
						hospitalWrapper = new QueryWrapper<>();
						hospitalWrapper.lambda().eq(Hospital::getParentId,provinceId)
												.like(Hospital::getName,medliveUser.getPuCity());
						//查询市的id
						Hospital city = hospitalService.getOne(hospitalWrapper);
						if(city != null){
							cityId = city.getId();
							hospitalWrapper = new QueryWrapper<>();
							hospitalWrapper.lambda().eq(Hospital::getParentId,cityId)
													.like(Hospital::getName,medliveUser.getPuRegion());
							//查询区县的id
							Hospital region = hospitalService.getOne(hospitalWrapper);
							if(region != null){
								regionId = region.getId();
							}
						}
					}

				}

				if(user == null || user.getPuId() == null){
					medliveUser.setPuProvinceId(provinceId);
					medliveUser.setPuCityId(cityId);
					medliveUser.setPuRegionId(regionId);
					medliveUser.setPuCreateTime(now);
					userService.save(medliveUser);
					userId = medliveUser.getPuId();
				}else{
					user.setPuName(medliveUser.getPuName());
					user.setPuProvince(medliveUser.getPuProvince());
					user.setPuCity(medliveUser.getPuCity());
					user.setPuRegion(medliveUser.getPuRegion());
					user.setPuProvinceId(provinceId);
					user.setPuCityId(cityId);
					user.setPuRegionId(regionId);
					user.setPuMedliveUserThumb(medliveUser.getPuMedliveUserThumb());
					user.setPuDept(medliveUser.getPuDept());
					user.setPuProfessional(medliveUser.getPuProfessional());
					user.setPuHospital(medliveUser.getPuHospital());
					user.setPuUpdateTime(now);
					userService.updateById(user);
					userId = user.getPuId();
				}

				//用户和微信号绑定
				wechatUser.setWuUserId(userId);
				wechatUser.setWuUpdateTime(now);
				wechatUserService.updateById(wechatUser);

				//登录，保存session，cookie
				UserModel userModel = new UserModel();
				userModel.setWechatUser(wechatUser);
				if(user == null || user.getPuId() == null){
					userModel.setUser(medliveUser);
				}else{
					userModel.setUser(user);
				}
				UserUtil.login(userModel);

				// 医脉通用户积分详细
				/*JsonElement result = pointBillService.addRegisterPoints(userId);
				logger.error("注册[医脉通]积分,医脉通用户，id=" + userId + ",结果" + result.toString());*/
				return "redirect:" + UserUtil.getLoginReferer("/wechat/userCenter/center");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("医脉通登陆出错！", e);
			mm.addAttribute("msg", "医脉通登陆出错！");
			return "error";
		}
		return "redirect:" + MedliveUtil.getMedLoginUrlInit(domain + "/wechat/userLogin/medlive_login_init");
	}

}
