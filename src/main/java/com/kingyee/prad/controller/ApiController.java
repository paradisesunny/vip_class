package com.kingyee.prad.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.AesUtil;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.wx.mp.config.WxMpProperties;
import com.kingyee.prad.wx.mp.controller.WxPortalController;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 翼多接口
 *
 * @author 张烨
 * @version：2019-11-28 17:43
 */
@RestController
@RequestMapping("api")
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(WxPortalController.class);

    private final WxMpService wxService;

    public ApiController(WxMpService wxService) {
        this.wxService = wxService;
    }

    @Autowired
    private WxMpProperties wxMpProperties;

    /**
     * 获取AccessToken
     *
     * @return
     */
    @RequestMapping("getAccessToken/{appid}")
    @ResponseBody
    public JsonNode getAccessToken(@PathVariable String appid, Boolean forceRefresh) {
        try {
            if (StringUtils.isEmpty(appid) || !wxMpProperties.getAppId().equals(appid)) {
                return JacksonMapper.newErrorInstance("未找到对应appid=" + appid);
            }
            if (null == forceRefresh) {
                forceRefresh = false;
            }
            String accessToken = wxService.getAccessToken(forceRefresh);

            Map<String, Object> map = new HashMap<>();
            map.put(JacksonMapper.SUCCESS, true);
            map.put("access_token", AesUtil.encode(accessToken));
            map.put("expires_time", TimeUtil.longToString(wxService.getWxMpConfigStorage().getExpiresTime(),
                    TimeUtil.FORMAT_DATETIME_FULL));
            logger.info("获取accessToken={}", accessToken);
            return JacksonMapper.newJson(map);
        } catch (WxErrorException e) {
            e.printStackTrace();
            logger.error("获取AccessToken出错了！", e);
            return JacksonMapper.newErrorInstance("获取AccessToken出错了！");
        }
    }
}
