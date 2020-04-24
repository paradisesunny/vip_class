// ======================================
// Project Name:ceog
// Package Name:com.kingyee.common.jackson
// File Name:JacksonMapper.java
// Create Date:2018年02月07日  15:29
// ======================================
package com.kingyee.common.jackson;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingyee.common.modal.IJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Jackson用于转换的核心类ObjectMapper无需每次都new一个object
 *
 * @author fyq
 * @version 2018年02月07日  15:29
 */
public class JacksonMapper implements IJson {

    private static Logger logger = LoggerFactory.getLogger(JacksonMapper.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private JacksonMapper() {

    }

    public static ObjectMapper getInstance() {
        return mapper;
    }

    /**
     * 将String类型的json数据转换为JsonNode
     */
    public static JsonNode parse(String jsonStr) {
        try {
            return getInstance().readTree(jsonStr);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return getInstance().createObjectNode();
        }
    }

    /**
     * 成功的json<br>
     * <code>{success:true}</code>
     *
     * @return JsonElement
     */
    public static JsonNode newSuccessInstance() {
        Map<String, Object> map = new HashMap<>();
        map.put(SUCCESS, true);
        return toJsonNode(map);
    }

    /**
     * 包含错误信息的json<br>
     * <code>{success:false,msg:$msg}</code>
     *
     * @param msg 错误信息
     * @return JsonElement
     */
    public static JsonNode newErrorInstance(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put(SUCCESS, false);
        map.put(MSG, msg == null ? "" : msg);
        return toJsonNode(map);
    }

    /**
     * 包含数据的json
     *
     * @param data JsonElement或普通的对象
     * @return JsonElement
     */
    public static JsonNode newDataInstance(Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put(SUCCESS, true);
        map.put(CODE, 0);
        map.put(DATA, data == null ? "" : data);
        return toJsonNode(map);
    }

    /**
     * 生成纯json
     *
     * @param data Object
     * @return JsonElement
     */
    public static JsonNode newJson(Object data) {
        return toJsonNode(data == null ? "" : data);
    }

    /**
     * 包含count属性的json,一般前台的grid需要
     *
     * @param data  Object
     * @param count long
     * @return JsonElement
     */
    public static JsonNode newCountInstance(Object data, long count) {
        Map<String, Object> map = new HashMap<>();
        map.put(SUCCESS, true);
        map.put(CODE, 0);
        map.put(DATA, data == null ? "" : data);
        map.put(COUNT, count);
        return toJsonNode(map);
    }

    /**
     * 包含count属性的json,一般前台的grid需要
     *
     * @param pageInfo IPageInfo<?>
     * @return JsonElement
     */
    public static JsonNode newCountInstance(IPage<?> pageInfo) {
        return newCountInstance(pageInfo.getRecords(), pageInfo.getTotal());
    }

    private static JsonNode toJsonNode(Object data) {
        return getInstance().valueToTree(data);
    }

}