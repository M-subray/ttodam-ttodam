package com.ttodampartners.ttodamttodam.domain.request.controller;

import com.ttodampartners.ttodamttodam.domain.request.dto.ActivitiesDto;
import com.ttodampartners.ttodamttodam.domain.request.dto.RequestDto;
import com.ttodampartners.ttodamttodam.domain.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RequiredArgsConstructor
@RestController
public class RequestController {

    private final RequestService requestService;

    @PostMapping("/post/{postId}/request")
    public ResponseEntity<RequestDto> sendRequest(
            @PathVariable Long postId
        ) {
        return ResponseEntity.ok(RequestDto.of(requestService.sendRequest(postId)));
       }

    // 모집에 참여한 게시글 목록 조회 (로그인 유저가 참여요청을 보낸 모든 게시글)
    @GetMapping("/users/activities")
    public ResponseEntity<List<ActivitiesDto>> getUsersActivities(
    ){
        List<ActivitiesDto> activities = requestService.getUsersActivities();
        return ResponseEntity.ok(activities);
    }

    @DeleteMapping("/request/{requestId}")
    public ResponseEntity<Void> deleteRequest(
            @PathVariable Long requestId
    )
    {
        requestService.deleteRequest(requestId);
        return ResponseEntity.status(OK).build();
    }

    @GetMapping("/post/{postId}/request")
    public ResponseEntity<List<RequestDto>> getRequestList(
            @PathVariable Long postId
    ){
        List<RequestDto> requestList = requestService.getRequestList(postId);
        return ResponseEntity.ok(requestList);
    }

    @PutMapping("/request/{requestId}/{requestStatus}")
    public ResponseEntity<RequestDto> updateRequestStatus(
            @PathVariable Long requestId,
            @PathVariable String requestStatus
    ) {
        return ResponseEntity.ok(RequestDto.of(requestService.updateRequestStatus(requestId, requestStatus)));
    }


}
