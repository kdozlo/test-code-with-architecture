package com.example.demo.user.controller.port;

import com.example.demo.user.controller.request.UserUpdateRequest;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;

public interface UserService {

    User getByEmail(String email);

    User getById(long id);

    User create(UserCreate userCreate);

    User update(long id, UserUpdateRequest userUpdate);

    void login(long id);

    void verifyEmail(long id, String certificationCode);
}
