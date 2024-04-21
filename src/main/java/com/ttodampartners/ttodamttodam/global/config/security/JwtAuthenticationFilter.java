package com.ttodampartners.ttodamttodam.global.config.security;

import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import com.ttodampartners.ttodamttodam.global.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final TokenProvider tokenProvider;
  private final RedisUtil redisUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = tokenProvider.resolveTokenFromRequest(request);

    if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {

      // 토큰이 이미 로그아웃 됐는지 체크
      if (redisUtil.keyExists(token)) {
        log.error("에러코드: {}, 에러 메시지: {}",
            ErrorCode.ALREADY_LOGOUT, ErrorCode.ALREADY_LOGOUT.getDescription());

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.getWriter().write(ErrorCode.ALREADY_LOGOUT.getDescription());

        return;
      }

      Authentication auth = this.tokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }
}
