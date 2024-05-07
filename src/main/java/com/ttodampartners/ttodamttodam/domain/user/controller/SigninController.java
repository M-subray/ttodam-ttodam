package com.ttodampartners.ttodamttodam.domain.user.controller;

import com.ttodampartners.ttodamttodam.domain.notification.service.NotificationService;
import com.ttodampartners.ttodamttodam.domain.notification.util.TokenSseEmitter;
import com.ttodampartners.ttodamttodam.domain.user.dto.SigninRequestDto;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.domain.user.service.SigninService;
import com.ttodampartners.ttodamttodam.global.dto.UserDetailsDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SigninController {
  private final SigninService signinService;
  private final NotificationService notificationService;

  @PostMapping("/users/signin")
  public ResponseEntity<?> signin (@RequestBody @Valid SigninRequestDto signinRequestDto) {
    String token = signinService.signin(signinRequestDto);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + token);
    log.info("로그인 성공, email : {}", signinRequestDto.getEmail());
    log.info("token : {}", token);
    return ResponseEntity.ok().headers(headers).body("로그인 성공");
  }

  @GetMapping(value="/users/sse-sub", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public TokenSseEmitter sseSubscribe (@AuthenticationPrincipal UserDetailsDto userDetailsDto) {
    return notificationService.subscribe(userDetailsDto.getId());
  }
}