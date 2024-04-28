package com.ttodampartners.ttodamttodam.domain.post.dto;

import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.request.dto.RequestDto;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDto {
    private PostDto post;
    private String loginUserRequestStatus;
    private Long bookmarkId;
    private List<RequestDto> requestList;

    public static PostDetailDto of(PostEntity postEntity, List<RequestEntity> requestEntities, String loginUserRequestStatus, Long bookmarkId) {
        return PostDetailDto.builder()
                .post(PostDto.of(postEntity))
                .requestList(requestEntities.stream()
                        .map(RequestDto::of)
                        .collect(Collectors.toList()))
                .loginUserRequestStatus(loginUserRequestStatus)
                .bookmarkId(bookmarkId)
                .build();
    }
}
