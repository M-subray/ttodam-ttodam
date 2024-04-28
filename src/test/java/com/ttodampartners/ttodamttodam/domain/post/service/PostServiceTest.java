package com.ttodampartners.ttodamttodam.domain.post.service;

import com.ttodampartners.ttodamttodam.domain.post.dto.*;
import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.post.exception.PostException;
import com.ttodampartners.ttodamttodam.domain.post.repository.PostRepository;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @Test
    void CREATE_POST_TEST(){
        // 테스트 상품 생성
        ProductAddDto testProduct = testProduct("test product");
        // 테스트 게시물 생성
        PostCreateDto testPost = testPost("이미지 test",testProduct);

        // 가짜 이미지 파일 생성
        List<MultipartFile> imageFiles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            byte[] imageBytes = ("test image " + (i + 1)).getBytes();
            MultipartFile imageFile = new MockMultipartFile("imageFiles", "test-image-" + (i + 1) + ".jpg", "image/jpeg", imageBytes);
            imageFiles.add(imageFile);
        }

        PostEntity post = postService.createPost(5L, imageFiles, testPost);

        Optional<PostEntity> optionalPost = postRepository.findById(post.getPostId());
        assertTrue(optionalPost.isPresent());

        PostEntity result = optionalPost.get();
        assertEquals("이미지 test", result.getTitle());
    }
    private static PostCreateDto testPost(String title, ProductAddDto product){
        return PostCreateDto.builder()
                .title(title)
                .participants(2)
                .place("서울특별시 중구 소공동 세종대로18길 2")
                .deadline(LocalDateTime.of(2024, 4, 17, 0, 0))
                .status(PostEntity.Status.IN_PROGRESS)
                .category(PostEntity.Category.DAILY)
                .content("test content")
                .products(Collections.singletonList(product))
                .build();
    }
    private static ProductAddDto testProduct(String productName){
        return ProductAddDto.builder()
                .productName(productName)
                .count(10)
                .price(10000L)
                .purchaseLink(":https://smartstore.naver.com/sskorea777/products/6364217638")
                .build();
    }

    @Test
    void GET_POST_LIST_TEST(){
        Long userId = 3L;

        List<PostListDto> postList = postService.getPostList(userId);

        assertEquals(2, postList.size());
    }

    @Test
    void GET_CATEGORY_POST_LIST_TEST(){
        Long userId = 1L;
        String category = "생활용품";

        List<PostListDto> postList = postService.getCategoryPostList(userId,category);

        assertEquals(3, postList.size());
    }


    @Test
    void GET_USERS_POST_LIST_TEST(){
        Long userId = 3L;

        List<PostListDto> userPostList = postService.getUsersPostList(userId);

        assertEquals(3, userPostList.size());
    }

    @Test
    void SEARCH_POST_LIST_TEST() {

        String search = "물티슈";

        List<PostListDto> searchPostList = postService.searchPostList(search);

        assertEquals(3, searchPostList.size());
    }

    @Test
    void GET_POST_TEST(){
        Long userId = 5L;
        Long postId = 63L;

        PostDetailDto testPost = postService.getPost(userId, postId);

        assertEquals("NONE", testPost.getLoginUserRequestStatus());

    }

    @Test
    void UPDATE_POST_TEST(){
        // 게시물 가져오기
        PostEntity testPost = postRepository.findById(63L)
                .orElseThrow(() -> new PostException(ErrorCode.NOT_FOUND_POST));

        // 새로운 이미지 업로드를 위한 가짜 이미지 파일 생성
        List<MultipartFile> newImageFiles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            byte[] imageBytes = ("new test image " + (i + 1)).getBytes();
            MultipartFile imageFile = new MockMultipartFile("imageFiles", "new-test-image-" + (i + 1) + ".jpg", "image/jpeg", imageBytes);
            newImageFiles.add(imageFile);
        }

        // 게시물 업데이트
        PostUpdateDto postUpdateDto = new PostUpdateDto();
        postUpdateDto.setTitle("Updated Title");
        postUpdateDto.setParticipants(5);
        postUpdateDto.setPlace("서울특별시 마포구 양화로 지하188");
        List<String> imgUrls = new ArrayList<>();
        imgUrls.add("https://ttodam-ttodam.s3.ap-northeast-2.amazonaws.com/남은 기존 이미지.jpg");
        postUpdateDto.setImgUrls(imgUrls);

        postService.updatePost(3L, 63L, newImageFiles, postUpdateDto);

        // 업데이트된 게시물 확인
        Optional<PostEntity> optionalPost = postRepository.findById(testPost.getPostId());
        assertTrue(optionalPost.isPresent());

        PostEntity updatedPost = optionalPost.get();
        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals(5, updatedPost.getParticipants());
        assertEquals("서울특별시 마포구 양화로 지하188", updatedPost.getPlace());

    }

    @Test
    void UPDATE_POST_PURCHASE_STATUS_TEST(){
        Long userId = 3L;
        Long postId = 59L;

        PostEntity updateRequest = postService.updatePurchaseStatus(userId, postId,"성공");

        assertEquals(PostEntity.PurchaseStatus.SUCCESS, updateRequest.getPurchaseStatus());
    }

    @Test
    void DELETE_POST_TEST(){
        Long userId = 2L;
        Long postId = 58L;

        postService.deletePost(userId, postId);

        assertFalse(postRepository.existsById(58L));

    }


}