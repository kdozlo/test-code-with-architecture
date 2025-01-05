package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMaliSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void init() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeMaliSender fakeMaliSender = new FakeMaliSender();

        this.userService = UserService.builder()
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .clockHolder(new TestClockHolder(1234567L))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMaliSender))
                .build();

        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("test")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());

        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("test2@naver.com")
                .nickname("test2")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
    }

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
    public void userCreate를_이용하여_유저를_생성할_수_있다() throws Exception {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("kdy@test.com")
                .address("Masan")
                .nickname("kdy")
                .build();

        //when
        User result = userService.create(userCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    public void userUpdate를_이용하여_유저를_수정할_수_있다() throws Exception {
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
        assertThat(user.getLastLoginAt()).isEqualTo(1234567L);
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
