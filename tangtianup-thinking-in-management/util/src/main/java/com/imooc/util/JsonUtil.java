package com.imooc.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;

/**
 * Created by Administrator on 2017/6/11 0011.
 */
public class JsonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);
    private final static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
// 属性可见度只打印public
//     mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public static void setDateFormat(DateFormat dateFormat) {
        mapper.setDateFormat(dateFormat);
    }

    /**
     * 把Java对象转为JSON字符串
     *
     * @param obj the object need to transfer into json string.
     * @return json string.
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            LOG.error("to json exception.", e);
            throw new JSONException("把对象转换为JSON时出错了", e);
        }
    }
}

final class JSONException extends RuntimeException {
    public JSONException(final String message) {
        super(message);
    }

    public JSONException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
