package com.kingyee.prad.common.security;

import com.kingyee.prad.entity.PradUser;
import com.kingyee.prad.entity.WechatUser;

import java.io.Serializable;

public class UserModel implements Serializable{

    WechatUser wechatUser;
    PradUser user;

    public WechatUser getWechatUser() {
        return wechatUser;
    }

    public void setWechatUser(WechatUser wechatUser) {
        this.wechatUser = wechatUser;
    }

    public PradUser getUser() {
        return user;
    }

    public void setUser(PradUser user) {
        this.user = user;
    }
}
