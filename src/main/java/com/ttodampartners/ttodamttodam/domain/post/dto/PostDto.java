package com.ttodampartners.ttodamttodam.domain.post.dto;

import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long postId;
    private Long userId;
    private PostEntity.Category category;
    private PostEntity.Status status;
    private PostEntity.PurchaseStatus purchaseStatus;
    private String title;
    private LocalDateTime deadline;
    private Integer participants;
    private String place;
    private Double pLocationX;
    private Double pLocationY;
    private String content;
    private List<String> imgUrls;
    private List<ProductsDto> products;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static PostDto of(PostEntity postEntity) {
        List<ProductsDto> products = postEntity.getProducts()
                .stream().map(ProductsDto::from).collect(Collectors.toList());
        return PostDto.builder()
                .postId(postEntity.getPostId())
                .userId(postEntity.getUser().getId())
                .title(postEntity.getTitle())
                .participants(postEntity.getParticipants())
                .place(postEntity.getPlace())
                .pLocationX(postEntity.getPLocationX())
                .pLocationY(postEntity.getPLocationY())
                .deadline(postEntity.getDeadline())
                .category(postEntity.getCategory())
                .content(postEntity.getContent())
                .imgUrls(postEntity.getImgUrls())
                .createdAt(postEntity.getCreatedAt())
                .updatedAt(postEntity.getUpdatedAt())
                .products(products)
                .status(postEntity.getStatus())
                .purchaseStatus(postEntity.getPurchaseStatus())
                .build();
    }

}