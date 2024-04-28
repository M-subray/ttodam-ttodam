package com.ttodampartners.ttodamttodam.domain.user.controller;

import com.ttodampartners.ttodamttodam.domain.user.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WithdrawController {
  private final WithdrawService withdrawService;

  @DeleteMapping("/users/withdraw")
  public ResponseEntity<String> withdraw () {
    withdrawService.withdraw();

    log.info("회원탈퇴 완료");
    return ResponseEntity.ok("탈퇴 완료");
  }
}
