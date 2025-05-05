package ru.t1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.t1.dto.TaskNotificationDto;
import ru.t1.dto.request.TaskRequest;
import ru.t1.entity.Task;
import ru.t1.entity.User;
import ru.t1.kafka.KafkaClientProducer;
import ru.t1.repository.TaskRepository;
import ru.t1.repository.UserRepository;
import ru.t1.service.NotificationService;
import ru.t1.util.TaskStatusEnum;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class KafkaTest extends KafkaContainerIntegrationTest {
    @Autowired
    private KafkaTemplate<String, TaskNotificationDto> kafkaTemplate;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private KafkaClientProducer kafkaClientProducer;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        saveTestUser();
        saveTestTask();
    }

    @Test
    @DisplayName("тест на отправку сообщения в кафку и вызов notification сервиса")
    void testThatMessageIsConsumedAndNotificationSent() {
        TaskNotificationDto dto = new TaskNotificationDto(
                123L,
                TaskStatusEnum.TODO,
                TaskStatusEnum.IN_WORK,
                "test@example.com"
        );

        kafkaTemplate.send("t1_task_notifications", dto);


        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
                verify(notificationService, times(1))
                        .sendNotification(dto)
        );
    }

    @Test
    @DisplayName("тест на update задачи с изменением статуса")
    void testThatStatusChangeProducesKafkaMessage() throws Exception {
        Long taskId = taskRepository.findAll().getFirst().getId();
        TaskRequest request = new TaskRequest(
                "title",
                "desc",
                TaskStatusEnum.IN_WORK
        );

        mockMvc.perform(put("/tasks/{task_id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
                verify(kafkaClientProducer, times(1))
                        .sendTo(anyString(), any(TaskNotificationDto.class))
        );
    }

    private void saveTestUser() {
        User user = new User(
                "login",
                "pass",
                "email@email.ru"
        );

        userRepository.save(user);
    }

    private void saveTestTask() {
        Task task = new Task(
                "title",
                "desc",
                userRepository.findAll().getFirst(),
                TaskStatusEnum.TODO
        );

        taskRepository.save(task);
    }
}
