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
import java.util.List;
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
    -> 나를 제외한 유저 가져오기 + 작성자 entity 추가하기
     */
    List<UserEntity> userEntitiesForEvaluate = getRequestList(postId, curUser, postUser);
    // 유저 Entity 에서 유저 닉네임 만들기
    List<String> userNicknames = userEntitiesForEvaluate.stream()
        .map(UserEntity::getNickname)
        .toList();

    return MannersEvaluateCheckDto.builder().userEntities(userEntitiesForEvaluate)
        .userNicknames(userNicknames).build();
  }

  private List<UserEntity> getRequestList (Long postId, UserEntity curUser, UserEntity postUser) {
    List<RequestEntity> allByPostPostId = requestRepository.findAllByPost_PostId(postId);
    List<RequestEntity> acceptedRequests = new ArrayList<>();

    // postId로 가져온 RequestEntity중 accept인 Entity만 모으기
    for (RequestEntity request : allByPostPostId) {
      if (request.getRequestStatus().equals(RequestStatus.ACCEPT)) {
        acceptedRequests.add(request);
      }
    }
    return getUsersFromAcceptedRequests(acceptedRequests, curUser, postUser);
  }

  private List<UserEntity> getUsersFromAcceptedRequests(List<RequestEntity> acceptedRequests,
      UserEntity curUser, UserEntity postUser) {
    List<UserEntity> userEntities = new ArrayList<>();
    for (RequestEntity request : acceptedRequests) {
      // 본인 제외하고 추가 (본인을 본인이 매너점수 평가할 수 없기에)
      if (request.getRequestUser().getId() != curUser.getId()) {
        userEntities.add(request.getRequestUser());
      }
    }
    // 현재 로그인 유저가 글 작성자가 아니라면 글 작성자를 추가
    // (글 작성자는 요청 목록에 없기 떄문)
    if (curUser.getId() != postUser.getId()) {
      userEntities.add(postUser);
    }
    return userEntities;
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