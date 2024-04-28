package com.ttodampartners.ttodamttodam.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  /*
  User Exception
   */
  NOT_FOUND_USER("해당 유저를 찾지 못했습니다."),
  NOT_FOUND_EMAIL("해당 이메일로 등록된 유저를 찾지 못했습니다."),
  EXISTS_EMAIL("이미 등록된 이메일입니다."),
  EXISTS_PHONE("이미 등록된 휴대폰 번호입니다."),
  EXISTS_NICKNAME("이미 등록된 닉네임입니다."),
  NOT_MATCH_PASSWORD("비밀번호가 일치하지 않습니다."),
  SIGNIN_TIME_OUT("재로그인이 필요합니다."),
  PERMISSION_DENIED("권한이 없습니다."),
  SOCIAL_ACCOUNTS_IMPOSSIBLE("소셜 계정은 비밀번호 설정이 불가합니다."),
  ALREADY_LOGOUT("해당 계정은 이미 로그아웃됐습니다."),

  /*
  Mail Exception
   */
  NOT_CERTIFIED("메일 인증을 진행해 주세요."),
  AUTH_KEY_MISMATCH("인증키가 일치하지 않습니다."),
  MAIL_CREATION_ERROR("메일을 생성하는 중 오류가 발생했습니다."),

  /*
  Aws Exception
   */
  UPLOAD_FAILED("파일 업로드를 실패했습니다."),

  /*
  Coordinate Exception
   */
  API_REQUEST_FAILED("API 요청 실패"),
  NOT_FOUND_ADDRESS("주소 정보를 찾을 수 없습니다."),

  /*
  Keyword Exception
  */
  NOT_FOUND_KEYWORD("해당 키워드는 이미 삭제되었습니다."),
  ALREADY_EXISTS_KEYWORD("이미 등록된 키워드입니다."),

  /*
  Post Exception
   */
  NOT_FOUND_POST("해당 게시글을 찾지 못했습니다."),
  NOT_FOUND_PRODUCT("해당 게시글 상품을 찾지 못했습니다."),
  POST_PERMISSION_DENIED("해당 게시글에 권한이 없습니다."),
  POST_READ_PERMISSION_DENIED("본인의 거주지 주변 게시글만 조회가 가능합니다."),
  POST_PURCHASE_STATUS_NOT_SUCCESS("공동구매가 완료되어야 평가할 수 있습니다."),
  NOT_ACCEPTED_MEMBER("해당 게시글의 수락된 요청자가 아닙니다."),
  /*
  Bookmark Exception
   */
  NOT_FOUND_BOOKMARK("해당 북마크를 찾을 수 없습니다."),
  BOOKMARK_PERMISSION_DENIED("해당 북마크에 권한이 없습니다."),

  /*
  Request Exception
   */
  NOT_FOUND_REQUEST("해당 요청은 찾을 수 없습니다."),
  POST_STATUS_COMPLETED("모집이 완료된 게시글입니다."),
  POST_STATUS_FAILED("모집이 종료된 게시글입니다."),
  POST_STATUS_IN_PROGRESS("모집이 진행중인 게시글입니다."),
  REQUEST_PERMISSION_DENIED("해당 참여요청에 권한이 없습니다."),

  /*
  Chatroom Exception
   */
  CHATROOM_ALREADY_EXIST("개인 채팅방이 이미 존재합니다."),
  GROUP_CHATROOM_ALREADY_EXIST("단체 채팅방이 이미 존재합니다."),
  CHATROOM_CREATE_DENIED("채팅방 생성은 모집중 상태에만 가능합니다."),
  USER_CHATROOM_NOT_EXIST("해당 유저가 속한 채팅방이 존재하지 않습니다."),
  USER_NOT_IN_CHATROOM("해당 유저가 채팅방에 속해있지 않아 채팅방을 나갈 수 없습니다."),
  CHATROOM_NOT_FOUND("채팅방이 존재하지 않습니다."),
  CHATROOM_MESSAGE_NOT_FOUND("이전 채팅 기록이 존재하지 않습니다."),
  /*
    Stomp Exception
   */
  INVALID_MESSAGE("유효하지 않은 메시지입니다."),
  HEADER_NOT_FOUND("유효한 헤더가 존재하지 않습니다."),

  /*
  Notification Exception
   */
  SSE_SEND_FAILED("알림 전송을 실패했습니다."),
  NOT_FOUND_NOTIFICATION("해당 알림은 이미 삭제되었습니다.")
  ;

  private final String description;
}
