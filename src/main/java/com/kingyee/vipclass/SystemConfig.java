
package com.kingyee.vipclass;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 李旭光
 * @version 2019年10月16日  15:54
 */
@ConfigurationProperties(prefix = "system")
public class SystemConfig {


    private String domain;

    private String version;

    private boolean debug;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}