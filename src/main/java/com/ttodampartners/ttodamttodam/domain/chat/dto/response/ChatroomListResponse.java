package com.ttodampartners.ttodamttodam.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/*
    채팅방 리스트 조회 response body
    실제 response body에는 ChatroomListResponse가 담긴 리스트 형식 반환
*/

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatroomListResponse {
    private Long chatroomId;
    private String chatName;
    private String product;
    private Long hostId;
    private String hostNickname;
    private int userCount;
    private boolean ableChat = true;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
}
