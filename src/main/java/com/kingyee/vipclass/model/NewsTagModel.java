package com.kingyee.vipclass.model;


import com.kingyee.vipclass.entity.PradNewsCategory;

import java.util.List;

/**
 * @author nmy
 * @date 2020/3/10
 * @desc 后台管理-内容标签列表
 */
public class NewsTagModel {
	private PradNewsCategory parentCategory;
    private List<PradNewsCategory> childrenCategoryList;

    public PradNewsCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(PradNewsCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<PradNewsCategory> getChildrenCategoryList() {
        return childrenCategoryList;
    }

    public void setChildrenCategoryList(List<PradNewsCategory> childrenCategoryList) {
        this.childrenCategoryList = childrenCategoryList;
    }
}
