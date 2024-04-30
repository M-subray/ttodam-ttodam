package com.ttodampartners.ttodamttodam.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MannersEvaluateUpdateDto {

  @NotEmpty(message = "거래한 유저에 대해 평가해 주세요.")
  private Map<Long, Double> mannersForMembers;
}
