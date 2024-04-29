package com.ttodampartners.ttodamttodam.domain.bookmark.dto;

import com.ttodampartners.ttodamttodam.domain.bookmark.entity.BookmarkEntity;
import com.ttodampartners.ttodamttodam.domain.post.dto.ProductListDto;
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
public class BookmarkDto {
    private Long bookmarkId;
    private Long userId;
    private Long postId;
    private Long authorId;
    private String authorNickname;
    private PostEntity.Category category;
    private PostEntity.Status status;
    private PostEntity.PurchaseStatus purchaseStatus;
    private String postTitle;
    private String postStatus;
    private List<ProductListDto> products;

    public static BookmarkDto of(BookmarkEntity bookmarkEntity) {
        String postStatus = bookmarkEntity.getPost().getStatus().toString();
        List<ProductListDto> products = bookmarkEntity.getPost().getProducts()
                .stream()
                .map(ProductListDto::from)
                .collect(Collectors.toList());

        return BookmarkDto.builder()
                .bookmarkId(bookmarkEntity.getBookmarkId())
                .postId(bookmarkEntity.getPost().getPostId())
                .userId(bookmarkEntity.getUser().getId())
                .authorId(bookmarkEntity.getPost().getUser().getId())
                .authorNickname(bookmarkEntity.getPost().getUser().getNickname())
                .category(bookmarkEntity.getPost().getCategory())
                .status(bookmarkEntity.getPost().getStatus())
                .purchaseStatus(bookmarkEntity.getPost().getPurchaseStatus())
                .postTitle(bookmarkEntity.getPost().getTitle())
                .postStatus(postStatus)
                .products(products)
                .build();
    }
}
