package com.pty.SerializerType;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * Json序列化、反序列化
 * 选用fastjson框架
 * @author : pety
 * @date : 2022/7/16 16:58
 */
@Slf4j
public class JsonSerializer implements Serializer {


    @Override
    public <T> byte[] serialize(T object) {
        try {
            byte[] bytes = JSON.toJSONBytes(object);
            return bytes;
        } catch (Exception e) {
            log.error("序列化错误"+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            T t = (T) JSON.parseObject(bytes, clazz);
            return t;
        } catch (Exception e) {
            log.error("反序列化错误"+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
