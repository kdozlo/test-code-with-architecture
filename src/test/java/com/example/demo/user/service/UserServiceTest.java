package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    public void getByEmail은_Active_상태인_유저를_찾아올_수_있다() throws Exception {
        //given
        String email = "test@test.com";

        //when
        User result = userService.getByEmail(email);

        //then
        assertThat(result.getNickname()).isEqualTo("test");
    }

    @Test
    public void getByEmail은_PENDING_상태인_유저를_찾아올_수_있다() throws Exception {
        //given
        String email = "test2@test.com";

        //when

        //then
        assertThatThrownBy(() -> {
            User result = userService.getByEmail(email);
            assertThat(result.getNickname()).isEqualTo("test2");
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    public void getById은_Active_상태인_유저를_찾아올_수_있다() throws Exception {
        //given
        long id = 1L;

        //when
        User result = userService.getById(id);

        //then
        assertThat(result.getNickname()).isEqualTo("test");
    }

    @Test
    public void getById은_PENDING_상태인_유저를_찾아올_수_있다() throws Exception {
        //given
        long id = 2L;

        //when

        //then
        assertThatThrownBy(() -> {
            User result = userService.getById(id);
            assertThat(result.getNickname()).isEqualTo("test2");
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void userCreateDto를_이용하여_유저를_생성할_수_있다() throws Exception {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("kdy@test.com")
                .address("Masan")
                .nickname("kdy")
                .build();

        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        //when
        User result = userService.create(userCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        // todo : 인증값 test code 필요!
        // assertThat(result.getCertificationCode()).isEqualTo("");
    }

    @Test
    public void userUpdateDto를_이용하여_유저를_수정할_수_있다() throws Exception {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Masan")
                .nickname("kdy")
                .build();

        //when
        userService.update(1, userUpdate);

        //then
        User user = userService.getById(1);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getAddress()).isEqualTo("Masan");
        assertThat(user.getNickname()).isEqualTo("kdy");
    }

    @Test
    public void user를_로그인_시키면_마지막_로그인_시간이_변경된다() throws Exception {
        //given

        //when
        userService.login(1);

        //then
        User user = userService.getById(1);
        assertThat(user.getLastLoginAt()).isGreaterThan(0L);
        // todo : 마지막 로그인 시간 test code 필요!
        // assertThat(result.getLastLoginAt()).isEqualTo("");
    }

    @Test
    public void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() throws Exception {
        //given

        //when
        userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        //then
        User user = userService.getById(2);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() throws Exception {
        //given

        //when

        //then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaC");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}