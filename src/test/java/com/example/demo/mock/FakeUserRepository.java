package com.example.demo.mock;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {

    private Long autoGeneratedId = 0L;
    private final List<User> data = new ArrayList<>();


    @Override
    public User getById(long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return data.stream().filter(item ->
                item.getId().equals(id) && item.getStatus() == userStatus).findAny();
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return data.stream().filter(item ->
                item.getEmail().equals(email) && item.getStatus() == userStatus).findAny();
    }

    /** todo : update와 save를 정확하게 구분해서 구현하기
     * save에 update와 save 모두를 구현하는건 JPA의 동작 원리다
     * -> 이렇게 구현하면 jpa에 의존적인 기능이 된다.
     */
    @Override
    public User save(User user) {
        if(user.getId() == null || user.getId() == 0) {
            User newUser = User.builder()
                    .id(++autoGeneratedId)
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .address(user.getAddress())
                    .certificationCode(user.getCertificationCode())
                    .status(user.getStatus())
                    .lastLoginAt(user.getLastLoginAt())
                    .build();
            data.add(newUser);

            return newUser;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), user.getId()));
            data.add(user);

            return user;
        }
    }

    @Override
    public Optional<User> findById(long id) {

        return data.stream().filter(item -> item.getId().equals(id)).findAny();
    }
}
