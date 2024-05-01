package com.ttodampartners.ttodamttodam.domain.post.dto;

import java.util.List;
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
public class PostListWithUserAddressDto {
  private List<PostListDto> postList;
  private String loginUserLocation;
  private Double loginUserLocationX;
  private Double loginUserLocationY;
}
