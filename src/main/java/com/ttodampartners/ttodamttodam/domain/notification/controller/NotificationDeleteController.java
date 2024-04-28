package com.ttodampartners.ttodamttodam.domain.notification.controller;

import com.ttodampartners.ttodamttodam.domain.notification.service.NotificationDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationDeleteController {
  private final NotificationDeleteService notificationDeleteService;

  @DeleteMapping("/notifications/{notificationId}")
  public ResponseEntity<?> deleteNotification(@RequestParam Long notificationId) {
    notificationDeleteService.deleteNotification(notificationId);

    return ResponseEntity.ok("정상적으로 삭제되었습니다.");
  }
}
