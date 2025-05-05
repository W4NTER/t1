package ru.t1.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.dto.TaskNotificationDto;
import ru.t1.mailer.MailSender;
import ru.t1.service.NotificationService;
import ru.t1.util.TaskStatusEnum;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {
    @Mock
    private MailSender mailSender;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationServiceImpl(mailSender);
    }

    @Test
    @DisplayName("тест на проверку формирования и правильной отправки сообщений")
    void testThatSendNotificationAndBuildEmailBodyReturnedSucceed() {
        final String EXPECTED_RESULT_BODY = "Статус был изменен с TODO на IN_PROGRESS";
        TaskNotificationDto notificationDto = new TaskNotificationDto(
                1L,
                TaskStatusEnum.TODO,
                TaskStatusEnum.IN_PROGRESS,
                "ryan_gosling@yandex.ru"
        );

        notificationService.sendNotification(notificationDto);

        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> emailBodyCaptor = ArgumentCaptor.forClass(String.class);

        verify(mailSender).sendMail(emailCaptor.capture(), emailBodyCaptor.capture());

        assertEquals(notificationDto.email(), emailCaptor.getValue());
        assertEquals(EXPECTED_RESULT_BODY, emailBodyCaptor.getValue());
    }
}