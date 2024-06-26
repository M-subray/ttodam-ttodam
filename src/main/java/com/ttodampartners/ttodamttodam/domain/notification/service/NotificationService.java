package com.ttodampartners.ttodamttodam.domain.notification.service;

import com.ttodampartners.ttodamttodam.domain.keyword.entity.KeywordEntity;
import com.ttodampartners.ttodamttodam.domain.keyword.repository.KeywordRepository;
import com.ttodampartners.ttodamttodam.domain.notification.entity.NotificationEntity;
import com.ttodampartners.ttodamttodam.domain.notification.entity.NotificationEntity.Type;
import com.ttodampartners.ttodamttodam.domain.notification.exception.NotificationException;
import com.ttodampartners.ttodamttodam.domain.notification.repository.NotificationRepository;
import com.ttodampartners.ttodamttodam.domain.notification.util.TokenSseEmitter;
import com.ttodampartners.ttodamttodam.domain.post.dto.PostCreateDto;
import com.ttodampartners.ttodamttodam.domain.post.dto.ProductAddDto;
import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.global.config.security.TokenProvider;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private static final long EXPIRE_TIME = 1000 * 60 * 60;
  private static Map<Long, TokenSseEmitter> sseEmitters = new ConcurrentHashMap<>();
  private final KeywordRepository keywordRepository;
  private final NotificationRepository notificationRepository;

  public TokenSseEmitter subscribe(Long userId) {
    // 현재 클라이언트를 위한 sseEmitter 객체 생성 (만료시간 한시간 설정)
    TokenSseEmitter sseEmitter = new TokenSseEmitter(EXPIRE_TIME);
    Date now = new Date();
    // sseEmitter.setExpiredDate(now.getTime() + EXPIRE_TIME);
    sseEmitter.setExpiredDate(now.getTime() + EXPIRE_TIME);

    // 연결
    try {
      sseEmitter.send(SseEmitter.event().name("connect").data("SSE 연결")); // 클라이언트에게 이벤트 전송
    } catch (IOException e) {
      throw new NotificationException(ErrorCode.SSE_SEND_FAILED);
    }

    // 저장
    sseEmitters.put(userId, sseEmitter);
    log.info("유저 id : {}의 SSE 객체 생성", userId);

    // 연결 종료 처리
    sseEmitter.onCompletion(() -> sseEmitters.remove(userId));
    sseEmitter.onTimeout(() -> sseEmitters.remove(userId));
    sseEmitter.onError((e) -> {
      log.error("SSE 연결 오류 발생 (userId: {})", userId, e);
      sseEmitters.remove(userId);
    });

    return sseEmitter;
  }

  public void sendNotificationForKeyword (PostCreateDto postCreateDto, PostEntity post) {
    // PostCreateDto 에서 가져온 키워드를 담을 String 리스트
    List<String> keywordList = new ArrayList<>();

    // 키워드 추출
    List<ProductAddDto> products = postCreateDto.getProducts();
    for (ProductAddDto productAddDto : products) {
      keywordList.add(productAddDto.getProductName());
    }

    // 추출한 키워드 갖고 있는 키워드 엔티티 찾아 알림 발송
    for (String keyword : keywordList) {
      List<KeywordEntity> byKeywordNameIn =
          keywordRepository.findAllByKeywordName(keyword);

      for (KeywordEntity keywordEntity : byKeywordNameIn) {
        SseEmitter sseEmitter = getSseEmitter(keywordEntity.getUser().getId());
        String message = String.format("등록하신 키워드 '%s' 가 포함된 글이 작성되었습니다.",
            keywordEntity.getKeywordName());
        saveNotificationForKeyword(keywordEntity, message, post);
        if (sseEmitter != null) {
          try {
            sseEmitter.send(SseEmitter.event().
                name("notification").
                data(Map.of("notificationMessage", message)));
          } catch (IOException e) {
            throw new NotificationException(ErrorCode.SSE_SEND_FAILED);
          }
        }
      }
    }
  }

  private void saveNotificationForKeyword (KeywordEntity keywordEntity,
      String message, PostEntity post) {
    notificationRepository.save(NotificationEntity.builder()
        .user(keywordEntity.getUser())
        .message(message)
        .type(Type.KEYWORD)
        .post(post)
        .build());
  }

  public void sendNotificationForGroupChat (PostEntity post, List<UserEntity> members) {
    for (UserEntity member: members) {
      SseEmitter sseEmitter = getSseEmitter(member.getId());
      String message = String.format("희망하셨던 %s 게시글에 대한 공동구매가 성사되어 단체 채팅방이 생성되었습니다. 채팅방 목록에서 확인해보세요!", post.getTitle());
      saveNotificationForGroupChat(member, message, post);

      if (sseEmitter != null) {
        try {
          sseEmitter.send(SseEmitter.event().
                  name("notification").
                  data(Map.of("notificationMessage", message)));
        } catch (IOException e) {
          throw new NotificationException(ErrorCode.SSE_SEND_FAILED);
        }
      }
    }
  }

  private void saveNotificationForGroupChat(UserEntity member, String message, PostEntity post) {
    notificationRepository.save(NotificationEntity.builder()
        .user(member)
        .message(message)
        .type(Type.GROUPCHAT)
        .post(post)
        .build());
  }

  public void sendNotificationForDirectChat(PostEntity post, UserEntity host) {
    SseEmitter sseEmitter = getSseEmitter(host.getId());
    String message = String.format("%s 게시글에 대한 문의 메시지가 도착했습니다. 채팅방 목록에서 확인해보세요!", post.getTitle());
    saveNotificationForDirectChat(host, message, post);
    if (sseEmitter != null) {
      try {
        sseEmitter.send(SseEmitter.event().
                name("notification").
                data(Map.of("notificationMessage", message)));
      } catch (IOException e) {
        throw new NotificationException(ErrorCode.SSE_SEND_FAILED);
      }
    }
  }

  private void saveNotificationForDirectChat(UserEntity host, String message, PostEntity post) {
    notificationRepository.save(NotificationEntity.builder()
            .user(host)
            .message(message)
            .type(Type.DIRECTCHAT)
            .post(post)
            .build());
  }

  @Scheduled(fixedRate = 900000)
  public void sendHeartBeat() {
    long currentTime = System.currentTimeMillis();

    Iterator<Entry<Long, TokenSseEmitter>> iterator = sseEmitters.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<Long, TokenSseEmitter> entry = iterator.next();
      Long userId = entry.getKey();
      TokenSseEmitter sseEmitter = entry.getValue();

      // 토큰 만료 시간 확인
      long expirationTime = sseEmitter.getExpiredDate();
      if (expirationTime <= currentTime) {
        // 만료된 SSE 연결을 종료하고 맵에서 제거
        sseEmitter.complete();
        iterator.remove();
        log.info("SSE 연결이 만료되어 종료됩니다. UserId: {}", userId);
        continue; // 만료된 SSE 연결인 경우 이후 로직은 실행하지 않고 다음 반복으로 넘어갑니다.
      }

      try {
        int heartBeat = (int) (Math.random() * 100) + 60;

        Map<String, Object> data = new HashMap<>();
        data.put("heartRate", heartBeat);
        sseEmitter.send(SseEmitter.event().name("heartbeat").data(data));
        log.info("{}, 연결 데이터 전송 / 임의 데이터 : {}", userId, heartBeat);
      } catch (IOException e) {
        log.error("SSE 데이터 전송 오류", e);
        sseEmitters.remove(userId);
      }
    }
  }

  private SseEmitter getSseEmitter (Long userId) {
    return sseEmitters.get(userId);
  }

  public void remove(Long userId) {
    sseEmitters.remove(userId);
  }
}