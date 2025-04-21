package ru.t1.config.kafka.kafkaConfig;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.kafka")
public record KafkaConfig(
        @NotNull KafkaBootstrap bootstrap,
        @NotNull KafkaConsumer consumer,
        @NotNull KafkaProducer producer,
        @NotNull KafkaTopic topic,
        @NotNull KafkaListener listener
) {}

