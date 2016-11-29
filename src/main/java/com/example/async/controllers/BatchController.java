package com.example.async.controllers;

import com.example.async.models.Task;
import com.example.async.services.batch.BatchService;
import com.example.async.services.common.SharedService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("batch/tasks")
@AllArgsConstructor
public class BatchController {
    final SharedService asyncSharedService;
    final BatchService batchService;
    /**
     * タスクをスタートする
     * @param quantity
     * @return
     */
    @PostMapping
    public CompletableFuture<Task> start(@RequestParam("quantity") Optional<Integer> quantity) {
        Task task = asyncSharedService.register(quantity.orElse(10));
        CompletableFuture<Task> future = batchService.batchExecute(task.getId());
        future.thenAccept(i ->{
            log.info("バッチ処理が完了したあとに呼ばれるコールバック");
        });
        return future;
    }
}
