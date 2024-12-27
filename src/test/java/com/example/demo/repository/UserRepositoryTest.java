package com.example.demo.repository;


import com.example.demo.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource("classpath:test-application.properties")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_가_제대로_연결되었다() throws Exception {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("test");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa");

        //when
        UserEntity result = userRepository.save(userEntity);

        //then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    public void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() throws Exception {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("test");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() throws Exception {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("test");
        userEntity.setCertificationCode("aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.ACTIVE);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() throws Exception {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("test");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("test@test.com", UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() throws Exception {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@test.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("test");
        userEntity.setCertificationCode("aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("test@test.com", UserStatus.ACTIVE);

        //then
        assertThat(result.isEmpty()).isTrue();
    }
}