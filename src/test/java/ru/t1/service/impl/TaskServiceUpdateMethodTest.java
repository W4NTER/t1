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
import ru.t1.config.kafka.kafkaConfig.KafkaTopic;
import ru.t1.dto.TaskNotificationDto;
import ru.t1.dto.request.TaskRequest;
import ru.t1.dto.response.TaskResponse;
import ru.t1.entity.Task;
import ru.t1.entity.User;
import ru.t1.exceptions.EntityNotFoundException;
import ru.t1.kafka.KafkaClientProducer;
import ru.t1.repository.TaskRepository;
import ru.t1.service.TaskService;
import ru.t1.service.UserService;
import ru.t1.service.util.mapper.TaskMapper;
import ru.t1.util.TaskStatusEnum;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceUpdateMethodTest {
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

    @Mock
    private KafkaTopic kafkaTopic;

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
    @DisplayName("тест обновления задачи")
    void testThatUpdateTaskReturnedSucceed() {
        final Long taskId = 1L;
        Task taskEntity = new Task(
                "Ryan Gosling",
                "again",
                new User(),
                TaskStatusEnum.SUCCESS
        );

        TaskRequest taskRequest = new TaskRequest(
                "title",
                "description",
                TaskStatusEnum.TODO
        );

        when(taskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(kafkaConfig.topic()).thenReturn(kafkaTopic);
        when(kafkaTopic.notifications()).thenReturn("test_topic");

        TaskResponse res = taskService.update(taskRequest, taskId);

        assertNotNull(res);
        assertEquals(taskRequest.title(), res.title());
        assertEquals(taskRequest.description(), res.description());
        assertEquals(taskRequest.status(), res.status());
        verify(kafkaClientProducer).sendTo(eq("test_topic"), any(TaskNotificationDto.class));
    }

    @Test
    @DisplayName("тест обновления задачи — статус не изменился, Kafka не вызывается")
    void testThatKafkaNotCalledWhenStatusNotChanged() {
        final Long taskId = 1L;
        TaskStatusEnum unchangedStatus = TaskStatusEnum.TODO;

        Task taskEntity = new Task(
                "Ryan Gosling",
                "again",
                new User(),
                unchangedStatus
        );

        TaskRequest taskRequest = new TaskRequest(
                "title",
                "description",
                unchangedStatus
        );

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        when(taskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);

        TaskResponse res = taskService.update(taskRequest, taskId);

        verify(kafkaClientProducer, never()).sendTo(anyString(), any());
        verify(taskRepository).save(taskCaptor.capture());

        assertNotNull(res);
        assertEquals(taskRequest.title(), res.title());
        assertEquals(taskRequest.description(), res.description());
        assertEquals(taskRequest.status(), res.status());

        assertEquals(taskRequest.title(), taskCaptor.getValue().getTitle());
        assertEquals(taskRequest.description(), taskCaptor.getValue().getDescription());
        assertEquals(taskRequest.status(), taskCaptor.getValue().getStatus());
    }

    @Test
    @DisplayName("тест обновления задачи, выброс ошибки EntityNotFound")
    void testThatUpdateTaskThrowingException() {
        final Long userId = 1L;
        TaskRequest taskRequest = new TaskRequest(
                "title",
                "description",
                TaskStatusEnum.TODO
        );

        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> taskService.update(taskRequest, userId));
    }
}