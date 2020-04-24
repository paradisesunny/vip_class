package com.kingyee.prad.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kingyee.common.util.EncryptUtil;
import com.kingyee.prad.common.security.AdminUserUtil;
import com.kingyee.prad.common.security.UserModel;
import com.kingyee.prad.common.security.UserUtil;
import com.kingyee.prad.entity.SysUser;
import com.kingyee.prad.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

/**
 * @author dxl
 * @version 2019年10月24日  13:57
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminLoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminLoginController.class);

    private final ISysUserService sysUserService;

    public AdminLoginController(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @RequestMapping(value = {"", "/login"}, method = RequestMethod.GET)
    public String loginInit() {
        return "/admin/login";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(ModelMap mm, String name, String password, HttpServletRequest request) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            mm.addAttribute("errMsg", "用户名或密码不能为空！");
            return "/admin/login";
        }
        try {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>()
                    .eq("su_login_name", name)
                    .eq("su_password", EncryptUtil.getSHA256Value(password));
            SysUser user = sysUserService.getOne(queryWrapper, false);
            if (user == null) {
                mm.addAttribute("errMsg", "用户名或密码输入错误！");
                return "/admin/login";
            }
            AdminUserUtil.login(user);

            return "redirect:index";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            mm.addAttribute("errMsg", "出错了~");
            return "/admin/login";
        }
    }


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/admin/index";
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        try {
			AdminUserUtil.logout();
        } catch (Exception e) {
            LOGGER.error("退出操作失败" ,e);
            return "/error/error";
        }
        return "/admin/login";
    }

    public static void main(String[] args) {
        try {
            System.out.println(EncryptUtil.getSHA256Value("1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}