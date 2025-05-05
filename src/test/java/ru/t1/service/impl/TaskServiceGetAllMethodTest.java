package ru.t1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.config.kafka.kafkaConfig.KafkaConfig;
import ru.t1.dto.response.TaskResponse;
import ru.t1.dto.response.UserResponse;
import ru.t1.entity.Task;
import ru.t1.entity.User;
import ru.t1.kafka.KafkaClientProducer;
import ru.t1.repository.TaskRepository;
import ru.t1.service.TaskService;
import ru.t1.service.UserService;
import ru.t1.service.util.mapper.TaskMapper;
import ru.t1.util.TaskStatusEnum;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceGetAllMethodTest {
    @Mock
    private UserService userService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private KafkaClientProducer kafkaClientProducer;

    @Mock
    private KafkaConfig kafkaConfig;

    private final TaskMapper taskMapper = new TaskMapper();

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(
                taskRepository,
                objectMapper,
                userService,
                taskMapper,
                kafkaClientProducer,
                kafkaConfig
        );
    }

    @Test
    @DisplayName("тест на получение всех тасок для пользователя")
    void testThatGetAllTasksReturnedSucceed() {
        Long userId = 1L;
        Task task1 = new Task(
                "Ryan Gosling",
                "thirst",
                new User(),
                TaskStatusEnum.TODO
        );

        Task task2 = new Task(
                "Ryan Gosling",
                "second",
                new User(),
                TaskStatusEnum.TODO
        );

        Task task3 = new Task(
                "Ryan Gosling",
                "third",
                new User(),
                TaskStatusEnum.TODO
        );
        User user = new User();
        UserResponse userResponse = new UserResponse(1L, "", "", "");

        when(userService.getUser(userId)).thenReturn(userResponse);
        when(objectMapper.convertValue(userResponse, User.class)).thenReturn(user);
        when(taskRepository.findAllByUser(user))
                .thenReturn(List.of(task1, task2, task3));

        List<TaskResponse> res = taskService.getAll(userId);

        assertNotNull(res);
        assertEquals(task1.getDescription(), res.getFirst().description());
        assertEquals(task1.getTitle(), res.getFirst().title());
        assertEquals(task1.getStatus(), res.getFirst().status());

        assertEquals(task2.getDescription(), res.get(1).description());
        assertEquals(task2.getTitle(), res.get(1).title());
        assertEquals(task2.getStatus(), res.get(1).status());

        assertEquals(task3.getDescription(), res.get(2).description());
        assertEquals(task3.getTitle(), res.get(2).title());
        assertEquals(task3.getStatus(), res.get(2).status());
    }

    @Test
    @DisplayName("тест на получение пустого списка тасок пользователя")
    void testThatGetAllTasksWithEmptyListReturnedSucceed() {
        Long userId = 1L;
        User user = new User();
        UserResponse userResponse = new UserResponse(1L, "", "", "");

        when(userService.getUser(userId)).thenReturn(userResponse);
        when(objectMapper.convertValue(userResponse, User.class)).thenReturn(user);
        when(taskRepository.findAllByUser(user)).thenReturn(new ArrayList<>());

        List<TaskResponse> res = taskService.getAll(userId);

        assertNotNull(res);
        assertEquals(0, res.size());
    }
}
