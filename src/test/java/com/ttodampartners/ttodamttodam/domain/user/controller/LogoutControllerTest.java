package com.ttodampartners.ttodamttodam.domain.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import com.ttodampartners.ttodamttodam.domain.user.service.LogoutService;
import com.ttodampartners.ttodamttodam.global.config.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
@DisplayName("로그아웃 테스트")
class LogoutControllerTest {
  @Mock
  private TokenProvider tokenProvider;

  @Mock
  private LogoutService logoutService;

  @InjectMocks
  private LogoutController logoutController;

  @Test
  void testLogout() {
    // given
    HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
    String expectedToken = "testToken";

    Mockito.when(tokenProvider.resolveTokenFromRequest(mockRequest)).thenReturn(expectedToken);
    Mockito.doNothing().when(logoutService).addTokenToBlacklist(expectedToken);

    // when
    ResponseEntity<String> response = logoutController.logout(mockRequest);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("정상적으로 로그아웃 됐습니다.", response.getBody());
    verify(tokenProvider).resolveTokenFromRequest(mockRequest);
    verify(logoutService).addTokenToBlacklist(expectedToken);
  }
}