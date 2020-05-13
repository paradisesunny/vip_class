package com.kingyee.vipclass.controller.web;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 * @date 2019/11/28
 * @desc 入口
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = {"/index", ""})
    public String index(ModelMap mm) {
        log.info("访问首页");
        return "web/index";
    }

    @RequestMapping(value = {"/begin"})
    public String begin(ModelMap mm,String username,String password) {
        log.info("登录submit");
        System.out.println(username);
        System.out.println(password);

        return "web/index";
    }

    /**
     * 模拟扫描带参二维码之后翼多的操作
     *
     * @param request
     * @return
     */
    @RequestMapping("meet")
    @ResponseBody
    public String meet(HttpServletRequest request, @RequestBody String requestBody) {
        WxMpXmlMessage mpXmlMessage = null;
        try {
            if (null != requestBody) {
                mpXmlMessage = WxMpXmlMessage.fromXml(requestBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String meetMsg = mpXmlMessage.toString();
        System.out.println(meetMsg);
        return "success";
    }

}