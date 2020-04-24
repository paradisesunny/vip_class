package com.kingyee.prad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 专家表
 * </p>
 *
 * @author Administrator
 * @since 2020-03-16
 */
public class PradExpert implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "pe_id", type = IdType.AUTO)
    private Long peId;

    /**
     * 专家名称
     */
    private String peName;

    /**
     * 头像缩略图
     */
    private String peHeadPath;

    /**
     * 医院
     */
    private String peHospital;

    /**
     * 科室
     */
    private String peDept;

    /**
     * 职称
     */
    private String peProfessional;

    /**
     * 创建时间
     */
    private Long peCreateTime;

    /**
     * 创建人id
     */
    private Long peCreateUserId;

    /**
     * 创建人姓名
     */
    private String peCreateUserName;

    /**
     * 更新时间
     */
    private Long peUpdateTime;

    /**
     * 更新人id
     */
    private Long peUpdateUserId;

    /**
     * 更新人姓名
     */
    private String peUpdateUserName;


    public Long getPeId() {
        return peId;
    }

    public void setPeId(Long peId) {
        this.peId = peId;
    }

    public String getPeName() {
        return peName;
    }

    public void setPeName(String peName) {
        this.peName = peName;
    }

    public String getPeHeadPath() {
        return peHeadPath;
    }

    public void setPeHeadPath(String peHeadPath) {
        this.peHeadPath = peHeadPath;
    }

    public String getPeHospital() {
        return peHospital;
    }

    public void setPeHospital(String peHospital) {
        this.peHospital = peHospital;
    }

    public String getPeDept() {
        return peDept;
    }

    public void setPeDept(String peDept) {
        this.peDept = peDept;
    }

    public String getPeProfessional() {
        return peProfessional;
    }

    public void setPeProfessional(String peProfessional) {
        this.peProfessional = peProfessional;
    }

    public Long getPeCreateTime() {
        return peCreateTime;
    }

    public void setPeCreateTime(Long peCreateTime) {
        this.peCreateTime = peCreateTime;
    }

    public Long getPeCreateUserId() {
        return peCreateUserId;
    }

    public void setPeCreateUserId(Long peCreateUserId) {
        this.peCreateUserId = peCreateUserId;
    }

    public String getPeCreateUserName() {
        return peCreateUserName;
    }

    public void setPeCreateUserName(String peCreateUserName) {
        this.peCreateUserName = peCreateUserName;
    }

    public Long getPeUpdateTime() {
        return peUpdateTime;
    }

    public void setPeUpdateTime(Long peUpdateTime) {
        this.peUpdateTime = peUpdateTime;
    }

    public Long getPeUpdateUserId() {
        return peUpdateUserId;
    }

    public void setPeUpdateUserId(Long peUpdateUserId) {
        this.peUpdateUserId = peUpdateUserId;
    }

    public String getPeUpdateUserName() {
        return peUpdateUserName;
    }

    public void setPeUpdateUserName(String peUpdateUserName) {
        this.peUpdateUserName = peUpdateUserName;
    }

    @Override
    public String toString() {
        return "PradExpert{" +
        "peId=" + peId +
        ", peName=" + peName +
        ", peHeadPath=" + peHeadPath +
        ", peHospital=" + peHospital +
        ", peDept=" + peDept +
        ", peProfessional=" + peProfessional +
        ", peCreateTime=" + peCreateTime +
        ", peCreateUserId=" + peCreateUserId +
        ", peCreateUserName=" + peCreateUserName +
        ", peUpdateTime=" + peUpdateTime +
        ", peUpdateUserId=" + peUpdateUserId +
        ", peUpdateUserName=" + peUpdateUserName +
        "}";
    }
}
