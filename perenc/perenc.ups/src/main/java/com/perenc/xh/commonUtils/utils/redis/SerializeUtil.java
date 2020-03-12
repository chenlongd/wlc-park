package com.perenc.xh.commonUtils.utils.redis;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description TDDO
 * @Author xiaobai
 * @Date 2019/2/28 16:06
 **/
public class SerializeUtil {

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> byte[] serializer(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(clazz);
        return ProtobufIOUtil.toByteArray(obj, schema, LinkedBuffer.allocate(256));
    }

    public static <T> T deSerializer(byte[] bytes, Class<T> clazz) {
        T message;
        try {
            message = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Schema<T> schema = getSchema(clazz);
        ProtobufIOUtil.mergeFrom(bytes, message, schema);
        return message;
    }

    @SuppressWarnings("unchecked")
    public static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(clazz);
            if (schema != null) {
                cachedSchema.put(clazz, schema);
            }
        }
        return schema;
    }
}
