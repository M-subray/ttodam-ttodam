package com.ttodampartners.ttodamttodam.global.error;

import com.ttodampartners.ttodamttodam.domain.bookmark.exception.BookmarkException;
import com.ttodampartners.ttodamttodam.domain.keyword.exception.KeywordException;
import com.ttodampartners.ttodamttodam.domain.chat.dto.ChatExceptionResponse;
import com.ttodampartners.ttodamttodam.domain.chat.exception.ChatroomException;
import com.ttodampartners.ttodamttodam.domain.chat.exception.ChatroomStringException;
import com.ttodampartners.ttodamttodam.domain.notification.exception.NotificationException;
import com.ttodampartners.ttodamttodam.domain.post.exception.PostException;
import com.ttodampartners.ttodamttodam.domain.request.exception.RequestException;
import com.ttodampartners.ttodamttodam.domain.user.exception.AwsException;
import com.ttodampartners.ttodamttodam.domain.user.exception.CoordinateException;
import com.ttodampartners.ttodamttodam.infra.email.exception.MailException;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserException.class)
  public ResponseEntity<String> userExceptionHandle(UserException e) {
    log.error("에러코드: {}, 에러 메시지: {}", e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorMessage());
  }

  @ExceptionHandler(MailException.class)
  public ResponseEntity<String> mailExceptionHandle(MailException e) {
    log.error("에러코드: {}, 에러 메시지: {}", e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorMessage());
  }

  @ExceptionHandler(AwsException.class)
  public ResponseEntity<String> awsExceptionHandle(AwsException e) {
    log.error("에러코드: {}, 에러 메시지: {}", e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorMessage());
  }

  @ExceptionHandler(CoordinateException.class)
  public ResponseEntity<String> coordinateExceptionHandle(CoordinateException e) {
    log.error("에러코드: {}, 에러 메시지: {}", e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorMessage());
  }

  @ExceptionHandler(KeywordException.class)
  public ResponseEntity<String> keywordExceptionHandle(KeywordException e) {
    log.error("에러코드: {}, 에러 메시지: {}", e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorMessage());
  }

  /*
    채팅방 관련 Exception 추가
  */
  @ExceptionHandler(ChatroomException.class)
  public ResponseEntity<ChatExceptionResponse> ChatroomExceptionHandler(ChatroomException e) {
    log.error("에러코드: {}, 에러 메시지: {}", e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getResponse());
  }

  @ExceptionHandler(ChatroomStringException.class)
  public ResponseEntity<String> ChatroomStringExceptionHandler(ChatroomStringException e) {
    log.error("에러코드: {}, 에러 메시지: {}", e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
  }

  @ExceptionHandler(NotificationException.class)
  public ResponseEntity<String> NotificationExceptionHandler(NotificationException e) {
    log.error("에러코드: {}, 에러 메시지: {}", e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
  }

  /*
    게시글 관련 Exception 추가
  */
  @ExceptionHandler(PostException.class)
  public ResponseEntity<String> PostExceptionHandler(PostException e) {
    log.error("에러코드: {}, 에러 메시지: {}", e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorMessage());
  }

  @ExceptionHandler(RequestException.class)
  public ResponseEntity<String> RequestExceptionHandler(RequestException e) {
    log.error("에러코드: {}, 에러 메시지: {}", e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
  }

  @ExceptionHandler(BookmarkException.class)
  public ResponseEntity<String> BookmarkExceptionHandle(BookmarkException e) {
    log.error("에러코드: {}, 에러 메시지: {}", e.getErrorCode(), e.getErrorMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> validationExceptionHandle(MethodArgumentNotValidException e) {
    String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
  }

  /*
    파일 크기가 1MB 보다 큰 경우(FileSizeLimitExceededException),
    하나의 요청이 기본 설정된 최대 용량을 초과했을 때(SizeLimitExceededException)
    발생하는 예외 처리
   */
  @ExceptionHandler({MaxUploadSizeExceededException.class})
  protected ResponseEntity<String> handleMultipartException(MaxUploadSizeExceededException e) {
    log.error("에러코드: MaxUploadSizeExceededException, 에러 메시지: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("1MB 미만의 파일만 업로드 가능합니다.");
  }

}