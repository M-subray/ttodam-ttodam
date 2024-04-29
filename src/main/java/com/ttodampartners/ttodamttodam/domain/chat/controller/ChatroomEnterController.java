package com.ttodampartners.ttodamttodam.domain.chat.controller;

import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatMessageResponse;
import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomEnterResponse;
import com.ttodampartners.ttodamttodam.domain.chat.service.ChatroomEnterService;
import com.ttodampartners.ttodamttodam.global.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatroom")
@RestController
public class ChatroomEnterController {
    private final ChatroomEnterService chatroomEnterService;

    // {chatroomId} 채팅방 입장 시 채팅방에 속한 유저들 프로필 리스트 반환
    @GetMapping // GET /chatroom?chatroomId={chatroomId}
    public ResponseEntity<ChatroomEnterResponse> getChatroomDetails(@RequestParam Long chatroomId, @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        ChatroomEnterResponse chatroomEnterResponse = chatroomEnterService.getChatroomDetails(chatroomId);
        return ResponseEntity.ok(chatroomEnterResponse);
    }

    // {chatroomId} 채팅방 입장 후 해당 채팅방에서 송수신된 메시지 반환
    @GetMapping("/{chatroomId}/message-history")
    public ResponseEntity<List<ChatMessageResponse>> getChatMessageHistory(@PathVariable Long chatroomId, @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        List<ChatMessageResponse> chatMessageResponses = chatroomEnterService.getChatMessageHistory(chatroomId);
        return ResponseEntity.ok(chatMessageResponses);
    }
}
