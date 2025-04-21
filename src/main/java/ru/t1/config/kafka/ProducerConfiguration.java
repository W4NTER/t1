package ru.t1.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.t1.config.kafka.kafkaConfig.KafkaConfig;
import ru.t1.dto.TaskNotificationDto;
import ru.t1.kafka.KafkaClientProducer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KafkaConfig.class)
public class ProducerConfiguration {
    private final KafkaConfig kafka;

    public ProducerConfiguration(KafkaConfig kafka) {
        this.kafka = kafka;
    }

    @Bean
    public KafkaTemplate<String, TaskNotificationDto> kafkaTemplate(ProducerFactory<String, TaskNotificationDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnProperty(
            value = "app.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true
    )
    public KafkaClientProducer producerClient(@Qualifier("kafkaTemplate") KafkaTemplate kafkaTemplate) {
        kafkaTemplate.setDefaultTopic(kafka.topic().notifications());
        return new KafkaClientProducer(kafkaTemplate);
    }

    @Bean
    public ProducerFactory<String, TaskNotificationDto> producerClientFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.bootstrap().server());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(props);
    }
}
