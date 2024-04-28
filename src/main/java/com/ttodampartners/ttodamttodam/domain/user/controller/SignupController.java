package com.ttodampartners.ttodamttodam.domain.user.controller;

import com.ttodampartners.ttodamttodam.domain.user.dto.SignupRequestDto;
import com.ttodampartners.ttodamttodam.domain.user.service.SignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SignupController {
  private final SignupService signupService;

  @PostMapping("/users/signup")
  public ResponseEntity<String> signup(
      @RequestBody @Valid SignupRequestDto signupRequestDto) {
    signupService.signup(signupRequestDto);

    log.info("회원가입 성공, 이메일 : {}", signupRequestDto.getEmail());
    return ResponseEntity.ok("회원가입 완료\n이메일 : " + signupRequestDto.getEmail());
  }
}
