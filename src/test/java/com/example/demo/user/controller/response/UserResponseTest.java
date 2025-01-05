package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseTest {

    @Test
    public void User으로_응답을_생성할_수_있다() throws Exception {
        //given
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("test")
                .address("Masan")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        //when
        UserResponse userResponse = UserResponse.from(user);

        //then
        assertThat(userResponse.getId()).isEqualTo(1L);
        assertThat(userResponse.getEmail()).isEqualTo("test@test.com");
        assertThat(userResponse.getNickname()).isEqualTo("test");
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
    }
}
