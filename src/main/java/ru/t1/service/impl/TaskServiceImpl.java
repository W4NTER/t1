package ru.t1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.aspect.annotation.HandlingResult;
import ru.t1.aspect.annotation.LogException;
import ru.t1.aspect.annotation.LogExecution;
import ru.t1.aspect.annotation.LogTracking;
import ru.t1.dto.request.TaskRequest;
import ru.t1.dto.response.TaskResponse;
import ru.t1.entity.Task;
import ru.t1.entity.User;
import ru.t1.exceptions.EntityNotFoundException;
import ru.t1.repository.TaskRepository;
import ru.t1.service.TaskService;
import ru.t1.service.UserService;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            ObjectMapper objectMapper,
            UserService userService) {
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    @LogExecution
    @HandlingResult
    public TaskResponse create(TaskRequest task, Long userId) {
        User user = objectMapper.convertValue(userService.getUser(userId), User.class);
        Task taskEntity = new Task(task.title(), task.description(), user);

        return convertToTaskResponse(taskRepository.save(taskEntity));
    }

    @Override
    @Transactional
    @LogException
    @HandlingResult
    public TaskResponse update(TaskRequest task, Long id) {
        Task taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Task.class.getSimpleName(), id));

        taskEntity.setTitle(task.title());
        taskEntity.setDescription(task.description());
        return convertToTaskResponse(taskRepository.save(taskEntity));
    }

    private TaskResponse convertToTaskResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(),
                task.getDescription(), task.getUser().getId());
    }

    @Override
    @LogException
    @HandlingResult
    public TaskResponse getTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Task.class.getSimpleName(), id));
        return convertToTaskResponse(task);
    }

    @Override
    @Transactional
    @LogExecution
    @LogException
    public void delete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Task.class.getSimpleName(), id));
        taskRepository.delete(task);
    }

    @Override
    @LogTracking
    @LogException
    public void justWaiting() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @LogException
    public List<TaskResponse> getAll(Long userId) {
        return taskRepository.findAllByUserId(userId).stream()
                .map(this::convertToTaskResponse).toList();
    }
}
