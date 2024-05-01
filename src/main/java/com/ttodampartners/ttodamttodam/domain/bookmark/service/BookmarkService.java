package com.ttodampartners.ttodamttodam.domain.bookmark.service;

import com.ttodampartners.ttodamttodam.domain.bookmark.dto.BookmarkDto;
import com.ttodampartners.ttodamttodam.domain.bookmark.entity.BookmarkEntity;
import com.ttodampartners.ttodamttodam.domain.bookmark.exception.BookmarkException;
import com.ttodampartners.ttodamttodam.domain.bookmark.repository.BookmarkRepository;
import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.post.exception.PostException;
import com.ttodampartners.ttodamttodam.domain.post.repository.PostRepository;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.domain.user.util.AuthenticationUtil;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public BookmarkEntity createBookmark(Long postId) {
        UserEntity user = getUser();

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.NOT_FOUND_POST));

        BookmarkEntity bookmark = BookmarkEntity.builder()
                .user(user)
                .post(post)
                .build();

        return bookmarkRepository.save(bookmark);
    }

    @Transactional
    public List<BookmarkDto> getBookmarkList() {
        UserEntity user = getUser();

        List<BookmarkEntity> bookmarkList = bookmarkRepository.findByUserId(user.getId());

        return bookmarkList.stream()
                .map(BookmarkDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        UserEntity user = getUser();

        BookmarkEntity bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new BookmarkException(ErrorCode.NOT_FOUND_BOOKMARK));

        Long bookmarkUserId = bookmark.getUser().getId();

        // 권한 인증
        if (!user.getId().equals(bookmarkUserId)) {
            throw new BookmarkException(ErrorCode.BOOKMARK_PERMISSION_DENIED);
        }

        bookmarkRepository.delete(bookmark);
    }

    private UserEntity getUser () {
        Authentication authentication = AuthenticationUtil.getAuthentication();
        UserEntity user = userRepository.findByEmail(authentication.getName()).orElseThrow(() ->
                new UserException(ErrorCode.NOT_FOUND_USER));

        Optional<String> location = Optional.ofNullable(user.getLocation());

        if (location.isEmpty() || location.get().equals("null")) {
            throw new UserException(ErrorCode.NOT_UPDATE_PROFILE);
        }

        return user;
    }

    // 게시글 삭제 시 북마크도 함께 삭제
    @Transactional
    public void deleteBookmarksByPost(Long postId) {
        List<BookmarkEntity> bookmarks = bookmarkRepository.findAllByPost_PostId(postId);
        bookmarkRepository.deleteAll(bookmarks);
    }

    // 회원 탈퇴 시 북마크도 함께 삭제
    @Transactional
    public void deleteBookmarksByUser(Long userId) {
        List<BookmarkEntity> bookmarks = bookmarkRepository.findByUserId(userId);
        bookmarkRepository.deleteAll(bookmarks);
    }

}
