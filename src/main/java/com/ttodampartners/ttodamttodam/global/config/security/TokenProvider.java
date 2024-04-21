package com.ttodampartners.ttodamttodam.global.config.security;

import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.service.AuthenticationService;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {

  @Value("${spring.jwt.secret}")
  private String secretKey;

  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;
  private final AuthenticationService authenticationService;

  public String generateToken(String username) {
    Claims claims = Jwts.claims().setSubject(username);
    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS512, this.secretKey)
        .compact();
  }

  public Authentication getAuthentication(String jwt) {
    UserDetails userDetails =
        this.authenticationService.loadUserByUsername(this.getUsername(jwt));
    return new UsernamePasswordAuthenticationToken(userDetails, "",
        userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return this.parseClaims(token).getSubject();
  }

  public boolean validateToken(String token) {
    if (!StringUtils.hasText(token)) return false;

    Claims claims = this.parseClaims(token);
    return !claims.getExpiration().before(new Date());
  }

  public Claims parseClaims(String token) {
    try {
      return Jwts.parserBuilder().
          setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  // request 에서 token 가져오기
  public String resolveTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      return token.substring(7);
    } else {
      return null;
    }
  }

  // 토큰의 남은 시간 가져오기 (토큰 blacklist Redis 저장 때 duration 지정 위해 쓰임)
  public long calculateRemainingTime(Date expiration) {
    Date now = new Date();
    return expiration.getTime() - now.getTime();
  }
}
