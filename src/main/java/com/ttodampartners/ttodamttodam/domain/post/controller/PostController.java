package com.ttodampartners.ttodamttodam.domain.post.controller;

import com.ttodampartners.ttodamttodam.domain.post.dto.*;
import com.ttodampartners.ttodamttodam.domain.post.service.PostService;
import com.ttodampartners.ttodamttodam.global.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/post/write")
    public ResponseEntity<PostDto> createPost(
            @AuthenticationPrincipal UserDetailsDto userDetails,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestPart PostCreateDto postCreateDto
    ) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(PostDto.of(postService.createPost(userId, imageFiles, postCreateDto)));
    }


    @GetMapping("/post/list")
    public ResponseEntity<List<PostListDto>> getPostList(
            @AuthenticationPrincipal UserDetailsDto userDetails
    ){
        Long userId = userDetails.getId();
        List<PostListDto> postList = postService.getPostList(userId);
        return ResponseEntity.ok(postList);
    }

    @GetMapping("/post/category/{category}")
    public ResponseEntity<List<PostListDto>> getCategoryPostList(
            @AuthenticationPrincipal UserDetailsDto userDetails,
            @PathVariable String category
    ){
        Long userId = userDetails.getId();
        List<PostListDto> postList = postService.getCategoryPostList(userId,category);
        return ResponseEntity.ok(postList);
    }

    // 자신이 작성한 게시글 목록 조회
    @GetMapping("/users/post/list")
    public ResponseEntity<List<PostListDto>> getUsersPostList(
            @AuthenticationPrincipal UserDetailsDto userDetails
    ){
        Long userId = userDetails.getId();
        List<PostListDto> userPostList = postService.getUsersPostList(userId);
        return ResponseEntity.ok(userPostList);
    }

    //게시글 검색
    @GetMapping("/post/search")
    public ResponseEntity<List<PostListDto>> searchPostList(
            @RequestParam(required = false) String word
    ){
        List<PostListDto> searchPostList = postService.searchPostList(word);
        return ResponseEntity.ok(searchPostList);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDetailDto> getPost(
            @AuthenticationPrincipal UserDetailsDto userDetails,
            @PathVariable Long postId
    )
    {
        Long userId = userDetails.getId();
        PostDetailDto postDto = postService.getPost(userId, postId);
        return ResponseEntity.status(OK).body(postDto);
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @AuthenticationPrincipal UserDetailsDto userDetails,
            @PathVariable Long postId,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> newImageFiles,
            @RequestPart PostUpdateDto postUpdateDto
    )
    {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(PostDto.of(postService.updatePost(userId, postId, newImageFiles, postUpdateDto)));
    }

    @PutMapping("/post/{postId}/purchase/{purchaseStatus}")
    public ResponseEntity<PostDto> updatePurchaseStatus(
            @AuthenticationPrincipal UserDetailsDto userDetails,
            @PathVariable Long postId,
            @PathVariable String purchaseStatus
    ) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(PostDto.of(postService.updatePurchaseStatus(userId, postId, purchaseStatus)));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal UserDetailsDto userDetails,
            @PathVariable Long postId
    )
    {
        Long userId = userDetails.getId();
        postService.deletePost(userId, postId);
        return ResponseEntity.status(OK).build();
    }


}
