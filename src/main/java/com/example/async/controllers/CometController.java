package com.example.async.controllers;

import com.example.async.models.Task;
import com.example.async.services.comet.CometTaskService;
import com.example.async.services.common.SharedService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * comet用のコントローラ
 */
@Slf4j
@RestController
@RequestMapping("comet")
@AllArgsConstructor
public class CometController {
    final CometTaskService cometTaskService;
    final SharedService asyncSharedService;

    /**
     * タスクをスタートする
     *
     * @param quantity 総タスク数
     * @return
     */
    @PostMapping("deferred-result/tasks")
    public Task startDeferredResult(@RequestParam("quantity") Optional<Integer> quantity) {
        Task task = asyncSharedService.register(quantity.orElse(10));

        cometTaskService.executeAllDeferredResultVersion(task.getId());
        return task;
    }


    /**
     * 現在のタスクの状態を表示する(Comet版)
     * FIXME: 存在しないタスクIDや完了済みのタスクIDを指定すると延々待つ
     *
     * @param id タスクID
     * @return
     */
    @GetMapping("deferred-result/tasks/{id}")
    public DeferredResult<Task> statusDeferredResult(@PathVariable("id") Long id) {
        return cometTaskService.subscribeDeferredResultVersion(id);
    }

    /**
     * タスクをスタートする
     *
     * @param quantity 総タスク数
     * @return
     */
    @PostMapping("completable-future/tasks")
    public Task startCompletableFuture(@RequestParam("quantity") Optional<Integer> quantity) {
        Task task = asyncSharedService.register(quantity.orElse(10));

        cometTaskService.executeAllCompletableFutureVersion(task.getId());
        return task;
    }


    /**
     * 現在のタスクの状態を表示する(Comet版)
     * 存在しないタスクIDや完了済みのタスクIDを指定すると例外が発生する
     *
     * @param id タスクID
     * @return
     */
    @GetMapping("completable-future/tasks/{id}")
    public CompletableFuture<Task> statusCompletableFuture(@PathVariable("id") Long id) {
        log.info("controller ");
        CompletableFuture<Task> future = cometTaskService.subscribeCompletableFutureVersion(id);
        future.thenAccept(i -> {
            log.info("CompletableFutureのコールバック処理");
        });
        return future;
    }

}
