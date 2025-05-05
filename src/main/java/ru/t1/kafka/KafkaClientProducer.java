package ru.t1.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import ru.starter_t1.aspect.annotation.LogExecution;

public class KafkaClientProducer {
    private final static Logger LOGGER = LogManager.getLogger();
    private final KafkaTemplate kafkaTemplate;

    public KafkaClientProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @LogExecution
    public void send(Object data) {
        try {
            kafkaTemplate.sendDefault(data);
            kafkaTemplate.flush();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @LogExecution
    public void sendTo(String topic, Object data) {
        try {
            kafkaTemplate.send(topic, data);
            kafkaTemplate.flush();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
