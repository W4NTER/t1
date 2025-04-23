package ru.t1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.starter_t1.aspect.annotation.HandlingResult;
import ru.starter_t1.aspect.annotation.LogException;
import ru.starter_t1.aspect.annotation.LogExecution;
import ru.starter_t1.aspect.annotation.LogTracking;
import ru.t1.config.kafka.kafkaConfig.KafkaConfig;
import ru.t1.dto.TaskNotificationDto;
import ru.t1.dto.request.TaskRequest;
import ru.t1.dto.response.TaskResponse;
import ru.t1.entity.Task;
import ru.t1.entity.User;
import ru.t1.exceptions.EntityNotFoundException;
import ru.t1.kafka.KafkaClientProducer;
import ru.t1.repository.TaskRepository;
import ru.t1.service.TaskService;
import ru.t1.service.UserService;
import ru.t1.service.util.mapper.TaskMapper;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final TaskMapper taskMapper;
    private final KafkaClientProducer kafkaClientProducer;
    private final KafkaConfig config;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            ObjectMapper objectMapper,
            UserService userService,
            TaskMapper taskMapper,
            KafkaClientProducer kafkaClientProducer,
            KafkaConfig config) {
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.taskMapper = taskMapper;
        this.kafkaClientProducer = kafkaClientProducer;
        this.config = config;
    }

    @Override
    @Transactional
    @LogExecution
    @HandlingResult
    public TaskResponse create(TaskRequest task, Long userId) {
        User user = objectMapper.convertValue(userService.getUser(userId), User.class);
        Task taskEntity = new Task(task.title(), task.description(), user, task.status());

        return taskMapper.toDto(taskRepository.save(taskEntity));
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
        checkStatus(task, taskEntity);
        taskEntity.setStatus(task.status());
        return taskMapper.toDto(taskRepository.save(taskEntity));
    }

    private void checkStatus(TaskRequest request, Task entity) {
        if (!request.status().equals(entity.getStatus()) && !entity.getUser().getEmail().isBlank()) {
            kafkaClientProducer.sendTo(
                    config.topic().notifications(),
                    new TaskNotificationDto(
                            entity.getId(),
                            entity.getStatus(),
                            request.status(),
                            System.getenv("EMAIL_RECIPIENT")
                    ));
        }
    }

    @Override
    @LogException
    @HandlingResult
    public TaskResponse getTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Task.class.getSimpleName(), id));
        return taskMapper.toDto(task);
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
                .map(taskMapper::toDto).toList();
    }
}
