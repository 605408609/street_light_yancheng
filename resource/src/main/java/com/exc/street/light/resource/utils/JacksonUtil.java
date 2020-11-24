package com.exc.street.light.resource.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json数据工具类
 *
 * @author huangmin
 * @date 2019/06/22
 */
public class JacksonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    public static Integer getReturnCode(String str) {
        logger.debug("json数据是:" + str);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("json解析错误" + e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("json解析错误" + e);
        }
        Integer returnInt = rootNode.path("returnCode").asInt();
        return returnInt;
    }

    public static Integer getCode(String str) {
        logger.debug("json数据是:" + str);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("json解析错误" + e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("json解析错误" + e);
        }
        Integer returnInt = rootNode.path("code").asInt();
        return returnInt;
    }

    /**
     * Object对象转换为String
     *
     * @param data
     *            Object对象
     * @return Object对象对应的字符串
     */
    public static <T> String toJsonString(T data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            if (logger.isErrorEnabled()) {
                logger.error("json parse error:", ex);
            }
            return "json parse error:" + ex.getMessage();
        }
    }

    /**
     * 字符串转换为对象
     *
     * @param json
     *            字符串
     * @param clazz
     *            类类型
     * @return clazz类型的对象
     */
    public static <R> R json2Object(String json, Class<R> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            		  
            return mapper.readValue(json, clazz);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("json parse error:", ex);
            }
        }
        return null;
    }
    
    /**
     * 字符串转换为对象，传入多个对象
     *
     * @param json
     *            字符串
     * @param clazz
     *            类类型
     * @return clazz类型的对象
     */
    public static <R> R json2Object(String json, Class<?> collectionClass,Class<?> elementClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaType constructParametricType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
            return mapper.readValue(json, constructParametricType);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("json parse error:", ex);
            }
        }
        return null;
    }
 

    /**
     * 字节转换为对象
     *
     * @param jsonBuffer
     *            字节
     * @param clazz
     *            类类型
     * @return clazz类型的对象
     */
    public static <R> R json2Object(byte[] jsonBuffer, Class<R> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonBuffer, clazz);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("json parse error:", ex);
            }
        }
        return null;

    }
}

