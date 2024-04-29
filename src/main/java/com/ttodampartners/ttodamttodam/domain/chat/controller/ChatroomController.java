package com.ttodampartners.ttodamttodam.domain.chat.controller;

import com.ttodampartners.ttodamttodam.domain.chat.dto.request.ChatroomCreateRequest;
import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomListResponse;
import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomResponse;
import com.ttodampartners.ttodamttodam.domain.chat.service.ChatroomLeaveService;
import com.ttodampartners.ttodamttodam.domain.chat.service.ChatroomService;
import com.ttodampartners.ttodamttodam.domain.chat.service.ChatroomCreateService;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.global.dto.UserDetailsDto;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatrooms")
@RestController
public class ChatroomController {
    private final ChatroomService chatroomService;
    private final ChatroomCreateService chatroomCreateService;
    private final ChatroomLeaveService chatroomLeaveService;
    private final UserRepository userRepository;
    private final ChatController chatController;

    @PostMapping // POST /chatrooms (채팅방 생성)
    public ResponseEntity<ChatroomResponse> createChatroom(@RequestBody ChatroomCreateRequest request, @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        Long userId = userDetailsDto.getId();
        ChatroomResponse chatroomResponse = chatroomCreateService.createChatroom(request, userId);

        log.info("채팅방 생성 성공 -> chatroomId: {}, userCount: {}", chatroomResponse.getChatroomId(), chatroomResponse.getUserCount());
        return ResponseEntity.ok(chatroomResponse);
    }

    @GetMapping // GET /chatrooms (채팅방 목록 조회)
    public ResponseEntity<List<ChatroomListResponse>> getChatrooms(@AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        List<ChatroomListResponse> chatroomListResponses = chatroomService.getChatrooms(userDetailsDto.getId());
        return ResponseEntity.ok(chatroomListResponses);
    }

    @DeleteMapping("/{chatroomId}/exit") // DELETE /chatrooms/{chatroomId}/exit (채팅방 나가기)
    public ResponseEntity<String> leaveChatroom(@PathVariable Long chatroomId, @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        Long leftUserId = userDetailsDto.getId();
        UserEntity user = userRepository.findById(leftUserId).orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));
        chatroomLeaveService.leaveChatroom(chatroomId, leftUserId);

        chatController.sendExitMessage(chatroomId, user.getNickname());

        return ResponseEntity.ok("정상적으로 채팅방 나가기가 수행되었습니다.");
    }
}
