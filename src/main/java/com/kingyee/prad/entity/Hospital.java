package com.kingyee.prad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 省地县医院四级树
 * </p>
 *
 * @author Administrator
 * @since 2020-03-16
 */
public class Hospital implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 编码
     */
    private Long code;

    /**
     * 名称
     */
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Hospital{" +
        "id=" + id +
        ", parentId=" + parentId +
        ", code=" + code +
        ", name=" + name +
        "}";
    }
}
