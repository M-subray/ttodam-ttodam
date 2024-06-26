package com.ttodampartners.ttodamttodam.domain.chat.service;

import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatMessageResponse;
import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomEnterResponse;
import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomProfileResponse;
import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatMessageEntity;
import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomEntity;
import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomMemberEntity;
import com.ttodampartners.ttodamttodam.domain.chat.exception.ChatroomStringException;
import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatMessageRepository;
import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomMemberRepository;
import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomRepository;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatroomEnterService {
    private final ChatroomMemberRepository chatroomMemberRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 입장했을 때, 해당 채팅방 멤버 정보 반환
    @Transactional
    public ChatroomEnterResponse getChatroomDetails(Long chatroomId) {
        ChatroomEntity chatroom = chatroomRepository.findByChatroomId(chatroomId).orElseThrow(() -> new ChatroomStringException(ErrorCode.CHATROOM_NOT_FOUND));
        List<ChatroomMemberEntity> chatroomMemberEntities = chatroomMemberRepository.findAllByChatroomEntity(chatroom);

        if (CollectionUtils.isEmpty(chatroomMemberEntities)) {
            throw new ChatroomStringException("채팅방에 소속된 유저가 없습니다.");
        }

        List<ChatroomProfileResponse> profileList = chatroomMemberEntities.stream().map(
                ChatroomMemberEntity::getChatroomProfile
        ).toList();
        return ChatroomEnterResponse.builder().profiles(profileList).build();
    }

    // 채팅방 메시지 리스트 반환
    @Transactional
    public List<ChatMessageResponse> getChatMessageHistory(Long chatroomId) {
        ChatroomEntity chatroom = chatroomRepository.findByChatroomId(chatroomId).orElseThrow(IllegalArgumentException::new);
        // 채팅 내역이 존재하지 않을 경우
        if (chatroom.getLastMessageId() == null) {
            throw new ChatroomStringException(ErrorCode.CHATROOM_MESSAGE_NOT_FOUND);
        }

        List<ChatMessageEntity> chatMessageEntities = chatMessageRepository.findAllByChatroomEntity(chatroom);

        return chatMessageEntities.stream().map(ChatMessageEntity::getChatMessageResponse).toList();
    }
}
