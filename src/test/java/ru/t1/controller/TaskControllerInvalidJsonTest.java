package ru.t1.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.t1.entity.Task;
import ru.t1.entity.User;
import ru.t1.repository.TaskRepository;
import ru.t1.repository.UserRepository;
import ru.t1.util.TaskStatusEnum;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskControllerInvalidJsonTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        saveTestUsers();
        saveTestTasks();
    }

    private void saveTestUsers() {
        User user = new User(
                "login",
                "pass",
                "email"
        );

        userRepository.save(user);
    }

    private void saveTestTasks() {
        User user = userRepository.findAll().getFirst();
        Task task = new Task(
                "title",
                "description",
                user,
                TaskStatusEnum.TODO
        );

        taskRepository.save(task);
    }

    private final String badJson = """
            {
              this: 'some text',
              is: 'more text',
              bad: 3,
              json: 'complete'
            }
            """;

    @Test
    @DisplayName("тест неправильного ввода для create")
    void testThatCreateTaskBadJsonThrowsException() throws Exception {
        Long userId = userRepository.findAll().getFirst().getId();
        mockMvc.perform(post("/tasks/{user_id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(badJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("тест неправильного ввода для update")
    void testThatUpdateTaskBadJsonThrowsException() throws Exception {
        Long taskId = taskRepository.findAll().getFirst().getId();
        mockMvc.perform(put("/tasks/{task_id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(badJson))
                .andExpect(status().isBadRequest());
    }
}
