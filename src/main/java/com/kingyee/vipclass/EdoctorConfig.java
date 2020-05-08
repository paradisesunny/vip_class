
package com.kingyee.vipclass;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 李旭光
 * @version 2019年10月16日  15:54
 */
@ConfigurationProperties(prefix = "edoctor")
public class EdoctorConfig {


    private String scanUrl;

    public String getScanUrl() {
        return scanUrl;
    }

    public void setScanUrl(String scanUrl) {
        this.scanUrl = scanUrl;
    }
}