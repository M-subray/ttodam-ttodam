package com.ttodampartners.ttodamttodam.domain.post.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ttodampartners.ttodamttodam.domain.bookmark.entity.BookmarkEntity;
import com.ttodampartners.ttodamttodam.domain.bookmark.repository.BookmarkRepository;
import com.ttodampartners.ttodamttodam.domain.bookmark.service.BookmarkService;
import com.ttodampartners.ttodamttodam.domain.notification.service.NotificationService;
import com.ttodampartners.ttodamttodam.domain.post.dto.*;
import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.post.exception.PostException;
import com.ttodampartners.ttodamttodam.domain.post.repository.PostRepository;
import com.ttodampartners.ttodamttodam.domain.post.entity.ProductEntity;
import com.ttodampartners.ttodamttodam.domain.request.entity.RequestEntity;
import com.ttodampartners.ttodamttodam.domain.request.exception.RequestException;
import com.ttodampartners.ttodamttodam.domain.request.repository.RequestRepository;
import com.ttodampartners.ttodamttodam.domain.request.service.RequestService;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.domain.user.util.AuthenticationUtil;
import com.ttodampartners.ttodamttodam.domain.user.util.CoordinateFinderUtil;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkService bookmarkService;
    private final RequestService requestService;
    private final CoordinateFinderUtil coordinateFinderUtil;
    private final AmazonS3 amazonS3;
    private final NotificationService notificationService;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public PostEntity createPost(Long userId, List<MultipartFile> imageFiles, PostCreateDto postCreateDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        try {
            // S3에 저장된 이미지 url
            List<String> postImgUrls = uploadImageFilesToS3(imageFiles);

            PostEntity post = PostCreateDto.of(user,postImgUrls,postCreateDto);
            // 저장된 만남장소 주소정보로 위도,경도 저장
            double[] coordinates = coordinateFinderUtil.getCoordinates(postCreateDto.getPlace());
            post.setPLocationX(coordinates[1]); // 경도 설정
            post.setPLocationY(coordinates[0]); // 위도 설정

            postRepository.save(post);
            // 키워드(프로덕트 이름 리스트)로 알림 발송
            notificationService.sendNotificationForKeyword(postCreateDto, post);
            return post;
        } catch (IOException e) {

            throw new RuntimeException("위치 정보를 가져오는 동안 에러가 발생했습니다.", e);
        }
    }

    private List<String> uploadImageFilesToS3(List<MultipartFile> imageFiles) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile imageFile : imageFiles) {
            String originalFilename = imageFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String imageFileName = uuid + originalFilename;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageFile.getSize());
            metadata.setContentType(imageFile.getContentType());

            amazonS3.putObject(bucket, imageFileName, imageFile.getInputStream(), metadata);

            String imageUrl = amazonS3.getUrl(bucket, imageFileName).toString();
            imageUrls.add(imageUrl);
        }

        return imageUrls;
    }

    //로그인된 유저의 도로명 주소(-로)를 기준으로 게시글의 만남장소를 특정하여 게시글 목록 불러오기
    @Transactional
    public List<PostListDto> getPostList(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        String userRoadName = roadName(user.getLocation());

        List<PostEntity> postList = postRepository.findAll();

        // 유저와 동일한 도로명을 가진 게시글 필터링
        List<PostEntity> filteredPosts = postList.stream()
                .filter(post -> {
                    String postRoadName = roadName(post.getPlace());
                    return postRoadName.equals(userRoadName);
                })
                .collect(Collectors.toList());

        return filteredPosts.stream()
                .map(PostListDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostListDto> getCategoryPostList(Long userId, String category) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        String userRoadName = roadName(user.getLocation());

        PostEntity.Category currentCategory = PostEntity.Category.fromLabel(category);
        List<PostEntity> postList = postRepository.findByCategory(currentCategory);

        // 유저와 동일한 도로명을 가진 게시글 필터링
        List<PostEntity> filteredPosts = postList.stream()
                .filter(post -> {
                    String postRoadName = roadName(post.getPlace());
                    return postRoadName.equals(userRoadName);
                })
                .collect(Collectors.toList());

        return filteredPosts.stream()
                .map(PostListDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostListDto> getUsersPostList(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        List<PostEntity> usersPostList = postRepository.findByUserId(userId);

        return usersPostList.stream()
                .map(PostListDto::of)
                .collect(Collectors.toList());
    }

    public List<PostListDto> searchPostList(String word) {

        List<PostEntity> searchPostList = postRepository.findBySearch(word);

        return searchPostList.stream()
                .map(PostListDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDetailDto getPost(Long userId, Long postId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        String userRoadName = roadName(user.getLocation());

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.NOT_FOUND_POST));

        Long bookmarkId = 0L;

        // 북마크 확인
        Optional<BookmarkEntity> bookmarkOptional = bookmarkRepository.findByPost_PostIdAndUserId(postId, userId);
        if (bookmarkOptional.isPresent()) {
            // 북마크가 존재하면 북마크 ID를 받아옴
            bookmarkId = bookmarkOptional.get().getBookmarkId();
        }

        String postRoadName = roadName(post.getPlace());

        // 로그인 유저 거주지와 만남장소 비교
        if (!userRoadName.equals(postRoadName)) {
            throw new PostException(ErrorCode.POST_READ_PERMISSION_DENIED);
        }

        // 작성자인지 판별
        boolean isAuthor = post.getUser().getId().equals(userId);

        List<RequestEntity> requestList = requestRepository.findAllByPost_PostId(postId);

        String loginUserRequestStatus = isAuthor ? "AUTHOR" : "NONE";

        if (!isAuthor) {
            // 요청자인지 확인 및 요청 상태 반환
            if (requestList != null && !requestList.isEmpty()) {
                for (RequestEntity request : requestList) {
                    if (request.getRequestUser().getId().equals(userId)) {
                        // 요청자인 경우 상태 반환
                        if (request.getRequestStatus() == RequestEntity.RequestStatus.ACCEPT) {
                            loginUserRequestStatus = "ACCEPT";
                        } else if (request.getRequestStatus() == RequestEntity.RequestStatus.REFUSE) {
                            loginUserRequestStatus = "REFUSE";
                        } else {
                            loginUserRequestStatus = "WAIT";
                        }
                        break;
                    }
                }
            }
        }

        return PostDetailDto.of(post, requestList, loginUserRequestStatus,bookmarkId);
    }

    // 도로명 주소에서 -로 부분 추출
    private String roadName(String address) {
        Pattern pattern = Pattern.compile("(\\S+로)");
        Matcher matcher = pattern.matcher(address);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }

    @Transactional
    public PostEntity updatePost(Long userId, Long postId,List<MultipartFile> newImageFiles, PostUpdateDto postUpdateDto) {

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.NOT_FOUND_POST));

        validateAuthority(userId, post);

        // 새로운 이미지 업로드
        List<String> newImageUrls = new ArrayList<>();
        try {
            newImageUrls = uploadImageFilesToS3(newImageFiles);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
        }

        // 이미지목록 업데이트
        List<String> allImageUrls = new ArrayList<>();
        if (postUpdateDto.getImgUrls() != null) {
            allImageUrls.addAll(postUpdateDto.getImgUrls());
        }
        allImageUrls.addAll(newImageUrls);

        updateImages(post, allImageUrls);

        post.setTitle(postUpdateDto.getTitle());
        post.setParticipants(postUpdateDto.getParticipants());
        post.setDeadline(postUpdateDto.getDeadline());
        post.setCategory(postUpdateDto.getCategory());
        post.setContent(postUpdateDto.getContent());

        // 만남 장소 정보가 변경되었을 때 위도와 경도를 업데이트
        if (!post.getPlace().equals(postUpdateDto.getPlace())) {
            double[] coordinates = coordinateFinderUtil.getCoordinates(postUpdateDto.getPlace());
            post.setPLocationX(coordinates[1]); // 경도 설정
            post.setPLocationY(coordinates[0]); // 위도 설정
        }
        post.setPlace(postUpdateDto.getPlace());

        // 상품목록 업데이트
        updateProducts(post, postUpdateDto.getProducts());

        return post;
    }

    private void updateImages(PostEntity post, List<String> allImageUrls) {
        for (String postImageUrl : post.getImgUrls()) {
            if (!allImageUrls.contains(postImageUrl)) {
                deleteImageFileFromS3(postImageUrl);
            }
        }
        post.setImgUrls(allImageUrls);
    }

    private void updateProducts(PostEntity post, List<ProductUpdateDto> products) {
        if (post.getProducts() == null) {
            post.setProducts(new ArrayList<>());
        }

        for (ProductUpdateDto productUpdateDto : products) {
            ProductEntity product = post.getProducts().stream()
                    .filter(pi -> pi.getProductId().equals(productUpdateDto.getProductId()))
                    .findFirst().orElseThrow(() -> new PostException(ErrorCode.NOT_FOUND_PRODUCT));
            product.setProductName(productUpdateDto.getProductName());
            product.setCount(productUpdateDto.getCount());
            product.setPrice(productUpdateDto.getPrice());
            product.setPurchaseLink(productUpdateDto.getPurchaseLink());
        }
    }

    @Transactional
    public PostEntity updatePurchaseStatus(Long userId, Long postId,String purchaseStatus){
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.NOT_FOUND_POST));

        // 주최자 인증
        validateAuthority(userId, post);

        if (post.getStatus() == PostEntity.Status.IN_PROGRESS) {
            throw new RequestException(ErrorCode.POST_STATUS_IN_PROGRESS);
        } else if (post.getStatus() == PostEntity.Status.FAILED) {
            throw new RequestException(ErrorCode.POST_STATUS_FAILED);
        }

        PostEntity.PurchaseStatus status = PostEntity.PurchaseStatus.fromLabel(purchaseStatus);

        post.setPurchaseStatus(status);

        return post;
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.NOT_FOUND_POST));

        validateAuthority(userId, post);

        //게시글 이미지 S3에서 삭제
        for (String deleteImageUrl : post.getImgUrls()) {
            deleteImageFileFromS3(deleteImageUrl);
        }

        // 게시글 관련 북마크 삭제
        bookmarkService.deleteBookmarksByPost(postId);

        // 게시글 관련 참여요청 삭제
        requestService.deleteRequestsByPost(postId);

        postRepository.delete(post);
    }

    private void deleteImageFileFromS3(String postImgUrl) {
        String imagefileName = getImageFileNameFromUrl(postImgUrl);
        amazonS3.deleteObject(bucket,imagefileName);
    }

    private String getImageFileNameFromUrl(String imageUrl) {
        try {
            URI uri = new URI(imageUrl);
            String path = uri.getPath();

            String uuid = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
            return path.substring(path.lastIndexOf(uuid) + uuid.length());

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("유효하지 않은 이미지 URL입니다.");
        }
    }

    private void validateAuthority(Long userId, PostEntity post) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        Long postAuthorId = post.getUser().getId();

        if (!userId.equals(postAuthorId)) {
            throw new PostException(ErrorCode.POST_PERMISSION_DENIED);
        }
    }
}
