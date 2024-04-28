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
public class PostListDto {
    private Long postId;
    private Long authorId;
    private String authorNickname;
    private String title;
    private String content;
    private List<ProductListDto> products;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostListDto of(PostEntity postEntity) {
        List<ProductListDto> products = postEntity.getProducts()
                .stream().map(ProductListDto::from).collect(Collectors.toList());
        return PostListDto.builder()
                .postId(postEntity.getPostId())
                .authorId(postEntity.getUser().getId())
                .authorNickname(postEntity.getUser().getNickname())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .createdAt(postEntity.getCreatedAt())
                .updatedAt(postEntity.getUpdatedAt())
                .products(products)
                .build();
    }

}