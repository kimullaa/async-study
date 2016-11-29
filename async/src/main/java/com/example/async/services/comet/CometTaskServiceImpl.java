package com.example.async.services.comet;

import com.example.async.models.Task;
import com.example.async.repositories.TaskRepository;
import com.example.async.services.common.Executor;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CometTaskServiceImpl implements CometTaskService {
    final TaskRepository taskRepository;
    final Executor executor;
    final DeferredResultTopic deferredResultTopic;
    final CompletableFutureTopicStore store;

    @Async
    @Override
    public void executeAllDeferredResultVersion( Long id) {
        Task task = taskRepository.findOne(id);

        while (task.getDone() < task.getTotal()) {
            task = executor.execute(task);
            deferredResultTopic.publish(task);
        }
    }

    @Async
    @Override
    public void executeAllCompletableFutureVersion(Long id) {
        Task task = taskRepository.findOne(id);
        CompletableFutureTopic topic = new CompletableFutureTopic(task.getId());
        store.add(topic);

        while (task.getDone() < task.getTotal()) {
            task = executor.execute(task);
            topic.publish(task);
        }

    }
}
