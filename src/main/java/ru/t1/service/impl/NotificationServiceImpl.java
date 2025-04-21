package ru.t1.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.t1.aspect.annotation.LogExecution;
import ru.t1.dto.TaskNotificationDto;
import ru.t1.mailer.MailSender;
import ru.t1.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final MailSender mailSender;
    private final static String EMAIL_TEXT = "Статус был изменен с %s на %s";

    public NotificationServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async("notificationExecutor")
    @LogExecution
    @Override
    public void sendNotification(TaskNotificationDto notification) {
        mailSender.sendMail(notification.email(), buildEmailBody(notification));
    }

    private String buildEmailBody(TaskNotificationDto notification) {
        return String.format(EMAIL_TEXT, notification.lastStatus(), notification.newStatus());
    }
}
