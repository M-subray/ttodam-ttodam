//package com.ttodampartners.ttodamttodam.domain.chat.service;
//
//import com.ttodampartners.ttodamttodam.domain.chat.dto.request.ChatroomCreateRequest;
//import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomListResponse;
//import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomResponse;
//import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomProfileResponse;
//import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomEntity;
//import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomMemberEntity;
//import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomMemberRepository;
//import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomRepository;
//import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
//import com.ttodampartners.ttodamttodam.domain.post.repository.PostRepository;
//import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
//import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class ChatroomService {
//    private final PostRepository postRepository;
//    private final UserRepository userRepository;
//    private final ChatroomRepository chatroomRepository;
//    private final ChatroomMemberRepository chatroomMemberRepository;
//
//    // 일대일 개인 채팅방 생성 -> response body 반환
//    @Transactional
//    public ChatroomResponse createChatroom(ChatroomCreateRequest request) {
//        // 문의자
//        UserEntity user = userRepository.findById(request.getUserId()).orElseThrow(IllegalArgumentException::new);
//
//        PostEntity post = postRepository.findById(request.getPostId()).orElseThrow(IllegalArgumentException::new);
//        // 게시글 작성자
//        UserEntity host = userRepository.findById(post.getUser().getId()).orElseThrow(IllegalArgumentException::new);
//
//        // CHATROOM 테이블에 컬럼 추가
//        ChatroomEntity chatroom = chatroomRepository.save(
//                ChatroomEntity.builder().postEntity(post).chatName(post.getTitle()).userCount(2).build()
//        );
//
//        // CHATROOM_MEMBER 테이블에 컬럼 추가
//        List<UserEntity> members = new ArrayList<>(
//                Arrays.asList(user, host)
//        );
//        List<ChatroomMemberEntity> memberEntityList = members.stream().map(
//                        member -> chatroomMemberRepository.save(
//                                ChatroomMemberEntity.builder()
//                                        .chatroomEntity(chatroom)
//                                        .userEntity(member)
//                                        .build()))
//                .toList();
//
//        // 해당 채팅방에 소속된 유저(공구 주최자, 문의자)의 프로필 정보 리스트
//        // ChatroomMemebrEntity로 리팩토링 필요!!
//        List<ChatroomProfileResponse> profileList = new ArrayList<>();
//        profileList.add(
//                ChatroomProfileResponse.builder()
//                .userId(host.getId()).nickname(host.getNickname()).profileImage(host.getProfileImgUrl())
//                .build()
//        );
//        profileList.add(
//                ChatroomProfileResponse.builder()
//                .userId(user.getId()).nickname(user.getNickname()).profileImage(user.getProfileImgUrl())
//                .build()
//        );
//
//        return ChatroomResponse.builder()
//                .userChatroomId(chatroom.getChatroomId())
//                .hostId(post.getUser().getId()).userCount(2)
//                .chatName(post.getTitle())
//                .createAt(chatroom.getCreateAt())
//                .profiles(profileList)
//                .build();
//    }
//
//    // 유저가 속한 채팅방 목록 조회 -> List 반환
//    @Transactional
//    public List<ChatroomListResponse> getChatrooms(Long userId) {
//        UserEntity user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
//        // 유저가 속한 CHATROOM_MEMBER 엔티티 리스트
//        List<ChatroomMemberEntity> userChatrooms = chatroomMemberRepository.findAllByUserEntity(user);
//
//        if (userChatrooms.size() == 0) {
//            // 추후 error response로 변경!!
//            List<ChatroomListResponse> noChatrooms = new ArrayList<>(
//                    Arrays.asList(ChatroomListResponse.builder().build())
//            );
//            return noChatrooms;
//        }
//
//        List<ChatroomListResponse> chatroomListResponses = userChatrooms.stream().map(
//                ChatroomMemberEntity::getChatroomInfos
//        ).toList();
//
//        return chatroomListResponses;
//    }
//}
