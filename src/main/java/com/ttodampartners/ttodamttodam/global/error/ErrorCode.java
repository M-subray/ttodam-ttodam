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
  NOT_FOUND_ADDRESS("주소 정보를 찾을 수 없습니다.")
  ;

  private final String description;

}
