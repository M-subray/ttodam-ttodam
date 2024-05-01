package com.ttodampartners.ttodamttodam.domain.bookmark.repository;

import com.ttodampartners.ttodamttodam.domain.bookmark.entity.BookmarkEntity;
import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    List<BookmarkEntity> findByUserId(Long userId);
    List<BookmarkEntity> findAllByPost_PostId(Long postId);
    Optional<BookmarkEntity> findByPost_PostIdAndUserId(Long postId, Long userId);

    boolean existsByBookmarkUserAndPost(UserEntity user, PostEntity post);
}
