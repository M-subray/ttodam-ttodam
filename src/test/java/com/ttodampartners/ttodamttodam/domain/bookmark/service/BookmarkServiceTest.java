package com.ttodampartners.ttodamttodam.domain.bookmark.service;

import com.ttodampartners.ttodamttodam.domain.bookmark.dto.BookmarkDto;
import com.ttodampartners.ttodamttodam.domain.bookmark.entity.BookmarkEntity;
import com.ttodampartners.ttodamttodam.domain.bookmark.repository.BookmarkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookmarkServiceTest {
    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Test
    void CREATE_BOOKMARK_TEST(){
        Long postId = 63L;

        BookmarkEntity bookmark =  bookmarkService.createBookmark(postId);

        Optional<BookmarkEntity> optionalBookmark = bookmarkRepository.findById(bookmark.getBookmarkId());
        assertTrue(optionalBookmark.isPresent());
    }

    @Test
    void GET_BOOKMARK_LIST_TEST(){

        List<BookmarkDto> bookmarkList = bookmarkService.getBookmarkList();

        assertEquals(2, bookmarkList.size());
    }

    @Test
    void DELETE_BOOKMARK_TEST(){

        Long bookmarkId = 3L;

        bookmarkService.deleteBookmark(bookmarkId);

        assertFalse(bookmarkRepository.existsById(3L));

    }
}