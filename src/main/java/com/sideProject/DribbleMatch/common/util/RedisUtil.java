package com.sideProject.DribbleMatch.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisUtil {

    @Value("${spring.jwt.refresh-token-expire}")
    private Long refreshExpire;

    private final RedisTemplate<String, String> redisTemplate;

    public void setRefreshToken(String refreshToken, String member_id)
    {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        deleteByValue(member_id);
        valueOperations.set(refreshToken, member_id, Duration.ofMillis(refreshExpire));
    }

    public void setAuthCode(String phone, String authCode)
    {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        deleteData(phone);
        valueOperations.set(phone, authCode, Duration.ofMillis(5 * 60 * 1000));
    }

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public boolean isExist(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void deleteByValue(String value) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        redisTemplate.keys("*").forEach(key -> {
            String storedValue = valueOps.get(key);
            if (value.equals(storedValue)) {
                redisTemplate.delete(key);
            }
        });
    }
}
