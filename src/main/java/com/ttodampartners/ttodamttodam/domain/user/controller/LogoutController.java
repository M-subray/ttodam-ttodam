package com.ttodampartners.ttodamttodam.domain.user.controller;

import com.ttodampartners.ttodamttodam.domain.user.service.LogoutService;
import com.ttodampartners.ttodamttodam.global.config.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LogoutController {

  private final TokenProvider tokenProvider;
  private final LogoutService logoutService;

  @PostMapping("/users/logout")
  public ResponseEntity<String> logout(HttpServletRequest request) {
    // request 에서 token 가져오기
    String token = tokenProvider.resolveTokenFromRequest(request);

    // service 에서 토큰을 Blacklist 로 저장
    logoutService.addTokenToBlacklist(token);

    //사용자 인증정보 삭제
    SecurityContextHolder.clearContext();

    log.info("로그아웃 완료");
    return ResponseEntity.ok("정상적으로 로그아웃 됐습니다.");
  }
}