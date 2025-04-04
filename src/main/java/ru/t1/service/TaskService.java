package ru.t1.service;

import ru.t1.dto.request.TaskRequest;
import ru.t1.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse create(TaskRequest task, Long userId);

    TaskResponse update(TaskRequest task, Long id);

    TaskResponse getTask(Long id);

    void delete(Long id);

    void justWaiting();

    List<TaskResponse> getAll(Long userId);
}
