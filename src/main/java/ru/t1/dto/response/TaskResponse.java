package ru.t1.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.t1.util.TaskStatusEnum;

public record TaskResponse(

        @JsonProperty("id")
        Long id,

        @JsonProperty("title")
        String title,

        @JsonProperty("description")
        String description,

        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("status")
        TaskStatusEnum status
) {
}
