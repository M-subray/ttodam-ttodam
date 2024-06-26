package com.ttodampartners.ttodamttodam.domain.request.dto;

import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity.RequestStatus;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestSendDto {

    private RequestEntity.RequestStatus requestStatus;

    public static RequestEntity of(UserEntity requestUser, PostEntity post) {

        return RequestEntity.builder()
                .requestUser(requestUser)
                .post(post)
                .requestStatus(RequestStatus.WAIT)
                .build();
    }
}

