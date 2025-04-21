package ru.t1.service.util.mapper;

import org.springframework.stereotype.Component;
import ru.t1.dto.response.TaskResponse;
import ru.t1.entity.Task;

@Component
public class TaskMapper {

    public TaskResponse toDto(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getUser().getId(),
                task.getStatus());
    }
}
