//package com.ttodampartners.ttodamttodam.domain.chat.service;
//
//import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomEnterResponse;
//import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomProfileResponse;
//import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomMemberEntity;
//import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomMemberRepository;
//import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//
//@RequiredArgsConstructor
//@Service
//public class ChatroomEnterService {
//    private final ChatroomMemberRepository chatroomMemberRepository;
//    private final ChatroomRepository chatroomRepository;
//
//    @Transactional
//    public ChatroomEnterResponse getChatroomDetails(Long chatroomId) {
//        List<ChatroomMemberEntity> chatroomMemberEntities = chatroomMemberRepository.findAllByChatroomEntity(
//                // 추후 exception handler 처리 필요!!
//                chatroomRepository.findByChatroomId(chatroomId).orElseThrow(IllegalArgumentException::new)
//        );
//        // 채팅방 소속 유저가 한 명도 없을 경우 처리 필요!!
//
//        List<ChatroomProfileResponse> profileList = chatroomMemberEntities.stream().map(
//                ChatroomMemberEntity::getChatroomProfile
//        ).toList();
//        return ChatroomEnterResponse.builder().profiles(profileList).build();
//    }
//}
