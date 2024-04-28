package com.ttodampartners.ttodamttodam.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    public enum MessageType{
        ENTER, TALK, LEFT
    }

    private MessageType type;
    private String nickname;
    @NotBlank(message = "채팅 내용이 없습니다.")
    private String content;
}
