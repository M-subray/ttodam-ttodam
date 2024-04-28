package com.ttodampartners.ttodamttodam.domain.chat.service;

import com.ttodampartners.ttodamttodam.domain.chat.dto.ChatExceptionResponse;
import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomEntity;
import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomMemberEntity;
import com.ttodampartners.ttodamttodam.domain.chat.exception.ChatroomException;
import com.ttodampartners.ttodamttodam.domain.chat.exception.ChatroomStringException;
import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomMemberRepository;
import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomRepository;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ttodampartners.ttodamttodam.global.error.ErrorCode.CHATROOM_NOT_FOUND;
import static com.ttodampartners.ttodamttodam.global.error.ErrorCode.USER_NOT_IN_CHATROOM;

@RequiredArgsConstructor
@Service
public class ChatroomLeaveService {
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatroomMemberRepository chatroomMemberRepository;

    // 유저가 속한 chatroomId 채팅방 나가기
    @Transactional
    public void leaveChatroom(Long chatroomId, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        ChatroomEntity chatroom = chatroomRepository.findByChatroomId(chatroomId).orElseThrow(() -> new ChatroomStringException(CHATROOM_NOT_FOUND));

        ChatroomMemberEntity userChatroom = chatroomMemberRepository.findByUserEntityAndChatroomEntity(user, chatroom).orElseThrow(
                () -> new ChatroomException(USER_NOT_IN_CHATROOM, ChatExceptionResponse.res(HttpStatus.BAD_REQUEST, USER_NOT_IN_CHATROOM.getDescription()))
        );

        chatroomMemberRepository.delete(userChatroom);

        List<ChatroomMemberEntity> otherUsers = chatroomMemberRepository.findAllByChatroomEntity(chatroom);
        // otherUsers.isEmpty() == true일 경우 아무도 이용하지 않는 채팅방 -> 추후 삭제 로직 고민
        if (otherUsers.size() == 1) {
            otherUsers.get(0).updateChatActiveFalse();
        }
    }
}
