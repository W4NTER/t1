package ru.t1.service;

import ru.t1.dto.request.UserRequest;
import ru.t1.dto.response.UserResponse;

public interface UserService {
    UserResponse create(UserRequest user);

    UserResponse update(UserRequest user, Long id);

    UserResponse getUser(Long id);

    void delete(Long id);
}
