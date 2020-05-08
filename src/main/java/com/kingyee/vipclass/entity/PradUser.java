package com.kingyee.vipclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kingyee.common.util.TimeUtil;

import java.io.Serializable;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author Administrator
 * @since 2020-03-16
 */
public class PradUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "pu_id", type = IdType.AUTO)
    private Long puId;

    /**
     * 用户名
     */
    private String puName;

    /**
     * 邮箱
     */
    private String puEmail;

    /**
     * 密码
     */
    private String puPwd;

    /**
     * 省份
     */
    private String puProvince;

    /**
     * 城市
     */
    private String puCity;

    /**
     * 区县
     */
    private String puRegion;

    /**
     * 医院
     */
    private String puHospital;

    /**
     * 科室
     */
    private String puDept;

    /**
     * 职称
     */
    private String puProfessional;

    /**
     * 手机号
     */
    private String puPhone;

    /**
     * 创建时间
     */
    private Long puCreateTime;

    /**
     * 更新时间
     */
    private Long puUpdateTime;
    /**
     * 省份id
     */
    private Long puProvinceId;
    /**
     * 城市id
     */
    private Long puCityId;
    /**
     * 区县id
     */
    private Long puRegionId;
	/**
	 * 来源
	 */
	private String puFrom;
	/**
	 * 医脉通用户id
	 */
	private Long puMedliveUserId;
	/**
	 * 医脉通用户头像
	 */
	private String puMedliveUserThumb;
    /**
     * 总积分
     */
    private Integer puTotalScore;

    public String getPuEmail() {
        return puEmail;
    }

    public void setPuEmail(String puEmail) {
        this.puEmail = puEmail;
    }

    public String getPuPwd() {
        return puPwd;
    }

    public void setPuPwd(String puPwd) {
        this.puPwd = puPwd;
    }

    public Long getPuProvinceId() {
        return puProvinceId;
    }

    public void setPuProvinceId(Long puProvinceId) {
        this.puProvinceId = puProvinceId;
    }

    public Long getPuCityId() {
        return puCityId;
    }

    public void setPuCityId(Long puCityId) {
        this.puCityId = puCityId;
    }

    public Long getPuRegionId() {
        return puRegionId;
    }

    public void setPuRegionId(Long puRegionId) {
        this.puRegionId = puRegionId;
    }

    public Long getPuId() {
        return puId;
    }

    public void setPuId(Long puId) {
        this.puId = puId;
    }

    public String getPuName() {
        return puName;
    }

    public void setPuName(String puName) {
        this.puName = puName;
    }

    public String getPuProvince() {
        return puProvince;
    }

    public void setPuProvince(String puProvince) {
        this.puProvince = puProvince;
    }

    public String getPuCity() {
        return puCity;
    }

    public void setPuCity(String puCity) {
        this.puCity = puCity;
    }

    public String getPuRegion() {
        return puRegion;
    }

    public void setPuRegion(String puRegion) {
        this.puRegion = puRegion;
    }

    public String getPuHospital() {
        return puHospital;
    }

    public void setPuHospital(String puHospital) {
        this.puHospital = puHospital;
    }

    public String getPuDept() {
        return puDept;
    }

    public void setPuDept(String puDept) {
        this.puDept = puDept;
    }

    public String getPuProfessional() {
        return puProfessional;
    }

    public void setPuProfessional(String puProfessional) {
        this.puProfessional = puProfessional;
    }

    public String getPuPhone() {
        return puPhone;
    }

    public void setPuPhone(String puPhone) {
        this.puPhone = puPhone;
    }

    public Long getPuCreateTime() {
        return puCreateTime;
    }

    public void setPuCreateTime(Long puCreateTime) {
        this.puCreateTime = puCreateTime;
    }

    public Long getPuUpdateTime() {
        return puUpdateTime;
    }

    public void setPuUpdateTime(Long puUpdateTime) {
        this.puUpdateTime = puUpdateTime;
    }

	public String getPuFrom() {
		return puFrom;
	}

	public void setPuFrom(String puFrom) {
		this.puFrom = puFrom;
	}

	public Long getPuMedliveUserId() {
		return puMedliveUserId;
	}

	public void setPuMedliveUserId(Long puMedliveUserId) {
		this.puMedliveUserId = puMedliveUserId;
	}

	public String getPuMedliveUserThumb() {
		return puMedliveUserThumb;
	}

	public void setPuMedliveUserThumb(String puMedliveUserThumb) {
		this.puMedliveUserThumb = puMedliveUserThumb;
	}

    public Integer getPuTotalScore() {
        return puTotalScore == null ? 0:puTotalScore;
    }

    public void setPuTotalScore(Integer puTotalScore) {
        this.puTotalScore = puTotalScore;
    }

    @Override
    public String toString() {
        return "PradUser{" +
                "puId=" + puId +
                ", puName='" + puName + '\'' +
                ", puProvince='" + puProvince + '\'' +
                ", puCity='" + puCity + '\'' +
                ", puRegion='" + puRegion + '\'' +
                ", puHospital='" + puHospital + '\'' +
                ", puDept='" + puDept + '\'' +
                ", puProfessional='" + puProfessional + '\'' +
                ", puPhone='" + puPhone + '\'' +
                ", puCreateTime=" + puCreateTime +
                ", puUpdateTime=" + puUpdateTime +
                ", puProvinceId=" + puProvinceId +
                ", puCityId=" + puCityId +
                ", puRegionId=" + puRegionId +
				", puFrom=" + puFrom +
				", puMedliveUserId=" + puMedliveUserId +
				", puMedliveUserThumb=" + puMedliveUserThumb +
                ", puTotalScore=" + puTotalScore +
                '}';
    }
    @TableField(exist = false)
    private String puCreateTimeStr;

    public String getPuCreateTimeStr() {
        puCreateTimeStr = TimeUtil.longToString(this.getPuCreateTime(), TimeUtil.FORMAT_DATE);
        return puCreateTimeStr;
    }

    public void setPuCreateTimeStr(String puCreateTimeStr) {
        this.puCreateTimeStr = puCreateTimeStr;
    }
}
