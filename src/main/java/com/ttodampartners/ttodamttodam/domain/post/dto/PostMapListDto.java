package com.ttodampartners.ttodamttodam.domain.post.dto;

import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMapListDto {
    private String loginUserLocation;
    private Double loginUserLocationX;
    private Double loginUserLocationY;
    private Long postId;
    private Long authorId;
    private String authorNickname;
    private String place;
    private Double pLocationX;
    private Double pLocationY;
    private PostEntity.Category category;
    private PostEntity.Status status;
    private PostEntity.PurchaseStatus purchaseStatus;
    private String title;
    private String content;
    private List<ProductListDto> products;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostMapListDto of(UserEntity userEntity, PostEntity postEntity) {
        List<ProductListDto> products = postEntity.getProducts()
                .stream().map(ProductListDto::from).collect(Collectors.toList());
        return PostMapListDto.builder()
                .loginUserLocation(userEntity.getLocation())
                .loginUserLocationX(userEntity.getLocationX())
                .loginUserLocationY(userEntity.getLocationY())
                .postId(postEntity.getPostId())
                .authorId(postEntity.getUser().getId())
                .authorNickname(postEntity.getUser().getNickname())
                .place(postEntity.getPlace())
                .pLocationX(postEntity.getPLocationX())
                .pLocationY(postEntity.getPLocationY())
                .category(postEntity.getCategory())
                .status(postEntity.getStatus())
                .purchaseStatus(postEntity.getPurchaseStatus())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .createdAt(postEntity.getCreatedAt())
                .updatedAt(postEntity.getUpdatedAt())
                .products(products)
                .build();
    }

}