package com.ttodampartners.ttodamttodam.domain.user.service;

import com.ttodampartners.ttodamttodam.domain.post.exception.PostException;
import com.ttodampartners.ttodamttodam.domain.post.repository.PostRepository;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity.RequestStatus;
import com.ttodampartners.ttodamttodam.domain.request.repository.RequestRepository;
import com.ttodampartners.ttodamttodam.domain.user.dto.MannersEvaluateCheckDto;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.domain.user.util.AuthenticationUtil;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MannersEvaluateCheckService {
  private final RequestRepository requestRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  @Transactional(readOnly = true)
  public MannersEvaluateCheckDto evaluateCheck(Long postId) {
    // 글 작성자 가져오기
    UserEntity postUser = getPostUser(postId);
    // 로그인 유저 가져오기
    UserEntity curUser = getUser();
    /*
    postId로 요청자중 상태가 accept인 유저들 가져온 다음
    (if 내가 글의 '작성자'라면)
    -> 나를 제외한 유저 가져오기
    (if 내가 글의 '참여자'라면)
    -> 나를 제외한 유저 가져오기 + 작성자 id 및 닉네임 추가
     */
    Map<Long, String> userIdAndNickname = getRequestList(postId, curUser, postUser);
    List<Long> userIdList = new ArrayList<>(userIdAndNickname.keySet());
    List<String> userNicknameList = new ArrayList<>(userIdAndNickname.values());

    return MannersEvaluateCheckDto.builder()
        .userIdList(userIdList)
        .userNicknameList(userNicknameList)
        .build();
  }

  private Map<Long, String> getRequestList (Long postId, UserEntity curUser, UserEntity postUser) {
    List<RequestEntity> acceptedRequests =
        requestRepository.findAllByPost_PostIdAndRequestStatus(postId, RequestStatus.ACCEPT);

    return getUsersFromAcceptedRequests(acceptedRequests, curUser, postUser);
  }

  private Map<Long, String> getUsersFromAcceptedRequests(List<RequestEntity> acceptedRequests,
      UserEntity curUser, UserEntity postUser) {
    Map<Long, String> userIdAndNickname = new HashMap<>();
    for (RequestEntity request : acceptedRequests) {
      // 본인 제외하고 추가 (본인을 본인이 매너점수 평가할 수 없기에)
      if (request.getRequestUser().getId() != curUser.getId()) {
        Long userId = request.getRequestUser().getId();
        String userNickname = request.getRequestUser().getNickname();
        userIdAndNickname.put(userId, userNickname);
      }
    }
    // 현재 로그인 유저가 글 작성자가 아니라면 글 작성자를 추가
    // (글 작성자는 요청 목록에 없기 떄문)
    if (curUser.getId() != postUser.getId()) {
      userIdAndNickname.put(postUser.getId(), postUser.getNickname());
    }
    return userIdAndNickname;
  }

  private UserEntity getUser () {
    Authentication authentication = AuthenticationUtil.getAuthentication();
    return userRepository.findByEmail(authentication.getName()).orElseThrow(() ->
        new UserException(ErrorCode.NOT_FOUND_USER));
  }

  private UserEntity getPostUser(Long postId) {
    return postRepository.findById(postId).orElseThrow(() ->
        new PostException(ErrorCode.NOT_FOUND_POST)).getUser();
  }
}