package com.example.async.services.batch;

import com.example.async.models.Task;
import com.example.async.repositories.TaskRepository;
import com.example.async.services.common.Executor;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Async
@Service
@AllArgsConstructor
public class BatchServiceImpl implements BatchService {
    TaskRepository taskRepository;
    Executor executor;

    @Override
    public CompletableFuture<Task> batchExecute(Long id) {
        Task task = taskRepository.findOne(id);

        while (task.getDone() < task.getTotal()) {
            task = executor.execute(task);
        }

        return CompletableFuture.completedFuture(task);
    }
}
