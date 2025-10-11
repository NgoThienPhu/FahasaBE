package com.example.demo.util.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.util.service.RedisService;

@Service
public class RedisService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public void setValue(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public String getValue(String key) {
		Object value = redisTemplate.opsForValue().get(key);
		return value instanceof String ? (String) value : null;
	}

	public void deleteValue(String key) {
		redisTemplate.delete(key);
	}

	public boolean hasKey(String key) {
		Boolean exists = redisTemplate.hasKey(key);
		return Boolean.TRUE.equals(exists);
	}

	public void expire(String key, long timeout, TimeUnit unit) {
		redisTemplate.expire(key, timeout, unit);
	}

	public void hSet(String key, String hashKey, Object value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T hGet(String key, String hashKey, Class<T> clazz) {
		Object value = redisTemplate.opsForHash().get(key, hashKey);
		if (value == null)
			return null;
		if (clazz.isInstance(value))
			return (T) value;
		throw new IllegalArgumentException("Stored value is not of type " + clazz.getName());
	}

	public void hDelete(String key, String... hashKeys) {
		redisTemplate.opsForHash().delete(key, (Object[]) hashKeys);
	}

	public boolean hHasKey(String key, String hashKey) {
		Boolean exists = redisTemplate.opsForHash().hasKey(key, hashKey);
		return Boolean.TRUE.equals(exists);
	}

	public Map<Object, Object> hGetAll(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	public Set<Object> hKeys(String key) {
		return redisTemplate.opsForHash().keys(key);
	}

	public long hSize(String key) {
		return redisTemplate.opsForHash().size(key);
	}
}
