package com.ttodampartners.ttodamttodam.domain.request.dto;

import com.ttodampartners.ttodamttodam.domain.post.dto.MembersDto;
import com.ttodampartners.ttodamttodam.domain.post.dto.ProductDto;
import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitiesDto {
    // 현재 로그인 유저 정보
    private Long userId;
    private String userNickname;
    //참여 게시글 정보
    private Long postId;
    private Long authorId;
    private String authorNickname;
    private List<MembersDto> members;
    private String title;
    private PostEntity.Status status;
    private PostEntity.PurchaseStatus purchaseStatus;
    private RequestEntity.RequestStatus requestStatus;
    private List<ProductDto> products;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ActivitiesDto of(RequestEntity request) {
        // 게시글의 다른 수락된 요청자들
//        List<MembersDto> members = List.of(MembersDto.of(request.getRequestUser()));

        List<ProductDto> products = request.getPost().getProducts()
                .stream().map(ProductDto::from).collect(Collectors.toList());

        return ActivitiesDto.builder()
                .userId(request.getRequestUser().getId())
                .userNickname(request.getRequestUser().getNickname())
                .postId(request.getPost().getPostId())
                .authorId(request.getPost().getUser().getId())
                .authorNickname(request.getPost().getUser().getNickname())
//                .members(members)
                .title(request.getPost().getTitle())
                .status(request.getPost().getStatus())
                .requestStatus(request.getRequestStatus())
                .purchaseStatus(request.getPost().getPurchaseStatus())
                .products(products)
                .createdAt(request.getPost().getCreatedAt())
                .updatedAt(request.getPost().getUpdatedAt())
                .build();
    }
}


