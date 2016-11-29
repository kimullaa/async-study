package com.example.async.services.polling;

import com.example.async.models.Task;
import com.example.async.repositories.TaskRepository;
import com.example.async.services.common.Executor;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PollingTaskServiceImpl implements PollingTaskService {
    TaskRepository taskRepository;
    Executor executor;

    @Async
    @Override
    public void executeAll(Long id) {
        Task task = taskRepository.findOne(id);

        while (task.getDone() < task.getTotal()) {
            task = executor.execute(task);
        }
    }

}
