package ru.t1.dto.request;

import ru.t1.util.TaskStatusEnum;

public record TaskRequest(
        String title,
        String description,
        TaskStatusEnum status
) {
}
