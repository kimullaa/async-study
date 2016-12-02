package com.example.async.services.sse;

import com.example.async.models.Task;
import com.example.async.repositories.TaskRepository;
import com.example.async.services.common.Executor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class SseServiceImpl implements SseService{
    final TaskRepository taskRepository;
    final Executor executor;

    @Async
    @Override
    public void executeAll(Long id , SseEmitter emitter) {
        Task task = taskRepository.findOne(id);

        while (task.getDone() < task.getTotal()) {
            task = executor.execute(task);
            try {
                emitter.send(task);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        emitter.complete();
    }
}
