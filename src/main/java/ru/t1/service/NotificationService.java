package ru.t1.service;

import ru.t1.dto.TaskNotificationDto;

public interface NotificationService {
    void sendNotification(TaskNotificationDto notification);
}
