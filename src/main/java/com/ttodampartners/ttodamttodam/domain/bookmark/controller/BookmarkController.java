package com.ttodampartners.ttodamttodam.domain.bookmark.controller;

import com.ttodampartners.ttodamttodam.domain.bookmark.dto.BookmarkDto;
import com.ttodampartners.ttodamttodam.domain.bookmark.service.BookmarkService;
import com.ttodampartners.ttodamttodam.global.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RequiredArgsConstructor
@RestController
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/post/{postId}/bookmark")
    public ResponseEntity<BookmarkDto> createBookmark(
            @PathVariable Long postId
        ) {
        return ResponseEntity.ok(BookmarkDto.of(bookmarkService.createBookmark(postId)));
       }

    @GetMapping("/post/bookmark")
    public ResponseEntity<List<BookmarkDto>> getBookmarkList(
    ){
        List<BookmarkDto> bookmarkList = bookmarkService.getBookmarkList();
        return ResponseEntity.ok(bookmarkList);
    }

    @DeleteMapping("/post/bookmark/{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(
            @PathVariable Long bookmarkId
    )
    {
        bookmarkService.deleteBookmark(bookmarkId);
        return ResponseEntity.status(OK).build();
    }
}
