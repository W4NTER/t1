package ru.t1.service.util.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.dto.response.TaskResponse;
import ru.t1.entity.Task;
import ru.t1.entity.User;
import ru.t1.util.TaskStatusEnum;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {

    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
    }

    @Test
    @DisplayName("тест на проверку маппинга из Task в TaskResponse")
    void testThatMapTaskToDtoReturnedSucceed() {
        //Нет возможности задать id через сеттер или конструктор (специально выпилил),
        // а пытаться засунуть id в сущности тоже не очень круто, поэтому будет null
        Task task = new Task(
                "Send mail to Ryan Gosling",
                "I'm not crazy",
                new User(),
                TaskStatusEnum.TODO
        );

        TaskResponse res = taskMapper.toDto(task);

        assertNotNull(res);
        assertEquals(task.getTitle(), res.title());
        assertEquals(task.getDescription(), res.description());
        assertNull(res.userId());
        assertEquals(task.getStatus(), res.status());
        assertNull(res.id());
    }
}