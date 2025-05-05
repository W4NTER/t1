package ru.t1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.dto.request.UserRequest;
import ru.t1.dto.response.UserResponse;
import ru.t1.entity.User;
import ru.t1.exceptions.LoginAlreadyInUseException;
import ru.t1.exceptions.UserNotFoundException;
import ru.t1.repository.UserRepository;
import ru.t1.service.UserService;
import ru.t1.service.util.validator.UserValidator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserValidator userValidator;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                userRepository,
                objectMapper,
                userValidator
        );
    }

    @Test
    @DisplayName("тест создания юзера")
    void testThatCreateUserReturnedSucceed() {
        UserRequest userRequest = new UserRequest(
                "Ryan Gosling",
                "pass",
                "ryan_gosling@mail.ru"
        );
        User user = new User(
                "Ryan Gosling",
                "pass",
                "ryan_gosling@mail.ru"
        );
        UserResponse userResponse = new UserResponse(
                1L,
                "Ryan Gosling",
                "pass",
                "ryan_gosling@mail.ru"
        );


        when(userRepository.findByLogin(userRequest.login())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(objectMapper.convertValue(user, UserResponse.class)).thenReturn(userResponse);

        UserResponse res = userService.create(userRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());
        verify(userValidator).validateEmail(userRequest.email());

        assertNotNull(res);
        assertEquals(userResponse.login(), res.login());
        assertEquals(userResponse.password(), res.password());
        assertEquals(userResponse.email(), res.email());

        assertEquals(userResponse.login(), userCaptor.getValue().getLogin());
        assertEquals(userResponse.password(), userCaptor.getValue().getPassword());
        assertEquals(userResponse.email(), userCaptor.getValue().getEmail());
    }

    @Test
    @DisplayName("тест на выброс ошибки при зарегистрированном email")
    void testThatCreateUserWithNotUniqueLoginThrowingException() {
        User user = new User(
                "Ryan Gosling",
                "pass",
                "ryan_gosling@mail.ru"
        );
        UserRequest userRequest = new UserRequest(
                "Ryan Gosling",
                "pass",
                "ryan_gosling@mail.ru"
        );

        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        assertThrows(LoginAlreadyInUseException.class,
                () -> userService.create(userRequest));
    }

    @Test
    @DisplayName("тест обновления юзера, позитивный сценарий")
    void testThatUpdateUserReturnedSucceed() {
        UserRequest userRequest = new UserRequest(
                "Ryan Gosling",
                "pass",
                "ryan_gosling@mail.ru"
        );
        User user = new User(
                "Sam",
                "password",
                "sam_sam_sam@mail.ru"
        );
        UserResponse userResponse = new UserResponse(
                1L,
                "Ryan Gosling",
                "pass",
                "ryan_gosling@mail.ru"
        );

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.findByLogin(userRequest.login())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.convertValue(user, UserResponse.class)).thenReturn(userResponse);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        UserResponse res = userService.update(userRequest, 1L);

        verify(userValidator).validateEmail(user.getEmail());
        verify(userRepository).save(userCaptor.capture());

        assertNotNull(res);
        assertEquals(userResponse.login(), res.login());
        assertEquals(userResponse.password(), res.password());
        assertEquals(userResponse.email(), res.email());

        assertEquals(userRequest.login(), userCaptor.getValue().getLogin());
        assertEquals(userRequest.password(), userCaptor.getValue().getPassword());
        assertEquals(userRequest.email(), userCaptor.getValue().getEmail());
    }

    @Test
    @DisplayName("тест обновления юзера, юзер не найден")
    void testThatUpdateUserWitchNotExistsThrowingException() {
        UserRequest userRequest = new UserRequest(
                "Ryan Gosling",
                "pass",
                "ryan_gosling@mail.ru"
        );

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.update(userRequest, 1L));
    }

    @Test
    @DisplayName("тест обновления юзера, логин занят")
    void testThatUpdateUserLoginExistsThrowingException() {
        UserRequest userRequest = new UserRequest(
                "Ryan Gosling",
                "pass",
                "ryan_gosling@mail.ru"
        );
        User existingUser = new User(
                "Sam",
                "password",
                "sam_sam_sam@mail.ru"
        );
        User userWithSameLogin = new User(
                "Ryan Gosling",
                "otherpass",
                "ryan_other@mail.ru");

        User spyExistingUser = spy(existingUser);
        User spyUserWithSameLogin = spy(userWithSameLogin);

        when(spyExistingUser.getId()).thenReturn(1L);
        when(spyUserWithSameLogin.getId()).thenReturn(2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(spyExistingUser));
        when(userRepository.findByLogin(any())).thenReturn(Optional.of(spyUserWithSameLogin));

        assertThrows(LoginAlreadyInUseException.class,
                () -> userService.update(userRequest, 1L));
    }

    @Test
    @DisplayName("тест получения юзера по id")
    void testThatGetUserByIdReturnedSucceed() {
        Long userId = 1L;
        User user = new User(
                "Sam",
                "password",
                "sam_sam_sam@mail.ru"
        );
        UserResponse userResponse = new UserResponse(
                1L,
                "Sam",
                "password",
                "sam_sam_sam@mail.ru"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(objectMapper.convertValue(user, UserResponse.class)).thenReturn(userResponse);

        UserResponse res = userService.getUser(userId);

        assertNotNull(res);
        assertEquals(user.getLogin(), res.login());
        assertEquals(user.getPassword(), res.password());
        assertEquals(user.getEmail(), res.email());
    }

    @Test
    @DisplayName("тест получения юзера по id, юзер не найден")
    void testThatGetUserByIdThrowingException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUser(1L));
    }

    @Test
    @DisplayName("тест удаления юзера по id")
    void testThatDeleteUserReturnedSucceed() {
        Long userId = 1L;
        User user = new User(
                "Sam",
                "password",
                "sam_sam_sam@mail.ru"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.delete(userId);

        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("тест удаления юзера по id, userNotFound")
    void testThatDeleteUserThrowingException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.delete(userId));
    }
}