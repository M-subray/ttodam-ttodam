package com.ttodampartners.ttodamttodam.domain.post.repository;

import com.ttodampartners.ttodamttodam.domain.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findByUserId(Long userId);
    List<PostEntity> findByCategory(PostEntity.Category category);
    @Query("SELECT DISTINCT p FROM post p LEFT JOIN p.products pr " +
            "WHERE p.title LIKE %:search% OR p.content LIKE %:search% OR pr.productName LIKE %:search%")
    List<PostEntity> findBySearch(@Param("search") String search);

}
