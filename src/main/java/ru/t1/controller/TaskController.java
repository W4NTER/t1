package ru.t1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.t1.dto.request.TaskRequest;
import ru.t1.dto.response.TaskResponse;
import ru.t1.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //нужно получать id из сессии,
    // но для этого проекта я не делал аутентификацию и авторизацию
    @PostMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse createTask(
            @RequestBody TaskRequest task,
            @PathVariable("user_id") Long userId) {
        return taskService.create(task, userId);
    }

    @PutMapping("/{task_id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse updateTask(
            @PathVariable("task_id") Long taskId,
            @RequestBody TaskRequest task
    ) {
        return taskService.update(task, taskId);
    }

    @GetMapping("/{task_id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse getTask(@PathVariable("task_id") Long taskId) {
        return taskService.getTask(taskId);
    }

    @DeleteMapping("/{task_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable("task_id") Long taskId) {
        taskService.delete(taskId);
    }

    @GetMapping("/waiting")
    @ResponseStatus(HttpStatus.OK)
    public String waiting() {
        taskService.justWaiting();
        return "sleep is a best way to be healthy";
    }

    @GetMapping("/all/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponse> getAllTasks(
            @PathVariable("user_id") Long userId
    ) {
        return taskService.getAll(userId);
    }
}
