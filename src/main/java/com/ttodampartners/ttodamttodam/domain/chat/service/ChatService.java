package com.ttodampartners.ttodamttodam.domain.chat.service;

import com.ttodampartners.ttodamttodam.domain.chat.dto.request.ChatMessageRequest;
import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatMessageEntity;
import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomEntity;
import com.ttodampartners.ttodamttodam.domain.chat.exception.ChatroomStringException;
import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatMessageRepository;
import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomRepository;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/*
    송신한 채팅 메시지를 저장하고 일정 기간이 지나면 삭제
*/


@RequiredArgsConstructor
@Service
public class ChatService {
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional // 채팅 메시지 저장
    public void saveChatMessage(Long chatroomId, ChatMessageRequest request, Long senderId) {
        UserEntity user = userRepository.findById(senderId).orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));
        ChatroomEntity chatroom = chatroomRepository.findByChatroomId(chatroomId).orElseThrow(() -> new ChatroomStringException(ErrorCode.CHATROOM_NOT_FOUND));

        // CHAT_MESSAGE 테이블에 insert
        Long chatMessageId = chatMessageRepository.save(
                ChatMessageEntity.builder().userEntity(user).chatroomEntity(chatroom).content(request.getContent()).nickname(request.getNickname()).build()
        ).getChatMessageId();
        // CHATROOM 테이블의 lastMessageId 업데이트
        chatroom.updateLastMessage(chatMessageId);
    }


    @Transactional // 30일 지난 채팅 메시지 자동 삭제
    @Async
    @Scheduled(cron = "0 0 12 * * *") // 매일 오후 12시 자동 실행
    public void autoChatDelete() {
        chatMessageRepository.deleteByCreateAtLessThanEqual(LocalDateTime.now().minusMonths(1));
    }
}
