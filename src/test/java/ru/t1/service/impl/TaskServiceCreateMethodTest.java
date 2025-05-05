package ru.t1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.config.kafka.kafkaConfig.KafkaConfig;
import ru.t1.dto.request.TaskRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceCreateMethodTest {
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
    @DisplayName("тест создания задачи")
    void testThatCreateTaskReturnedSucceed() {
        final Long userId = 1L;
        TaskRequest task = new TaskRequest(
                "title",
                "description",
                TaskStatusEnum.TODO
        );

        UserResponse userDto = new UserResponse(
                userId,
                "Ryan Gosling",
                "pass",
                "ryan_gosling@yandex.ru"
        );

        Task taskEntity = new Task(
                "title",
                "description",
                new User(),
                TaskStatusEnum.TODO
        );

        User userEntity = new User(
                "Ryan Gosling",
                "pass",
                "ryan_gosling@yandex.ru"
        );

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        when(taskRepository.save(any())).thenReturn(taskEntity);
        when(userService.getUser(userId)).thenReturn(userDto);
        when(objectMapper.convertValue(userDto, User.class)).thenReturn(userEntity);

        TaskResponse createdTask = taskService.create(task, userId);

        verify(taskRepository).save(taskCaptor.capture());

        assertNotNull(createdTask);
        assertEquals(task.title(), createdTask.title());
        assertEquals(task.description(), createdTask.description());
        assertEquals(task.status(), createdTask.status());

        assertEquals(task.title(), taskCaptor.getValue().getTitle());
        assertEquals(task.description(), taskCaptor.getValue().getDescription());
        assertEquals(task.status(), taskCaptor.getValue().getStatus());
    }
}
