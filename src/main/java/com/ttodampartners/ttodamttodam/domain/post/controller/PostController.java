package com.ttodampartners.ttodamttodam.domain.post.controller;

import com.ttodampartners.ttodamttodam.domain.post.dto.*;
import com.ttodampartners.ttodamttodam.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    // 게시글 작성
    @PostMapping("/post/write")
    public ResponseEntity<PostDto> createPost(
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestPart PostCreateDto postCreateDto
    ) {
        return ResponseEntity.ok(PostDto.of(postService.createPost(imageFiles, postCreateDto)));
    }

    // 게시글 조회
    @GetMapping("/post/list")
    public ResponseEntity<List<PostListDto>> getPostList(
    ){
        List<PostListDto> postList = postService.getPostList();
        return ResponseEntity.ok(postList);
    }

    // 카테고리별 조회
    @GetMapping("/post/category/{category}")
    public ResponseEntity<List<PostListDto>> getCategoryPostList(
            @PathVariable String category
    ){
        List<PostListDto> postList = postService.getCategoryPostList(category);
        return ResponseEntity.ok(postList);
    }

    // 자신이 작성한 게시글 목록 조회
    @GetMapping("/users/post/list")
    public ResponseEntity<List<PostListDto>> getUsersPostList(
    ){
        List<PostListDto> userPostList = postService.getUsersPostList();
        return ResponseEntity.ok(userPostList);
    }

    //게시글 검색어 통한 조회
    @GetMapping("/post/search")
    public ResponseEntity<List<PostListDto>> searchPostList(
            @RequestParam(required = false) String word
    ){
        List<PostListDto> searchPostList = postService.searchPostList(word);
        return ResponseEntity.ok(searchPostList);
    }

    // 특정 게시글 상세조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDetailDto> getPost(
            @PathVariable Long postId
    )
    {
        PostDetailDto postDto = postService.getPost(postId);
        return ResponseEntity.status(OK).body(postDto);
    }

    // 게시글 물건 수정
    @PutMapping("/post/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long postId,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> newImageFiles,
            @RequestPart PostUpdateDto postUpdateDto
    )
    {
        return ResponseEntity.ok(PostDto.of(postService.updatePost(postId, newImageFiles, postUpdateDto)));
    }

    @PutMapping("/post/{postId}/purchase/{purchaseStatus}")
    public ResponseEntity<PostDto> updatePurchaseStatus(
            @PathVariable Long postId,
            @PathVariable String purchaseStatus
    ) {
        return ResponseEntity.ok(PostDto.of(postService.updatePurchaseStatus(postId, purchaseStatus)));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId
    )
    {
        postService.deletePost(postId);
        return ResponseEntity.status(OK).build();
    }


}
