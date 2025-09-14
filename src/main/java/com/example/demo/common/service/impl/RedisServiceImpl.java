package com.example.demo.common.service.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.common.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void setValue(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public String getValue(String key) {
		Object value = redisTemplate.opsForValue().get(key);
		return value instanceof String ? (String) value : null;
	}

	@Override
	public void deleteValue(String key) {
		redisTemplate.delete(key);
	}

	@Override
	public boolean hasKey(String key) {
		Boolean exists = redisTemplate.hasKey(key);
		return Boolean.TRUE.equals(exists);
	}

	@Override
	public void expire(String key, long timeout, TimeUnit unit) {
		redisTemplate.expire(key, timeout, unit);
	}

	@Override
	public void hSet(String key, String hashKey, Object value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T hGet(String key, String hashKey, Class<T> clazz) {
		Object value = redisTemplate.opsForHash().get(key, hashKey);
		if (value == null)
			return null;
		if (clazz.isInstance(value))
			return (T) value;
		throw new IllegalArgumentException("Stored value is not of type " + clazz.getName());
	}

	@Override
	public void hDelete(String key, String... hashKeys) {
		redisTemplate.opsForHash().delete(key, (Object[]) hashKeys);
	}

	@Override
	public boolean hHasKey(String key, String hashKey) {
		Boolean exists = redisTemplate.opsForHash().hasKey(key, hashKey);
		return Boolean.TRUE.equals(exists);
	}

	@Override
	public Map<Object, Object> hGetAll(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	@Override
	public Set<Object> hKeys(String key) {
		return redisTemplate.opsForHash().keys(key);
	}

	@Override
	public long hSize(String key) {
		return redisTemplate.opsForHash().size(key);
	}
}
