package com.kingyee.prad.model;

import java.util.List;

public class NewsCategoryModel {
    /**
     * 节点唯一索引值，用于对指定节点进行各类操作
     */
    private String id;
    /**
     * 节点标题
     */
    private String title;
    /**
     * 子节点。支持设定选项同父节点
     */
    private List<NewsCategoryModel> children;
    /**
     * 节点是否初始展开，默认 false
     */
    private boolean spread;
    /**
     * 节点是否初始为选中状态（如果开启复选框的话），默认 false
     */
    private boolean checked;
    /**
     * 节点是否为禁用状态。默认 false
     */
    private boolean disabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NewsCategoryModel> getChildren() {
        return children;
    }

    public void setChildren(List<NewsCategoryModel> children) {
        this.children = children;
    }

    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return "NewsCategoryModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", children=" + children +
                ", spread=" + spread +
                ", checked=" + checked +
                ", disabled=" + disabled +
                '}';
    }
}
