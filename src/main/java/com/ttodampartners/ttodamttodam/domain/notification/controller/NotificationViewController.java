package com.ttodampartners.ttodamttodam.domain.notification.controller;

import com.ttodampartners.ttodamttodam.domain.notification.dto.NotificationViewDto;
import com.ttodampartners.ttodamttodam.domain.notification.service.NotificationViewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationViewController {
  private final NotificationViewService notificationViewService;

  @GetMapping("/notifications")
  public ResponseEntity<List<NotificationViewDto>> viewNotification () {
    List<NotificationViewDto> notificationViewDtos =
        notificationViewService.notificationView();

    return ResponseEntity.ok(notificationViewDtos);
  }
}
