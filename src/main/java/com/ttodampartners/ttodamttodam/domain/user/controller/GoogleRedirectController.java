package com.ttodampartners.ttodamttodam.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@Slf4j
public class GoogleRedirectController {

  @Value("${oauth2.google.client-id}")
  private String clientId;
  @Value("${oauth2.google.redirect-uri}")
  private String redirectUrl;

  @GetMapping("/users/signin/google")
  public ResponseEntity<?> googleSignin () {
    String authUrl = UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/auth")
        .queryParam("client_id", clientId)
        .queryParam("redirect_uri", redirectUrl)
        .queryParam("response_type", "code")
        .queryParam("scope", "https://www.googleapis.com/auth/userinfo.email "
            + "https://www.googleapis.com/auth/userinfo.profile")
        .toUriString();

    log.info("구글 리다이렉트 성공");
    return ResponseEntity.status(HttpStatus.FOUND).header("Location", authUrl).build();
  }
}