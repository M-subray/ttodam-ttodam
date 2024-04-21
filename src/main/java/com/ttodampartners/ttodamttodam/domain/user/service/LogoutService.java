package com.ttodampartners.ttodamttodam.domain.user.service;

import com.ttodampartners.ttodamttodam.global.config.security.TokenProvider;
import com.ttodampartners.ttodamttodam.global.util.RedisUtil;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutService {

  private final RedisUtil redisUtil;
  private final TokenProvider tokenProvider;

  @Transactional
  public void addTokenToBlacklist(String token) {
    long remainingTime =
        tokenProvider.calculateRemainingTime(tokenProvider.parseClaims(token).getExpiration());
    redisUtil.setValues(token, "BLACKLISTED", Duration.ofMillis(remainingTime));
  }
}