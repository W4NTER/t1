package ru.t1.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.aspect.annotation.LogExecution;
import ru.t1.dto.TaskNotificationDto;
import ru.t1.service.NotificationService;

import java.util.List;

@Component
public class KafkaClientConsumer {
    private final static Logger LOGGER = LogManager.getLogger();
    private final NotificationService notificationService;

    public KafkaClientConsumer(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @LogExecution
    @KafkaListener(
            id = "${app.kafka.consumer.group-id}",
            topics = "${app.kafka.topic.notifications}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(
            @Payload List<TaskNotificationDto> messageList,
            Acknowledgment ack) {
        try {
            LOGGER.info(messageList);
            messageList
                    .forEach(notificationService::sendNotification);
        } finally {
            ack.acknowledge();
        }
    }
}
