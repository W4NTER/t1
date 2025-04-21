package ru.t1.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.t1.config.kafka.kafkaConfig.KafkaConfig;
import ru.t1.dto.TaskNotificationDto;
import ru.t1.kafka.utils.MessageDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KafkaConfig.class)
public class ConsumerConfiguration {
    private final static Logger LOGGER = LogManager.getLogger();
    private final KafkaConfig kafka;

    public ConsumerConfiguration(KafkaConfig kafka) {
        this.kafka = kafka;
    }

    @Bean
    public ConsumerFactory<String, TaskNotificationDto> consumerListenerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.bootstrap().server());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafka.consumer().groupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.dto.TaskNotificationDto");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafka.consumer().sessionTimeout());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, kafka.consumer().maxPartitionFetchBytes());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafka.consumer().maxPollRecords());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafka.consumer().maxPollIntervalMs());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafka.consumer().autoOffsetReset());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, MessageDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, MessageDeserializer.class);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafka.consumer().heartbeatIntervalMs());



        DefaultKafkaConsumerFactory<String, TaskNotificationDto> factory =
                new DefaultKafkaConsumerFactory<String, TaskNotificationDto>(props);
        factory.setKeyDeserializer(new StringDeserializer());
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TaskNotificationDto> kafkaListenerContainerFactory(
            @Qualifier("consumerListenerFactory") ConsumerFactory<String, TaskNotificationDto> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, TaskNotificationDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, factory);
        return factory;
    }

    private <T> void factoryBuilder(
            ConsumerFactory<String, T> consumerFactory,
            ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(kafka.consumer().batchListener());
        factory.setConcurrency(kafka.consumer().concurrency());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(kafka.consumer().pollTimeout());
        factory.getContainerProperties().setMicrometerEnabled(kafka.consumer().micrometerEnabled());
        factory.setCommonErrorHandler(errorHandler());
    }

    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(
                new FixedBackOff(
                        kafka.consumer().fixedBackoffIntervalMs(),
                        kafka.consumer().fixedBackoffMaxAttempts()));
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners(((record, ex, deliveryAttempt) ->
                LOGGER.error("RetryListeners message {}, offset = {}, deliveryAttempt = {}",
                        ex.getMessage(), record.offset(), deliveryAttempt)
                ));

        return handler;
    }
}
