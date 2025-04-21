package ru.t1.kafka.utils;

import org.apache.kafka.common.header.Headers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.nio.charset.StandardCharsets;

public class MessageDeserializer<T> extends JsonDeserializer<T> {
    private final static Logger LOGGER = LogManager.getLogger();

    private String getMessage(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        try {
            return super.deserialize(topic, headers, data);
        } catch (Exception e) {
            LOGGER.warn("Error by deserialize the message {}", getMessage(data), e);
            return null;
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return super.deserialize(topic, data);
        } catch (Exception e) {
            LOGGER.warn("Error by deserialize the message {}", getMessage(data), e);
            return null;
        }
    }
}
