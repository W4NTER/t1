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
import ru.t1.dto.request.TaskRequest;
import ru.t1.entity.Task;
import ru.t1.entity.User;
import ru.t1.repository.TaskRepository;
import ru.t1.repository.UserRepository;
import ru.t1.util.TaskStatusEnum;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        saveTestUsers();
        saveTestTasks();
    }

    @Test
    @DisplayName("тест проверки создания задачи")
    void testThatCreateTaskReturnedSucceed() throws Exception {
        Long userId = userRepository.findAll().getFirst().getId();
        TaskRequest request = new TaskRequest(
                "Ryan Gosling",
                "is a best friend of the humans ;)",
                TaskStatusEnum.TODO
        );

        mockMvc.perform(post("/tasks/{user_id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(request.title())))
                .andExpect(jsonPath("$.description", is(request.description())))
                .andExpect(jsonPath("$.status", is(request.status().toString())));
    }

    @Test
    @DisplayName("тест обновления задачи")
    void testThatUpdateTaskReturnedSucceed() throws Exception {
        Task task = taskRepository.findAll().getFirst();
        Long taskId = task.getId();
        TaskRequest request = new TaskRequest(
                "Ryan Gosling",
                "is a best friend of the humans ;)",
                task.getStatus()
        );

        mockMvc.perform(put("/tasks/{task_id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(taskId.intValue())))
                .andExpect(jsonPath("$.title", is(request.title())))
                .andExpect(jsonPath("$.description", is(request.description())))
                .andExpect(jsonPath("$.status", is(request.status().toString())));
    }

    @Test
    @DisplayName("тест получения задачи по id")
    void testThatGetTaskByIdReturnedSucceed() throws Exception {
        Task thirstTask = taskRepository.findAll().getFirst();
        Long taskId = thirstTask.getId();

        mockMvc.perform(get("/tasks/{task_id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(thirstTask.getTitle())))
                .andExpect(jsonPath("$.description", is(thirstTask.getDescription())))
                .andExpect(jsonPath("$.status", is(thirstTask.getStatus().toString())));
    }

    @Test
    @DisplayName("тест удаления задачи по id")
    void testThatDeleteTaskReturnedSucceed() throws Exception {
        Long taskId = taskRepository.findAll().getFirst().getId();
        mockMvc.perform(delete("/tasks/{task_id}", taskId))
                .andExpect(status().isOk());

        assertEquals(2, taskRepository.findAll().size());
        assertTrue(taskRepository.findById(taskId).isEmpty());
    }

    @Test
    @DisplayName("тест получения всех задач пользователя")
    void testThatGetAllTasksReturnedSucceed() throws Exception {
        Long userId = userRepository.findAll().getFirst().getId();
        mockMvc.perform(get("/tasks/all/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].title").value("title1"))
                .andExpect(jsonPath("$[1].title").value("title2"))
                .andExpect(jsonPath("$[2].title").value("title3"));
    }

    private void saveTestUsers() {
        User user1 = new User(
                "Ryan Gosling",
                "pass",
                "ryan_gosling@mail.ru"
        );

        userRepository.save(user1);
    }

    private void saveTestTasks() {
        User user = userRepository.findAll().getFirst();
        Task task1 = new Task(
                "title1",
                "description1",
                user,
                TaskStatusEnum.TODO
        );
        Task task2 = new Task(
                "title2",
                "description2",
                user,
                TaskStatusEnum.IN_WORK
        );
        Task task3 = new Task(
                "title3",
                "description3",
                user,
                TaskStatusEnum.SUCCESS
        );

        taskRepository.saveAll(Arrays.asList(task1, task2, task3));
    }
}