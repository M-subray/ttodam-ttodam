package com.ttodampartners.ttodamttodam.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {
    public enum MessageType{
        ENTER, TALK, LEAVE
    }

    private MessageType type;
//    @NotBlank(message = "송신자 id가 존재하지 않습니다.")
//    private Long senderId;
    private String nickname;
    @NotBlank(message = "채팅 내용이 없습니다.")
    private String content;
}
