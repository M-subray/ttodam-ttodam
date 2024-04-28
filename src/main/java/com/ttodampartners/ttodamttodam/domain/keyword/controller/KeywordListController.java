package com.ttodampartners.ttodamttodam.domain.keyword.controller;

import com.ttodampartners.ttodamttodam.domain.keyword.dto.response.KeywordCheckResponseDto;
import com.ttodampartners.ttodamttodam.domain.keyword.service.KeywordListService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KeywordListController {
  private final KeywordListService keywordListService;

  @GetMapping("users/keywords")
  public ResponseEntity<List<KeywordCheckResponseDto>> getKeywordList () {
    List<KeywordCheckResponseDto> keywordList =
        keywordListService.getKeywordList();

    return ResponseEntity.ok(keywordList);
  }
}
