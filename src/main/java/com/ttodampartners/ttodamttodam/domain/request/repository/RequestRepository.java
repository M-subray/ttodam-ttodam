package com.ttodampartners.ttodamttodam.domain.request.repository;

import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity.RequestStatus;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {

    List<RequestEntity> findAllByPost_PostId(Long postId);
    List<RequestEntity> findAllByPost_PostIdAndRequestStatus(Long postId, RequestStatus requestStatus);
    List<RequestEntity> findAllByRequestUser_Id(Long RequestUserId);
    boolean existsByRequestUserAndPost(UserEntity requestUser, PostEntity post);
}
