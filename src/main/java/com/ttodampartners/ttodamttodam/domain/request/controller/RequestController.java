package com.ttodampartners.ttodamttodam.domain.request.controller;

import com.ttodampartners.ttodamttodam.domain.request.dto.ActivitiesDto;
import com.ttodampartners.ttodamttodam.domain.request.dto.RequestDto;
import com.ttodampartners.ttodamttodam.domain.request.dto.RequestSendDto;
import com.ttodampartners.ttodamttodam.domain.request.service.RequestService;
import com.ttodampartners.ttodamttodam.global.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RequiredArgsConstructor
@RestController
public class RequestController {

    private final RequestService requestService;

    @PostMapping("/post/{postId}/request")
    public ResponseEntity<RequestDto> sendRequest(
            @AuthenticationPrincipal UserDetailsDto userDetails,
            @PathVariable Long postId
        ) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(RequestDto.of(requestService.sendRequest(userId,postId)));
       }

    // 모집에 참여한 게시글 목록 조회 (로그인 유저가 참여요청을 보낸 모든 게시글)
    @GetMapping("/users/activities")
    public ResponseEntity<List<ActivitiesDto>> getUsersActivities(
            @AuthenticationPrincipal UserDetailsDto userDetails
    ){
        Long userId = userDetails.getId();
        List<ActivitiesDto> activities = requestService.getUsersActivities(userId);
        return ResponseEntity.ok(activities);
    }

    @DeleteMapping("/request/{requestId}")
    public ResponseEntity<Void> deleteRequest(
            @AuthenticationPrincipal UserDetailsDto userDetails,
            @PathVariable Long requestId
    )
    {
        Long userId = userDetails.getId();
        requestService.deleteRequest(userId, requestId);
        return ResponseEntity.status(OK).build();
    }

    @GetMapping("/post/{postId}/request")
    public ResponseEntity<List<RequestDto>> getRequestList(
            @AuthenticationPrincipal UserDetailsDto userDetails,
            @PathVariable Long postId
    ){
        Long userId = userDetails.getId();
        List<RequestDto> requestList = requestService.getRequestList(userId,postId);
        return ResponseEntity.ok(requestList);
    }

    @PutMapping("/request/{requestId}/{requestStatus}")
    public ResponseEntity<RequestDto> updateRequestStatus(
            @AuthenticationPrincipal UserDetailsDto userDetails,
            @PathVariable Long requestId,
            @PathVariable String requestStatus
    ) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(RequestDto.of(requestService.updateRequestStatus(userId, requestId, requestStatus)));
    }


}
