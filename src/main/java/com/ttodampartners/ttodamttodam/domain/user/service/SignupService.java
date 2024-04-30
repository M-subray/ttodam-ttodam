package com.ttodampartners.ttodamttodam.domain.user.service;

import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.dto.SignupRequestDto;
import com.ttodampartners.ttodamttodam.domain.user.exception.UserException;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import com.ttodampartners.ttodamttodam.global.error.ErrorCode;
import com.ttodampartners.ttodamttodam.global.util.RedisUtil;
import com.ttodampartners.ttodamttodam.infra.email.exception.MailException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignupService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RedisUtil redisUtil;

  @Transactional
  public void signup (SignupRequestDto signupRequestDto) {
    iskEmailExists(signupRequestDto.getEmail());
    isEmailVerified(signupRequestDto.getEmail());

    userRepository.save(UserEntity.builder()
        .email(signupRequestDto.getEmail())
        .password(passwordEncoder.encode(signupRequestDto.getPassword()))
        .evaluationNumber(0L)
        .build());
  }

  public void iskEmailExists (String email) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new UserException(ErrorCode.EXISTS_EMAIL);
    }
  }

  private void isEmailVerified (String email) {
    String confirmStatus = redisUtil.getValue(email);
    if (confirmStatus == null || !confirmStatus.equals("confirmed")) {
      throw new MailException(ErrorCode.NOT_CERTIFIED);
    }
  }
}
