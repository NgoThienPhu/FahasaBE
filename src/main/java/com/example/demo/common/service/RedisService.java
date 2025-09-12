package com.example.demo.common.service;

import java.util.Map;
import java.util.Set;

public interface RedisService {

    void setValue(String key, String value);
    String getValue(String key);
    void deleteValue(String key);
    boolean hasKey(String key);
    void expire(String key, long timeoutInSeconds);

    void hSet(String key, String hashKey, Object value);
    <T> T hGet(String key, String hashKey, Class<T> clazz);
    void hDelete(String key, String... hashKeys);
    boolean hHasKey(String key, String hashKey);
    Map<Object, Object> hGetAll(String key);
    Set<Object> hKeys(String key);
    long hSize(String key);
}
