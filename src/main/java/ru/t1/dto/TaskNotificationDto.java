package ru.t1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.t1.util.TaskStatusEnum;

public record TaskNotificationDto(
        @JsonProperty("task_id")
        Long taskId,

        @JsonProperty("last_status")
        TaskStatusEnum lastStatus,

        @JsonProperty("new_status")
        TaskStatusEnum newStatus,

        @JsonProperty("email")
        String email
) {
}
