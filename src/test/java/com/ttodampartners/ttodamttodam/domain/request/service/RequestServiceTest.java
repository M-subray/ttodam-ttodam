package com.ttodampartners.ttodamttodam.domain.request.service;

import com.ttodampartners.ttodamttodam.domain.request.dto.ActivitiesDto;
import com.ttodampartners.ttodamttodam.domain.request.dto.RequestDto;
import com.ttodampartners.ttodamttodam.domain.request.dto.RequestSendDto;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity;
import com.ttodampartners.ttodamttodam.domain.request.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RequestServiceTest {

    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestRepository requestRepository;

    @Test
    void SEND_REQUEST_TEST(){

        // 테스트 참여요청 생성
        RequestEntity request = requestService.sendRequest(1L,59L);

        Optional<RequestEntity> optionalRequest = requestRepository.findById(request.getRequestId());
        assertTrue(optionalRequest.isPresent());
    }
    private static RequestSendDto testRequest(){
        return RequestSendDto.builder()
                .requestStatus(RequestEntity.RequestStatus.WAIT)
                .build();
    }

    @Test
    void GET_REQUEST_LIST_TEST(){
        Long userId = 3L;
        Long postId = 59L;

        List<RequestDto> requestList = requestService.getRequestList(userId, postId);

        assertEquals(2, requestList.size());
    }

    @Test
    void UPDATE_REQUEST_STATUS_TEST(){

        RequestEntity updateRequest = requestService.updateRequestStatus(3L, 7L,"수락");

        Optional<RequestEntity> optionalRequest = requestRepository.findById(updateRequest.getRequestId());
        assertTrue(optionalRequest.isPresent());

        RequestEntity result = optionalRequest.get();
        assertEquals(RequestEntity.RequestStatus.ACCEPT, result.getRequestStatus());
    }

    @Test
    void DELETE_REQUEST_TEST(){
        requestService.deleteRequest(3L,3L);

        assertFalse(requestRepository.existsById(3L));

    }
    @Test
    void GET_USER_ACTIVITIES_TEST(){
        Long userId = 1L;

        List<ActivitiesDto> usersActivities = requestService.getUsersActivities(userId);

        assertEquals(1, usersActivities.size());
    }

}