package ru.t1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.t1.dto.request.UserRequest;
import ru.t1.entity.User;
import ru.t1.repository.TaskRepository;
import ru.t1.repository.UserRepository;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        saveTestUsers();
    }

    @Test
    @DisplayName("тест регистрации нового пользователя")
    void testThatRegisterUserReturnsSucceed() throws Exception {
        UserRequest request = new UserRequest(
                "newUserLogin",
                "newUserPass",
                "newUserEmail@mail.com"
        );

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is(request.login())))
                .andExpect(jsonPath("$.password", is(request.password())))
                .andExpect(jsonPath("$.email", is(request.email())));
    }

    @Test
    @DisplayName("тест обновления пользователя")
    void testThatUpdateUserReturnsSucceed() throws Exception {
        User user = userRepository.findAll().getFirst();
        Long userId = user.getId();

        UserRequest request = new UserRequest(
                "updatedLogin",
                "updatedPass",
                "updatedEmail@mail.com"
        );

        mockMvc.perform(put("/user/update/{user_id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is(request.login())))
                .andExpect(jsonPath("$.password", is(request.password())))
                .andExpect(jsonPath("$.email", is(request.email())));
    }

    @Test
    @DisplayName("тест получения пользователя по id")
    void testThatGetUserByIdReturnsSucceed() throws Exception {
        User user = userRepository.findAll().getFirst();
        Long userId = user.getId();

        mockMvc.perform(get("/user/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is(user.getLogin())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    @DisplayName("тест удаления пользователя по id")
    void testThatDeleteUserReturnsSucceed() throws Exception {
        User user = userRepository.findAll().getFirst();
        Long userId = user.getId();

        mockMvc.perform(delete("/user/{user_id}", userId))
                .andExpect(status().isOk());

        assertTrue(userRepository.findAll().isEmpty());
    }

    private void saveTestUsers() {
        User user1 = new User(
                "Ryan Gosling",
                "pass",
                "ryan_gosling@mail.ru"
        );
        userRepository.save(user1);
    }
}
