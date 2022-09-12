package me.alxndr.conccurecystock.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

/**
 * @author : Alexander Choi
 * @date : 2022/09/12
 */
@Repository
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> redisTemplate;


    public Boolean lock(Long key) {
        return redisTemplate.opsForValue()
                .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3000));
    }

    public Boolean unlock(Long key) {
        return redisTemplate.delete(generateKey(key));
    }

    private String generateKey(Long key) {
        return key.toString();
    }

}
