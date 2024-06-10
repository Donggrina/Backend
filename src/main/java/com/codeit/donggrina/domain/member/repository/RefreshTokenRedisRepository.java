package com.codeit.donggrina.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public void save(String refreshToken) {
        redisTemplate.opsForValue().set(refreshToken, "true");
    }

    public boolean hasRefreshToken(String refreshToken) {
        return redisTemplate.hasKey(refreshToken);
    }
}