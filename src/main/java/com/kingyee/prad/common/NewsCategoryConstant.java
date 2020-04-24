
package com.kingyee.prad.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 分类
 */
public class NewsCategoryConstant {

    /**
     * 前沿进展
     */
    public static final Long THEORY_FRONT = 1L;
    /**
     * MDT甄选
     */
    public static final Long DOCTOR_RESOURCES = 2L;
    /**
     * 医学工具
     */
    public static final Long DOCTOR_TOOLS = 3L;
    /**
     * 文章专题
     */
    public static final Long ARTICLE_TOPI= 4L;
    /**
     * 规范化课程
     */
    public static final Long NEWS_CLASS = 5L;

    public static Map<Long, String> map = new HashMap<>();

    static {
        map.put(THEORY_FRONT, "前沿进展");
        map.put(DOCTOR_RESOURCES, "MDT甄选");
        map.put(DOCTOR_TOOLS, "医学工具");
        map.put(ARTICLE_TOPI, "文章专题");
        map.put(NEWS_CLASS, "规范化课程");
    }

    /**
     * 分类
     *
     * @param id
     * @return
     */
    public static String getType(Long id) {
        if (map.containsKey(id)) {
            return map.get(id);
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(map.get(NEWS_CLASS));
    }
}