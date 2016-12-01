package com.example.async.services.comet;

import com.example.async.models.Task;
import com.example.async.repositories.TaskRepository;
import com.example.async.services.common.Executor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class CometTaskServiceImpl implements CometTaskService {
    final TaskRepository taskRepository;
    final Executor executor;
    final DeferredResultTopic deferredResultTopic;
    final CompletableFutureTopicStore store;

    @Async
    @Override
    public void executeAllDeferredResultVersion(Long id) {
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

        store.remove(task.getId());
    }

    @Async
    @Override
    public DeferredResult<Task> subscribeDeferredResultVersion(Long id) {
        final DeferredResult<Task> deferredResult = new DeferredResult<Task>();
        deferredResultTopic.subscribe(id, deferredResult);
        return deferredResult;
    }

    @Async
    @Override
    public CompletableFuture subscribeCompletableFutureVersion(Long id) {
        log.info(id.toString());
        CompletableFuture<Task> future = store.get(id).subscribe();
        return future;
    }
}
