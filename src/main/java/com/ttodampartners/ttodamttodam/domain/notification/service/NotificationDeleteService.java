package com.ttodampartners.ttodamttodam.domain.notification.service;

import com.ttodampartners.ttodamttodam.domain.notification.entity.NotificationEntity;
import com.ttodampartners.ttodamttodam.domain.notification.exception.NotificationException;
import com.ttodampartners.ttodamttodam.domain.notification.repository.NotificationRepository;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.domain.user.util.AuthenticationUtil;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationDeleteService {
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  public void deleteNotification(Long notificationId) {
    UserEntity user = getUser();
    NotificationEntity notificationEntity = getNotification(notificationId);

    if (user.getId() != notificationEntity.getUser().getId()) {
      throw new UserException(ErrorCode.PERMISSION_DENIED);
    }
    notificationRepository.delete(getNotification(notificationId));
  }

  public NotificationEntity getNotification (Long notificationId) {
    return notificationRepository.findById(notificationId).orElseThrow(() ->
        new NotificationException(ErrorCode.NOT_FOUND_NOTIFICATION));
  }

  private UserEntity getUser () {
    Authentication authentication = AuthenticationUtil.getAuthentication();
    return userRepository.findByEmail(authentication.getName()).orElseThrow(() ->
        new UserException(ErrorCode.NOT_FOUND_USER));
  }
}
