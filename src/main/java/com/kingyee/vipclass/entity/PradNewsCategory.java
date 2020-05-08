package com.kingyee.vipclass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 资讯分类表
 * </p>
 *
 * @author zhl
 * @since 2020-03-09
 */
public class PradNewsCategory implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "nc_id", type = IdType.AUTO)
    private Long ncId;

    /**
     * 父id
     */
    private Long ncPid;

    /**
     * 名称
     */
    private String ncName;

    /**
     * 介绍文字
     */
    private String ncMemo;

    /**
     * 缩略图
     */
    private String ncPicPath;

    /**
     * 备用1
     */
    private String ncAdd1;

    /**
     * 备用2
     */
    private String ncAdd2;

    /**
     * 备用3
     */
    private String ncAdd3;


    public Long getNcId() {
        return ncId;
    }

    public void setNcId(Long ncId) {
        this.ncId = ncId;
    }

    public Long getNcPid() {
        return ncPid;
    }

    public void setNcPid(Long ncPid) {
        this.ncPid = ncPid;
    }

    public String getNcName() {
        return ncName;
    }

    public void setNcName(String ncName) {
        this.ncName = ncName;
    }

    public String getNcMemo() {
        return ncMemo;
    }

    public void setNcMemo(String ncMemo) {
        this.ncMemo = ncMemo;
    }

    public String getNcPicPath() {
        return ncPicPath;
    }

    public void setNcPicPath(String ncPicPath) {
        this.ncPicPath = ncPicPath;
    }

    public String getNcAdd1() {
        return ncAdd1;
    }

    public void setNcAdd1(String ncAdd1) {
        this.ncAdd1 = ncAdd1;
    }

    public String getNcAdd2() {
        return ncAdd2;
    }

    public void setNcAdd2(String ncAdd2) {
        this.ncAdd2 = ncAdd2;
    }

    public String getNcAdd3() {
        return ncAdd3;
    }

    public void setNcAdd3(String ncAdd3) {
        this.ncAdd3 = ncAdd3;
    }

    @Override
    public String toString() {
        return "PradNewsCategory{" +
        "ncId=" + ncId +
        ", ncPid=" + ncPid +
        ", ncName=" + ncName +
        ", ncMemo=" + ncMemo +
        ", ncPicPath=" + ncPicPath +
        ", ncAdd1=" + ncAdd1 +
        ", ncAdd2=" + ncAdd2 +
        ", ncAdd3=" + ncAdd3 +
        "}";
    }

    @TableField(exist = false)
    private Integer newsCount;
    @TableField(exist = false)
    private String newsUpdateTimeStr;

    public Integer getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(Integer newsCount) {
        this.newsCount = newsCount;
    }

    public String getNewsUpdateTimeStr() {
        return newsUpdateTimeStr;
    }

    public void setNewsUpdateTimeStr(String newsUpdateTimeStr) {
        this.newsUpdateTimeStr = newsUpdateTimeStr;
    }
}
