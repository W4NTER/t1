package ru.t1.config.kafka.kafkaConfig;

public record KafkaConsumer(
        String groupId,
        Integer maxPollRecords,
        Integer maxPollIntervalMs,
        Integer maxPartitionFetchBytes,
        Integer sessionTimeout,
        String autoOffsetReset,
        Integer fixedBackoffMaxAttempts,
        Integer fixedBackoffIntervalMs,
        Integer heartbeatIntervalMs,
        Integer pollTimeout,
        Integer concurrency,
        Boolean batchListener,
        Boolean micrometerEnabled
        ) {}
