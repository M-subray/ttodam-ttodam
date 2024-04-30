package com.ttodampartners.ttodamttodam.domain.user.controller;

import com.ttodampartners.ttodamttodam.domain.user.dto.MannersEvaluateCheckDto;
import com.ttodampartners.ttodamttodam.domain.user.service.MannersEvaluateCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MannersEvaluateCheckController {
  private final MannersEvaluateCheckService mannersEvaluateCheckService;

  @GetMapping("/users/activities/manners/{postId}")
  public ResponseEntity<MannersEvaluateCheckDto> evaluateCheck (@PathVariable Long postId) {

    MannersEvaluateCheckDto mannersEvaluateCheckDto =
        mannersEvaluateCheckService.evaluateCheck(postId);

    return ResponseEntity.ok(mannersEvaluateCheckDto);
  }
}