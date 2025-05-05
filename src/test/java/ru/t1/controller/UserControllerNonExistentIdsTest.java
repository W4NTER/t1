package ru.t1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.t1.dto.request.UserRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerNonExistentIdsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long NON_EXISTENT_ID = -1L;

    private final UserRequest userRequest = new UserRequest(
            "newLogin",
            "newPassword",
            "newemail@example.com"
    );

    @Test
    @DisplayName("тест обновления пользователя с несуществующим ID")
    void testThatUpdateUserWithNonExistentIdReturnsNotFound() throws Exception {
        mockMvc.perform(put("/user/update/{user_id}", NON_EXISTENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("тест получения пользователя с несуществующим ID")
    void testThatGetUserWithNonExistentIdReturnsNotFound() throws Exception {
        mockMvc.perform(get("/user/{user_id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("тест удаления пользователя с несуществующим ID")
    void testThatDeleteUserWithNonExistentIdReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/user/{user_id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }
}
