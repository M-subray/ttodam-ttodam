package com.ttodampartners.ttodamttodam.global.util;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisUtil {

  private final RedisTemplate<String, String> redisTemplate;

  @Transactional
  public void setValues(String key, String value, Duration duration){
    redisTemplate.opsForValue().set(key, value, duration);
  }

  public String getValue(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public boolean keyExists(String token) {
    /*
    레디스의 key(지금은 token 이 key) 자체는 true 또는 false 로 가능하지만
    redisTemplate 가 없을 경우 nullPointException 이 발생할 수 있어
    Boolean.TRUE.equals 를 이용해 ture 일 경우 ture,
    false 또는 null 일 경우 false 를 반환하게 만듦
     */
    return Boolean.TRUE.equals(redisTemplate.hasKey(token));
  }
}