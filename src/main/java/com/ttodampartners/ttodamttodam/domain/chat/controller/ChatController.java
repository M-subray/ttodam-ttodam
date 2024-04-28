package com.ttodampartners.ttodamttodam.domain.chat.controller;

import com.ttodampartners.ttodamttodam.domain.chat.dto.request.ChatMessageRequest;
import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomEntity;
import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomMemberEntity;
import com.ttodampartners.ttodamttodam.domain.chat.exception.ChatroomStringException;
import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomMemberRepository;
import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomRepository;
import com.ttodampartners.ttodamttodam.domain.chat.service.ChatService;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.global.config.security.TokenProvider;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatroomRepository chatroomRepository;
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    /*
        "/chattings/{chatroomId}/messages"로 전송되는 메시지 처리 핸들러
    */

    @MessageMapping("/{chatroomId}/messages")
    public void chat(@DestinationVariable Long chatroomId, @Valid ChatMessageRequest request, SimpMessageHeaderAccessor accessor) {
        // 채팅방 존재 여부 확인
        chatroomRepository.findByChatroomId(chatroomId).orElseThrow(() -> new ChatroomStringException(ErrorCode.CHATROOM_NOT_FOUND));

        String authToken = String.valueOf(accessor.getNativeHeader("Authorization")).substring(7);
        String userEmail = tokenProvider.parseClaims(authToken).getSubject();
        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));
        // 채팅 메시지 DB 저장
        chatService.saveChatMessage(chatroomId, request, user.getId());
        // 받은 메시지를 "/chatroom/{userChatroomId}" 엔드포인트로 전송
        simpMessagingTemplate.convertAndSend("/chatroom/" + chatroomId, request.getContent());

        log.info("Message [{}] send by member: {}(id: {}) to chatting room id: {}", request.getContent(), request.getNickname(), user.getId(), chatroomId);
    }

    @MessageMapping("/user-left")
    public void notificateLeft(@RequestParam("chatroomId") Long chatroomId, @RequestParam("leftUserId") String leftUserNickname) {
        String message = String.format("%s님이 채팅방에서 나갔습니다.", leftUserNickname);
        simpMessagingTemplate.convertAndSend("/chatroom/" + chatroomId, message);

    }
}
