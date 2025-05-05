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
import ru.t1.dto.request.TaskRequest;
import ru.t1.util.TaskStatusEnum;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskControllerNonExistentIdsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long UNEXPECTED_ID = -1L;

    private final TaskRequest taskRequest = new TaskRequest(
            "title",
            "desc",
            TaskStatusEnum.TODO
    );

    @Test
    @DisplayName("тест проверки метода create с несуществующим id в пути")
    void testThatCreateTaskWithNonExistentUserIdThrowingException() throws Exception {
        mockMvc.perform(post("/tasks/{user_id}", UNEXPECTED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("тест проверки метода update с несуществующим id в пути")
    void testThatUpdateTaskWithNonExistentTaskIdThrowingException() throws Exception {
        mockMvc.perform(put("/tasks/{task_id}", UNEXPECTED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("тест проверки метода getTask с несуществующим id в пути")
    void testThatGetTaskWithNonExistentTaskIdThrowingException() throws Exception {
        mockMvc.perform(get("/tasks/{task_id}", UNEXPECTED_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("тест проверки метода deleteTask с несуществующим id в пути")
    void testThatDeleteTaskWithNonExistentTaskIdThrowingException() throws Exception {
        mockMvc.perform(delete("/tasks/{task_id}", UNEXPECTED_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("тест проверки метода getAllTasks с несуществующим id в пути")
    void testThatGetAllTasksWithNonExistentUserIdThrowingException() throws Exception {
        mockMvc.perform(get("/tasks/all/{user_id}", UNEXPECTED_ID))
                .andExpect(status().isNotFound());
    }
}
