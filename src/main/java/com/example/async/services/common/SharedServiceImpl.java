package com.example.async.services.common;

import com.example.async.models.Task;
import com.example.async.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SharedServiceImpl implements SharedService {
    TaskRepository taskRepository;

    @Override
    @Transactional
    public Task register(int total) {
        return taskRepository.save(new Task(total));
    }

    @Override
    @Transactional(readOnly = true)
    public Task status(Long id) {
        return taskRepository.findOne(id);
    }


}
