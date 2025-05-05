package ru.t1.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerInvalidJsonTest {

    @Autowired
    private MockMvc mockMvc;

    private final String badJson = """
            {
              this: 'some text',
              is: 'more text',
              bad: 3,
              json: 'complete'
            }
            """;

    @Test
    @DisplayName("тест неправильного ввода JSON при регистрации пользователя")
    void testThatRegisterUserWithBadJsonThrowsException() throws Exception {
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("тест неправильного ввода JSON при обновлении пользователя")
    void testThatUpdateUserWithBadJsonThrowsException() throws Exception {
        mockMvc.perform(put("/user/update/{user_id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());
    }
}
