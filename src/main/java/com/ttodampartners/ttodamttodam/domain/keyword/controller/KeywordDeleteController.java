package com.ttodampartners.ttodamttodam.domain.keyword.controller;

import com.ttodampartners.ttodamttodam.domain.keyword.service.KeywordDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KeywordDeleteController {
  private final KeywordDeleteService keywordDeleteService;

  @DeleteMapping("users/keywords")
  public ResponseEntity<String> deleteKeyword(@RequestParam("keywordId") Long keywordId) {
    keywordDeleteService.deleteKeyword(keywordId);

    return ResponseEntity.ok("키워드가 삭제되었습니다.");
  }
}
