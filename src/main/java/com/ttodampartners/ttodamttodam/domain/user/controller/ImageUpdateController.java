package com.ttodampartners.ttodamttodam.domain.user.controller;

import com.ttodampartners.ttodamttodam.domain.user.service.ImageUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ImageUpdateController {
  private final ImageUpdateService imageUpdateService;

  @PostMapping("users/profiles/img-update")
  public ResponseEntity<String> imageUpdate (
      @RequestParam("file")MultipartFile file) {

    imageUpdateService.imageUpdate(file);
    log.info("프로필 사진 수정 완료");

    return ResponseEntity.ok("정상적으로 수정되었습니다.");
  }
}