package com.kingyee.vipclass.common.security;

import com.kingyee.vipclass.entity.SysUser;

import java.io.Serializable;

public class AdminUserModel implements Serializable{
	private Long id;
	private String name;
	private String showName;
	private Long role;

	private SysUser user;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShowName() { return showName; }
	public void setShowName(String showName) { this.showName = showName; }
	public Long getRole() { return role; }
	public void setRole(Long role) { this.role = role; }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }
}
