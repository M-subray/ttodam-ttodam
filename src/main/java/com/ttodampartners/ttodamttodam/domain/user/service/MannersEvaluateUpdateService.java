package com.ttodampartners.ttodamttodam.domain.user.service;

import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.post.exception.PostException;
import com.ttodampartners.ttodamttodam.domain.post.repository.PostRepository;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity;
import com.ttodampartners.ttodamttodam.domain.request.repository.RequestRepository;
import com.ttodampartners.ttodamttodam.domain.user.dto.MannersEvaluateUpdateDto;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.domain.user.util.AuthenticationUtil;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MannersEvaluateUpdateService {
  private final UserRepository userRepository;
  private final RequestRepository requestRepository;
  private final PostRepository postRepository;

  @Transactional
  public void evaluateUpdate (Long postId, MannersEvaluateUpdateDto mannersEvaluateUpdateDto) {
    UserEntity curUser = getUser();

    isCurrentUserWriterOrMember(postId, curUser.getId());

    // 각각의 유저에 각각의 점수 넣기
    mannersEvaluateUpdateDto.getMannersForMembers().forEach((userId, manner) -> {
      if (manner == 0) {
        throw new UserException(ErrorCode.ADD_MANNERS_SCORE);
      }
      // 탈퇴한 유저는 없는지 확인
      if (userRepository.existsById(userId)) {
        UserEntity user = findUser(userId);
        user.setManners(user.getManners() + manner); // 기존의 매너점수 + 이번의 매너점수
        user.setEvaluationNumber(user.getEvaluationNumber() + 1); // 기존의 평가 횟수 + 1
        userRepository.save(user);
      }
    });
  }

  private void isCurrentUserWriterOrMember (Long postId, Long userId) {
    PostEntity post = postRepository.findById(postId).orElseThrow(() ->
        new PostException(ErrorCode.NOT_FOUND_POST));

    if (post.getUser().getId() == userId) { // 현재 유저가 작성자라면
      ifAlreadyEvaluateForWriter(postId);
    } else {  // 현재 유저가 참여자라면
      ifAlreadyEvaluateForMember(postId, userId);
    }
  }

  private void ifAlreadyEvaluateForWriter(Long postId) {
    PostEntity post = postRepository.findById(postId).orElseThrow(() ->
        new PostException(ErrorCode.NOT_FOUND_POST));
    if (post.isPostUserMannerEvaluated()) {
      throw new UserException(ErrorCode.ALREADY_EVALUATED_MANNERS);
    }
    post.setPostUserMannerEvaluated(true);
  }

  private void ifAlreadyEvaluateForMember(Long postId, Long userId) {
    List<RequestEntity> allByPostPostId = requestRepository.findAllByPost_PostId(postId);
    for (RequestEntity requestEntity : allByPostPostId) {
      if (requestEntity.getRequestUser().getId() == userId) {
        if (requestEntity.isRequestMemberMannerEvaluated()) {
          throw new UserException(ErrorCode.ALREADY_EVALUATED_MANNERS);
        } else {
          requestEntity.setRequestMemberMannerEvaluated(true);
        }
      }
    }
  }

  private UserEntity findUser(Long userId) {
    return userRepository.findById(userId).orElseThrow(() ->
        new UserException(ErrorCode.NOT_FOUND_USER));
  }

  private UserEntity getUser () {
    Authentication authentication = AuthenticationUtil.getAuthentication();
    return userRepository.findByEmail(authentication.getName()).orElseThrow(() ->
        new UserException(ErrorCode.NOT_FOUND_USER));
  }
}
