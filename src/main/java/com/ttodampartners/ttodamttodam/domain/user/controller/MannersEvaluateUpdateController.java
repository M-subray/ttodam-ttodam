package com.ttodampartners.ttodamttodam.domain.user.controller;

import com.ttodampartners.ttodamttodam.domain.user.dto.MannersEvaluateUpdateDto;
import com.ttodampartners.ttodamttodam.domain.user.service.MannersEvaluateUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MannersEvaluateUpdateController {
  private final MannersEvaluateUpdateService mannersEvaluateUpdateService;

  @PostMapping("/users/activities/manners/{postId}")
  public ResponseEntity<?> evaluateUpdate (
      @PathVariable Long postId,
      @RequestBody @Valid MannersEvaluateUpdateDto mannersEvaluateUpdateDto) {

    mannersEvaluateUpdateService.evaluateUpdate(postId, mannersEvaluateUpdateDto);
    return ResponseEntity.ok().body("평가해 주셔서 감사합니다.");
  }
}
