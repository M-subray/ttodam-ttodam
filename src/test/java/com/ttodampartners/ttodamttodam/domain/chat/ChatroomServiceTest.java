package com.ttodampartners.ttodamttodam.domain.chat;

import com.ttodampartners.ttodamttodam.domain.chat.dto.request.ChatroomCreateRequest;
import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomResponse;
import com.ttodampartners.ttodamttodam.domain.chat.service.ChatroomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@DisplayName("개인 채팅방 생성 확인")
public class ChatroomServiceTest {

    @Autowired
    private ChatroomService chatroomService;

    @Test
    @DisplayName("개인 채팅방 생성 완료")
    void CREATE_CHATROOM_TEST() {
        ChatroomCreateRequest chatroomCreateRequest = ChatroomCreateRequest.builder().postId(2L).userId(3L).build();

        ChatroomResponse chatroomResponse = chatroomService.createChatroom(chatroomCreateRequest);

        assertNotNull(chatroomResponse);
        assertNotNull(chatroomResponse.getUserChatroomId());
        assertNotNull(chatroomResponse.getProfiles());
    }
}