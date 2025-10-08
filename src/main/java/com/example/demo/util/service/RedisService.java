package com.example.demo.util.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisService {

    void setValue(String key, String value);
    String getValue(String key);
    void deleteValue(String key);
    boolean hasKey(String key);
    void expire(String key, long timeout, TimeUnit unit);

    void hSet(String key, String hashKey, Object value);
    <T> T hGet(String key, String hashKey, Class<T> clazz);
    void hDelete(String key, String... hashKeys);
    boolean hHasKey(String key, String hashKey);
    Map<Object, Object> hGetAll(String key);
    Set<Object> hKeys(String key);
    long hSize(String key);
}
