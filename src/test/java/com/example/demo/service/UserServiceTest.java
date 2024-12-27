package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getByEmail은_Active_상태인_유저를_찾아올_수_있다() throws Exception {
        //given
        String email = "test@test.com";

        //when
        UserEntity result = userService.getByEmail(email);

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
            UserEntity result = userService.getByEmail(email);
            assertThat(result.getNickname()).isEqualTo("test2");
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    public void getById은_Active_상태인_유저를_찾아올_수_있다() throws Exception {
        //given
        long id = 1L;

        //when
        UserEntity result = userService.getById(id);

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
            UserEntity result = userService.getById(id);
            assertThat(result.getNickname()).isEqualTo("test2");
        }).isInstanceOf(ResourceNotFoundException.class);

    }
}