package com.ttodampartners.ttodamttodam.domain.request.dto;

import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long requestId;
    private Long requestUserId;
    private String requestUserNickname;
    private double requestUserManners;
    private RequestEntity.RequestStatus requestStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RequestDto of(RequestEntity requestEntity) {
        return RequestDto.builder()
                .requestId(requestEntity.getRequestId())
                .requestUserNickname(requestEntity.getRequestUser().getNickname())
                .requestUserManners(requestEntity.getRequestUser().getManners())
                .requestUserId(requestEntity.getRequestUser().getId())
                .requestStatus(requestEntity.getRequestStatus())
                .createdAt(requestEntity.getCreatedAt())
                .updatedAt(requestEntity.getUpdatedAt())
                .build();
    }
}

