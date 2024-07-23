package com.puppypaws.project.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepository {


    private final RedisTemplate<String, String> redisTemplate;

    public void storeTokenWithExpiry(String key, String value, long ttl) {
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.MILLISECONDS);
    }

    public String findTokenByRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void updateReissueAccessToken(String key, String value, String oldKey) {
        Long expire = redisTemplate.getExpire(oldKey, TimeUnit.MILLISECONDS);
        if (expire != null && expire > 0) {
            redisTemplate.opsForValue().set(key, value, expire, TimeUnit.MILLISECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }
}