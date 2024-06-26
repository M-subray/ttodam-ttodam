package com.ttodampartners.ttodamttodam.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic(HttpBasicConfigurer::disable)
        .csrf(CsrfConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(authorize ->
            authorize
                .requestMatchers("/users/signin/**").permitAll()
                .requestMatchers("/users/signup/**").permitAll()
                .requestMatchers("/login/**").permitAll()
                .requestMatchers("/post/**").authenticated()
                .requestMatchers("/request/**").authenticated()
                .requestMatchers("/users/{userId}/**").authenticated()
                .requestMatchers("/users/**").authenticated()
                .requestMatchers("/notifications/**").authenticated()
                .requestMatchers("/chatroom/**").authenticated()
                .requestMatchers("/chatrooms/**").authenticated()
                .requestMatchers("/chattings/").permitAll()
                .requestMatchers("/ws-chatting/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        );
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web ->
        web
            .ignoring()
            .requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
            ));
  }
}
