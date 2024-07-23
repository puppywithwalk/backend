package com.puppypaws.project.service;

import com.puppypaws.project.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisRepository redisRepository;

    public void storeTokenWithExpiry(String key, String token, long ttl) {
        redisRepository.storeTokenWithExpiry(key, token, ttl);
    }

    public String findTokenByRefreshToken(String key) {
        return redisRepository.findTokenByRefreshToken(key);
    }

    public void updateReissueAccessToken(String newKey, String token, String oldKey) {
        redisRepository.updateReissueAccessToken(newKey, token, oldKey);
    }
}
