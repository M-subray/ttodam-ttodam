package com.ttodampartners.ttodamttodam.domain.user.service;

import com.ttodampartners.ttodamttodam.domain.user.dto.MannersDto;
import com.ttodampartners.ttodamttodam.domain.user.entity.UserEntity;
import com.ttodampartners.ttodamttodam.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MannersEvaluateCheckServiceTest {

    @Autowired
    private MannersEvaluateService mannersEvaluateService;

    @Test
    void EVALUATE_MANNERS_TEST(){
        Long userId = 1L;
        Long memberId = 2L;
        Long postId = 59L;

        MannersDto mannersDto = new MannersDto();
        mannersDto.setManners(4.5);

        UserEntity member = mannersEvaluateService.evaluateManners(userId, postId, memberId, mannersDto);

        assertEquals(4.5, member.getManners());
    }


}