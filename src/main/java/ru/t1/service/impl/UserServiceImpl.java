package ru.t1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.aspect.annotation.LogTracking;
import ru.t1.dto.request.UserRequest;
import ru.t1.dto.response.UserResponse;
import ru.t1.entity.User;
import ru.t1.exceptions.LoginAlreadyInUseException;
import ru.t1.exceptions.UserNotFoundException;
import ru.t1.repository.UserRepository;
import ru.t1.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserServiceImpl(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public UserResponse create(UserRequest user) {
        Optional<User> userEntity = userRepository.findByLogin(user.login());

        if (userEntity.isPresent()) {
            throw new LoginAlreadyInUseException(user.login());
        } else {
            User newUser = new User(user.login(), user.password());
            return objectMapper.convertValue(
                    userRepository.save(newUser), UserResponse.class);
        }
    }

    @Override
    @Transactional
    @LogTracking
    public UserResponse update(UserRequest user, Long id) {
        User userEntity = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));

        Optional<User> userByLogin = userRepository.findByLogin(user.login());
        if (userByLogin.isPresent() && !userByLogin.get().getId().equals(userEntity.getId())) {
            throw new LoginAlreadyInUseException(user.login());
        } else {
            userEntity.setLogin(user.login());
            userEntity.setPassword(user.password());
            return objectMapper.convertValue(userEntity, UserResponse.class);
        }
    }

    @Override
    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));
        return objectMapper.convertValue(user, UserResponse.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
