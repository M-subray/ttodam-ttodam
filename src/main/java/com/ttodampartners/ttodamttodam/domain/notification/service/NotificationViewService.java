package com.ttodampartners.ttodamttodam.domain.notification.service;

import com.ttodampartners.ttodamttodam.domain.notification.dto.NotificationViewDto;
import com.ttodampartners.ttodamttodam.domain.notification.entity.NotificationEntity;
import com.ttodampartners.ttodamttodam.domain.notification.entity.NotificationEntity.Type;
import com.ttodampartners.ttodamttodam.domain.notification.repository.NotificationRepository;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.domain.user.util.AuthenticationUtil;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationViewService {
  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;

  @Transactional(readOnly = true)
  public List<NotificationViewDto> notificationView () {
    List<NotificationEntity> allByUserId =
        notificationRepository.findAllByUserId(getUser().getId());

    return allByUserId.stream()
        .map(this::mapToResponseForNotificationDto)
        .collect(Collectors.toList());
  }

  private NotificationViewDto mapToResponseForNotificationDto (
      NotificationEntity notificationEntity) {
    NotificationViewDto.NotificationViewDtoBuilder builder = NotificationViewDto.builder()
        .notificationId(notificationEntity.getId())
        .userId(notificationEntity.getUser().getId())
        .type(notificationEntity.getType())
        .createAt(notificationEntity.getCreateAt());

    if (notificationEntity.getType().equals(Type.KEYWORD) || notificationEntity.getType().equals(Type.GROUPCHAT)) {
      builder.postId(notificationEntity.getPost().getPostId());
    }

    return builder.build();
  }


  private UserEntity getUser () {
    Authentication authentication = AuthenticationUtil.getAuthentication();
    return userRepository.findByEmail(authentication.getName()).orElseThrow(() ->
        new UserException(ErrorCode.NOT_FOUND_USER));
  }
}
