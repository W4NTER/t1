package ru.t1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.config.kafka.kafkaConfig.KafkaConfig;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceDeleteMethodTest {
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
    @DisplayName("тест удаления таски")
    void testThatDeleteTaskReturnedSucceed() {
        Long taskId = 1L;
        Task taskEntity = new Task(
                "Ryan Gosling",
                "again",
                new User(),
                TaskStatusEnum.TODO
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        taskService.delete(taskId);

        verify(taskRepository).delete(taskEntity);
    }

    @Test
    @DisplayName("тест удаления таски, выброс ошибки EntityNotFound")
    void testThatDeleteTaskThrowsException() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> taskService.delete(taskId));
    }
}
