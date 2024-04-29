package com.ttodampartners.ttodamttodam.domain.chat.service;

import com.ttodampartners.ttodamttodam.domain.chat.dto.ChatExceptionResponse;
import com.ttodampartners.ttodamttodam.domain.chat.dto.event.GroupChatCreateEvent;
import com.ttodampartners.ttodamttodam.domain.chat.dto.request.ChatroomCreateRequest;
import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomExistedResponse;
import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomResponse;
import com.ttodampartners.ttodamttodam.domain.chat.dto.response.ChatroomProfileResponse;
import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomEntity;
import com.ttodampartners.ttodamttodam.domain.chat.entity.ChatroomMemberEntity;
import com.ttodampartners.ttodamttodam.domain.chat.exception.ChatroomException;
import com.ttodampartners.ttodamttodam.domain.chat.exception.ChatroomStringException;
import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomMemberRepository;
import com.ttodampartners.ttodamttodam.domain.chat.repository.ChatroomRepository;
import com.ttodampartners.ttodamttodam.domain.notification.service.NotificationService;
import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.post.repository.PostRepository;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ttodampartners.ttodamttodam.global.error.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatroomCreateService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatroomMemberRepository chatroomMemberRepository;
    private final NotificationService notificationService;

    // 개인 채팅방 생성
    @Transactional
    public ChatroomResponse createChatroom(ChatroomCreateRequest request, Long userId) {
        // 문의자
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        PostEntity post = postRepository.findById(request.getPostId()).orElseThrow(IllegalArgumentException::new);
        if (post.getStatus() != PostEntity.Status.IN_PROGRESS) { // '모집중' 게시글이 아닌 경우
            throw new ChatroomStringException(CHATROOM_CREATE_DENIED);
        }
        // 게시글 작성자
        UserEntity host = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        // 이미 user가 해당 post에 일대일 채팅방 생성한 적 있는지 체크
        List<ChatroomEntity> chatroomEntities = chatroomRepository.findByPostEntity(post); // 이 post에서 생성된 채팅방 리스트
        ErrorCode code = CHATROOM_ALREADY_EXIST;
        if (!CollectionUtils.isEmpty(chatroomEntities)) { // 이 post에서 생성된 채팅방이 하나라도 존재한다면
            for (ChatroomEntity chatroom: chatroomEntities) {
                // 추후 chat_active도 체크!!
                if (chatroomMemberRepository.existsByUserEntityAndChatroomEntity(user, chatroom)) {
                    throw new ChatroomException(
                            code,
                            ChatExceptionResponse.res(
                                    HttpStatus.BAD_REQUEST,
                                    code.getDescription(),
                                    ChatroomExistedResponse.builder().chatroomId(chatroom.getChatroomId()).build()
                            )
                    );
                }
            }
        }

        // CHATROOM 테이블에 컬럼 추가
        ChatroomEntity chatroom = chatroomRepository.save(
                ChatroomEntity.builder().postEntity(post).chatName(post.getTitle()).userCount(2).build()
        );

        // 채팅방 소속 멤버들
        List<UserEntity> members = new ArrayList<>(
                Arrays.asList(user, host)
        );
        // CHATROOM_MEMBER 테이블에 컬럼 추가
        List<ChatroomMemberEntity> chatroomMemberEntities = saveChatroomMembers(members, chatroom);
        if (chatroomMemberEntities.isEmpty()) {
            log.info("save 실패");
        }
        // 해당 채팅방에 소속된 유저(공구 주최자, 문의자)의 프로필 정보 리스트
        List<ChatroomProfileResponse> profileList = getChatroomProfiles(members);

        notificationService.sendNotificationForDirectChat(post, host);

        return ChatroomResponse.builder()
                .chatroomId(chatroom.getChatroomId())
                .hostId(post.getUser().getId())
                .userCount(2)
                .chatName(post.getTitle())
                .createAt(chatroom.getCreateAt())
                .profiles(profileList)
                .build();
    }

    // 단체 채팅방 생성
    @Async // 이벤트 메소드를 새로운 스레드에서 실행
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createGroupChat(GroupChatCreateEvent event) {
        log.info("TransactionPhase.AFTER_COMMIT ---> {}", event);

        PostEntity post = event.getPost();
        List<RequestEntity> requestEntities = event.getRequestEntities();

        // 단체 채팅방 존재 여부 확인
        ChatroomEntity alreadyExist = chatroomRepository.findByPostEntityAndUserCountGreaterThan2(post);
        if (alreadyExist != null) {
            throw new ChatroomException(GROUP_CHATROOM_ALREADY_EXIST, ChatExceptionResponse.res(HttpStatus.BAD_REQUEST, GROUP_CHATROOM_ALREADY_EXIST.getDescription(), ChatroomExistedResponse.builder().chatroomId(alreadyExist.getChatroomId()).build()));
        }

        // CHATROOM 테이블에 컬럼 추가
        ChatroomEntity chatroom = chatroomRepository.save(
                ChatroomEntity.builder().postEntity(post).chatName(post.getTitle()).userCount(requestEntities.size() + 1).build()
        );

        List<UserEntity> members = new ArrayList<>();
        members.add(post.getUser());
        requestEntities.stream().map(
                request -> members.add(request.getRequestUser())
        );

        if (chatroom.getUserCount() != members.size()) {
            throw new IllegalArgumentException("단체 채팅방 인원 수가 맞지 않습니다.");
        }

        // 개인 채팅방 생성 알림 to 게시글 작성자
        List<ChatroomMemberEntity> chatroomMemberEntities = saveChatroomMembers(members, chatroom);

        // 단체 채팅방 생성 알림
        notificationService.sendNotificationForGroupChat(post, members);
    }

    /*
        채팅방에 소속된 유저들 관련 메소드
    */
    @Transactional
    public List<ChatroomMemberEntity> saveChatroomMembers(List<UserEntity> members, ChatroomEntity chatroom) {
        return members.stream().map(
                member -> chatroomMemberRepository.save(
                        ChatroomMemberEntity.builder()
                                .chatroomEntity(chatroom)
                                .userEntity(member)
                                .chatActive(true)
                                .build()
                )
        ).toList();
    }

    @Transactional
    public List<ChatroomProfileResponse> getChatroomProfiles(List<UserEntity> members) {
        return members.stream().map(
                member -> ChatroomProfileResponse.builder()
                        .userId(member.getId())
                        .nickname(member.getNickname())
                        .profileImage(member.getProfileImgUrl())
                        .build()

        ).toList();
    }
}
