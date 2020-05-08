package com.kingyee.vipclass.common;

import com.kingyee.common.util.HttpClientUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by zhl on 2019/12/9.
 */
@Component
public class EDoctorUtil {
    /**
     * 发送扫码数据给翼多
     *
     * @param httpUrl
     * @param params
     * @return
     */
    @Async
    public String sendScanData(String httpUrl, String params) {
        return HttpClientUtil.getInstance().sendHttpPost4Xml(httpUrl, params);
    }
}
