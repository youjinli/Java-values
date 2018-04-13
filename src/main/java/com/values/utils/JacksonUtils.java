/**
 * Copyright 2014 Welab, Inc. All rights reserved. WELAB PROPRIETARY/CONFIDENTIAL. Use is subject to
 * license terms.
 */
package com.values.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:Jason@wolaidai.com">Jason</a>
 */
public class JacksonUtils {
    //利用JackJson取json字符串的值
    //String  final_decision=JacksonUtils.getInstance().fetchValue(result, "personalSummaryReport:rulesResult:final_decision");
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtils.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JacksonUtils CONVERSION = new JacksonUtils();
    private static final String SEPARATOR = ":";

    private JacksonUtils() {
        init();
    }

    private static void init() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        MAPPER.setDateFormat(dateFormat);
        MAPPER.setSerializationInclusion(Include.NON_NULL);
    }


    public static JacksonUtils getInstance() {
        return CONVERSION;
    }

    public String pojo2Json(Object obj) throws IOException {
        StringWriter writer = new StringWriter();
        MAPPER.writeValue(writer, obj);

        return writer.toString();
    }

    public <T> T json2Pojo(InputStream jsonStream, Class<T> classType) throws IOException {
        T t = null;

        try (InputStream inputStream = jsonStream) {
            t = MAPPER.readValue(inputStream, classType);
        }

        return t;
    }

    public <T> T json2Pojo(String json, Class<T> classType) {
        try {
            return MAPPER.readValue(json, classType);
        } catch (Exception e) {
            LOGGER.error("Unexpected exception occurred!", e);
            return null;
        }

    }

    /**
     * 将JSON转化为List
     * 
     * @param json 字符串
     * @param elementClasses 元素类
     * @return List 转换后的对象
     */
    public static List<?> jsonConverList(String json, Class<?>... elementClasses) {
        try {
            return MAPPER.readValue(json, getCollectionType(List.class, elementClasses));
        } catch (Exception e) {
            LOGGER.error("Unexpected exception occurred!", e);
            return null;
        }
    }

    /**
     * 获取泛型的Collection Type
     * 
     * @param collectionClass 泛型的Collection
     * @param elementClasses 元素类
     * @return JavaType Java类型
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public <T> T map2Pojo(Map<String, Object> map, Class<T> classType) throws IOException {
        return MAPPER.convertValue(map, classType);
    }

    public <T> T jsonParse(String json) throws IOException {
        return MAPPER.readValue(json, new TypeReference<T>() {});
    }

    /**
     * path: Like xpath, to find the specific value via path. Use :(Colon) to separate different key
     * name or index. For example: JSON content: { "name": "One Guy", "details": [
     * {"education_first": "xx school"}, {"education_second": "yy school"}, {"education_third":
     * "zz school"}, ... ] }
     * 
     * To find the value of "education_second", the path="details:1:education_second".
     * 
     * @param json
     * @param path
     * @return
     */
    public String fetchValue(String json, String path) {
        JsonNode tempNode = null;
        try {
            JsonNode jsonNode = MAPPER.readTree(json);
            tempNode = jsonNode;
            String[] paths = path.split(SEPARATOR);

            for (String fieldName : paths) {
                if (tempNode.isArray()) {
                    tempNode = fetchValueFromArray(tempNode, fieldName);
                } else {
                    tempNode = fetchValueFromObject(tempNode, fieldName);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Unexpected exception occurred!", e);
            return null;
        }
        if (tempNode != null) {
            String value = tempNode.asText();

            if (value == null || value.isEmpty()) {
                value = tempNode.toString();
            }
            return value;
        }
        return null;
    }

    private JsonNode fetchValueFromObject(JsonNode jsonNode, String fieldName) {
        return jsonNode.get(fieldName);
    }

    private JsonNode fetchValueFromArray(JsonNode jsonNode, String index) {
        return jsonNode.get(Integer.parseInt(index));
    }

    public JsonNode convertStr2JsonNode(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            LOGGER.error("Unexpected exception occurred!", e);
            return null;
        }
    }
}
